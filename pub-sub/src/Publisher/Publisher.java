package Publisher;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.google.gson.Gson;

import Master.MasterClient;
import Messages.PublisherRequest;
import Messages.PublisherResponse;

/**
 * Publisher of type T
 */
public class Publisher<T> implements Runnable {
    private ExecutorService executor;
    private Lock lock;
    private ServerSocket server_socket;
    private MasterClient m_client;
    private Map<String, T> path_data;
    private Set<String> to_remove;
    private int port;
    private String hostname;
    private Gson parser;

    /**
     * @param port: for the publisher to bind on
     * @param master_hostname, master_port: location of the master
     * @param path_data: initial set of paths/values that the publisher
     * is publishing
     */
    public Publisher(int port, String master_hostname, int master_port,
		     Map<String, T> path_data) throws IOException
    {
	this.port = port;
	this.hostname = "localhost";
	this.executor = Executors.newCachedThreadPool();
	this.lock = new ReentrantLock();
	this.server_socket = new ServerSocket(port);
	this.server_socket.setReuseAddress(true);
	this.m_client = new MasterClient(master_hostname, master_port);
	this.path_data = path_data;
	this.to_remove = new HashSet<>();
	this.parser = new Gson();
    }

    /**
     * @param path_name: name of the path this is publishing
     * @param value: new value for that path
     */
    public void put_path(String path_name, T value) {
	this.lock.lock();
	this.path_data.put(path_name, value);
	this.lock.unlock();
    }

    /**
     * @param path_name: of the path to remove
     */
    public void remove_path(String path_name) {
	this.lock.lock();
	this.path_data.remove(path_name);
	this.to_remove.add(path_name);
	this.lock.unlock();
    }

    /**
     * Update the master with all of the paths that the publisher
     * is currently publishing, and removes all paths the publisher
     * is no longer publishing
     */
    public void send_paths_to_master() throws IOException {
	this.lock.lock();
	for (String path_name : this.to_remove) {
	    this.m_client.remove_path(path_name);
	}
	for (String path_name : this.path_data.keySet()) {
	    this.m_client.register_path(path_name,
					this.hostname,
					this.port);
	}
	this.lock.unlock();
    }

    public void run() {
	while (true) {
	    Socket socket;
	    try {
		socket = this.server_socket.accept();
		executor.execute(new Runnable() {
			public void run() {
			    try {
				handle_client(socket);
			    } catch (IOException e) {}
			}
		    });
	    } catch (IOException e) {}
	}
    }

    private void handle_client(Socket socket) throws IOException {
	System.out.println("NEW CLIENT: " + socket);
	BufferedReader from_client =
	    new BufferedReader(new InputStreamReader(socket.getInputStream()));
	DataOutputStream to_client =
	    new DataOutputStream(socket.getOutputStream());
	while (true) {
	    String client_data_raw = from_client.readLine();
	    if (client_data_raw == null) return;
	    PublisherRequest client_message =
		this.parser.fromJson(client_data_raw, PublisherRequest.class);
	    this.lock.lock();
	    if (!path_data.containsKey(client_message.path))
		to_client.writeBytes(parser.toJson(not_publishing_response()));
	    else {
		switch (client_message.type) {
		case QUERY_PUBLISHING_PATH:
		    to_client.writeBytes(parser.toJson(publishing_response()));
		    break;
		case GET_PATH_VALUE:
		    to_client.writeBytes(parser.toJson(
					 data_response(
					 path_data.get(
					 client_message.path))));
		    break;
		default:
			break;
		}
	    }
	    this.lock.unlock();
	    to_client.writeBytes("\n");
	}
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
