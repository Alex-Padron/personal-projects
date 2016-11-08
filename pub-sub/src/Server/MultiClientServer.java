package Server;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.google.gson.Gson;

import Messages.Serializable;

public abstract class MultiClientServer<Request extends Serializable, Response extends Serializable> implements Runnable {
    protected ServerSocket server_socket;
    protected Executor executor = Executors.newCachedThreadPool();
    protected SocketSet socket_set = new SocketSet();
    protected Gson parser = new Gson();
    protected String server_name;

    public MultiClientServer() {}

    public void run() {
	while(true) {
	    try {
		final Socket socket = this.server_socket.accept();
		this.executor.execute(new Runnable() {
			public void run() {
			    final int socket_id = socket_set.add(socket);
			    try {
				handle_client_connection(socket);
			    } catch (IOException e) {
				System.out.println(server_name + " CLIENT CONNECTION"
						   + " ERROR - CLOSING " + e);
				socket_set.remove(socket_id);
			    }
			}
		    });
	    } catch (IOException e) {
		System.out.println(server_name + " IO ERROR - CLOSING " + e);
		return;
	    }
	}
    }

    // close all active socket connections, and the server socket itself
    public void close() throws IOException {
	this.server_socket.close();
	for (Socket s : socket_set.to_close()) {
	    s.close();
	}
    }

    private void handle_client_connection(Socket socket) throws IOException {
	BufferedReader from_client =
	    new BufferedReader(new InputStreamReader(socket.getInputStream()));
	DataOutputStream to_client =
	    new DataOutputStream(socket.getOutputStream());
	while (true) {
	    String client_string = from_client.readLine();
	    Optional<Request> client_request =
		parse_client_string(client_string);
	    if (!client_request.isPresent()) {
		to_client.writeBytes(invalid_request().json() + "\n");
		continue;
	    }
	    Response response = handle_request(client_request.get());
	    to_client.writeBytes(response.json() + "\n");
	}
    }

    protected abstract Optional<Request> parse_client_string(String s);
    protected abstract Response invalid_request();
    protected abstract Response handle_request(Request req);
}
