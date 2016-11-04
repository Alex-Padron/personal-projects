package Master;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;

import Master.Paths.Path;
import Master.Paths.PublisherPaths;
import Messages.MasterRequest;
import Messages.MasterResponse;

/**
 * Master server which allows publishers to register for paths,
 * and clients to query which publisher is publishing a given path
 */
public class Master implements Runnable {
    private ServerSocket server_socket;
    private PublisherPaths data;
    private Gson parser;
    private ExecutorService executor;

    /**
     * @param port: for the master to bind on
     */
    public Master(int port) throws IOException {
	this.server_socket = new ServerSocket();
	this.server_socket.setReuseAddress(true);
	this.server_socket.bind(new InetSocketAddress(port));
	this.data = new PublisherPaths();
	this.parser = new Gson();
	this.executor = Executors.newCachedThreadPool();
    }

    public void run() {
	while(true) {
	    try {
		final Socket socket = this.server_socket.accept();
		this.executor.execute(new Runnable() {
			public void run() {
			    try {
				handle_client_connection(socket);
			    } catch (IOException e) {
				System.out.println(e.getStackTrace());
			    }
			}
		    });
	    } catch (IOException e) {
		System.out.println("MASTER IO ERROR - CLOSING" + e);
		return;
	    }
	}
    }

    private void handle_client_connection(Socket socket) throws IOException{
	BufferedReader from_client =
	    new BufferedReader(new InputStreamReader(socket.getInputStream()));
	DataOutputStream to_client =
	    new DataOutputStream(socket.getOutputStream());
	while (true) {
	    String client_data_raw = from_client.readLine();
	    if (client_data_raw == null) return; // client ended connection
	    MasterRequest client_message = request_from_string(client_data_raw);
	    if (client_message == null || (!validate(client_message))) {
		write(to_client, invalid_request());
		continue;
	    }
	    switch (client_message.type) {
	    case REGISTER_PUBLISHER:
		this.data.add(client_message.path, addr_from_msg(client_message));
		write(to_client, register_response());
		break;
	    case REMOVE_PUBLISHER:
		this.data.remove(client_message.path);
		write(to_client, remove_response());
		break;
	    case GET_PATHS_UNDER:
		write(to_client, paths_response(this.data.get_paths_under(client_message.path)));
		break;
	    case GET_PUBLISHER_OF_PATH:
		Path path = client_message.path;
		if (this.data.contains(path)) {
		    write(to_client,filled_query_response(this.data.get(path)));
		} else
		    write(to_client, empty_query_response());
		break;
	    }
	}
    }

    private synchronized MasterRequest request_from_string(String s) {
	try {
	    return parser.fromJson(s, MasterRequest.class);
	} catch (Exception e) {
	    return null;
	}
    }

    private boolean validate(MasterRequest msg) {
	switch (msg.type) {
	case REGISTER_PUBLISHER:
	    return msg.path.length() > 0
		&& msg.hostname.isPresent()
		&& msg.port.isPresent();
	case GET_PATHS_UNDER:
	case REMOVE_PUBLISHER:
	case GET_PUBLISHER_OF_PATH:
	    return msg.path.length() > 0
		&& (!msg.hostname.isPresent())
		&& (!msg.port.isPresent());
	default:
	    return false;
	}
    }

    private void write(DataOutputStream to_client, MasterResponse r) throws IOException {
	to_client.writeBytes(parser.toJson(r, MasterResponse.class) + "\n");
    }

    private InetSocketAddress addr_from_msg(MasterRequest msg) {
	return new InetSocketAddress(msg.hostname.get(), msg.port.get());
    }

    private MasterResponse paths_response(Set<String> paths) {
    return new MasterResponse(paths);
    }
    
    private MasterResponse register_response() {
	return new MasterResponse(MasterResponse.T.ACCEPT_UPDATE);
    }

    private MasterResponse remove_response() {
	return new MasterResponse(MasterResponse.T.ACCEPT_UPDATE);
    }

    private MasterResponse filled_query_response(InetSocketAddress addr) {
	return new MasterResponse(addr.getHostName(), addr.getPort());
    }

    private MasterResponse empty_query_response() {
	return new MasterResponse(MasterResponse.T.NO_PUBLISHER_FOR_PATH);
    }

    private MasterResponse invalid_request() {
	return new MasterResponse(MasterResponse.T.INVALID_REQUEST);
    }
}
