package Public;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import DataStructures.Path;
import Messages.PublisherRequest;
import Messages.PublisherResponse;
import Messages.Serializable;
import Messages.Bodies.PathBody;
import Server.MultiClientServer;

/**
 * Publisher of type T
 */
public class Publisher<T> extends MultiClientServer<PublisherRequest, PublisherResponse<T>> {
    private final Lock lock;
    private final MasterClient m_client;
    private final Map<Path, T> path_data;
    private final Set<Path> to_remove;
    private final int port;
    private final String hostname;

    /**
     * @param port: for the publisher to bind on
     * @param master_hostname, master_port: location of the master
     * @param path_data: initial set of paths/values that the publisher
     * is publishing
     */
    public Publisher(int port, String master_hostname, int master_port,
		     Map<Path, T> path_data) throws IOException
    {
	this.port = port;
	this.hostname = "localhost";
	this.lock = new ReentrantLock();
	this.server_socket = new ServerSocket(port);
	this.server_socket.setReuseAddress(true);
	this.m_client = new MasterClient(master_hostname, master_port);
	this.path_data = path_data;
	this.to_remove = new HashSet<>();
    }

    /**
     * @param path: name of the path this is publishing
     * @param value: new value for that path
     */
    public void put_path(Path path, T value) {
	this.lock.lock();
	this.path_data.put(path, value);
	this.lock.unlock();
    }

    /**
     * @param path: of the path to remove
     */
    public void remove_path(Path path) {
	this.lock.lock();
	this.path_data.remove(path);
	this.to_remove.add(path);
	this.lock.unlock();
    }

    /**
     * Update the master with all of the paths that the publisher
     * is currently publishing, and removes all paths the publisher
     * is no longer publishing
     */
    public void send_paths_to_master() throws IOException {
	this.lock.lock();
	for (Path path : this.to_remove) {
	    this.m_client.remove_path(path);
	}
	for (Path path : this.path_data.keySet()) {
	    this.m_client.register_path(path,
					this.hostname,
					this.port);
	}
	this.lock.unlock();
    }

    @Override
    protected Optional<PublisherRequest> parse_client_string(String s) {
	Optional<PublisherRequest> req =
	    Serializable.parse(s, PublisherRequest.class);
	if ((!req.isPresent()) || (!req.get().validate()))
	    return Optional.empty();
	return req;
    }

    protected PublisherResponse<T> handle_request(PublisherRequest req) {
    this.lock.lock();
	switch (req.type) {
	case QUERY_PUBLISHING_PATH: {
	    PathBody body = Serializable.parse_exn(req.body, PathBody.class);
	    if (path_data.containsKey(body.path)) {
		this.lock.unlock();
		return publishing_response();
	    }
	    this.lock.unlock();
	    return not_publishing_response();
	}
	case GET_PATH_VALUE: {
	    PathBody body = Serializable.parse_exn(req.body, PathBody.class);
	    if (path_data.containsKey(body.path)) {
		this.lock.unlock();
		return data_response(path_data.get(body.path));
	    }
	    this.lock.unlock();
	    return not_publishing_response();
	}
	default:
	    this.lock.unlock();
	    return not_publishing_response();
	}
    }

    @Override
    protected PublisherResponse<T> invalid_request() {
	return new PublisherResponse<T>(PublisherResponse.T.INVALID_REQUEST);
    }

    private PublisherResponse<T> not_publishing_response() {
	return new PublisherResponse<T>(PublisherResponse.T.NOT_PUBLISHING_PATH);
    }

    private PublisherResponse<T> publishing_response() {
	return new PublisherResponse<T>(PublisherResponse.T.AM_PUBLISHING_PATH);
    }

    private PublisherResponse<T> data_response(T data) {
	return new PublisherResponse<T>(data);
    }
}
