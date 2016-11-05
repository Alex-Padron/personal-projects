package Public;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.google.gson.reflect.TypeToken;

import DataStructures.Path;
import Messages.PublisherRequest;
import Messages.PublisherResponse;
import Server.MultiClientServer;

/**
 * Publisher of type T
 */
public class Publisher<T> extends MultiClientServer {
    private final Lock lock;
    private final MasterClient m_client;
    private final Map<Path, T> path_data;
    private final Set<Path> to_remove;
    private final int port;
    private final String hostname;
    private final Type response_type;

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
	this.response_type = new TypeToken<PublisherResponse<T>>(){}.getType();
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

    protected void handle_client_connection(Socket socket) throws IOException {
	BufferedReader from_client =
	    new BufferedReader(new InputStreamReader(socket.getInputStream()));
	DataOutputStream to_client =
	    new DataOutputStream(socket.getOutputStream());
	while (true) {
	    String client_data_raw = from_client.readLine();
	    if (client_data_raw == null) return;
	    PublisherRequest client_message =
		request_from_string(client_data_raw);
	    if (client_message == null || !(validate(client_message))) {
		write(to_client, invalid_request());
		continue;
	    }
	    this.lock.lock();
	    if (!path_data.containsKey(client_message.path))
		write(to_client, not_publishing_response());
	    else {
		switch (client_message.type) {
		case QUERY_PUBLISHING_PATH:
		    write(to_client, publishing_response());
		    break;
		case GET_PATH_VALUE:
		    write(to_client, data_response(path_data.get(client_message.path)));
		    break;
		default:
		    break;
		}
	    }
	    this.lock.unlock();
	}
    }

    private synchronized PublisherRequest request_from_string(String s) {
	try {
	    return parser.fromJson(s, PublisherRequest.class);
	} catch (Exception e) {
	    return null;
	}
    }

    private boolean validate(PublisherRequest req) {
	return req.path.length() > 0;
    }

    private void write(DataOutputStream to_client, PublisherResponse<T> r) throws IOException {
	to_client.writeBytes(parser.toJson(r, response_type) + "\n");
    }

    private PublisherResponse<T> invalid_request() {
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
