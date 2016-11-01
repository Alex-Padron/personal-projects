import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;

public class Master implements Runnable {
    public static final int REGISTER = 0;
    public static final int QUERY = 1;
    public static final int NO_PUBLISHER = 2;
    public static final int PUBLISHER_INFO = 3;
    public static final int ACCEPTED_REGISTER = 4;
    public static final int INVALID_REQUEST = 5;

    private ServerSocket server_socket;
    private MasterData data;
    private Gson parser;
    private ExecutorService executor;

    public Master(int port) throws IOException {
	System.out.println("MASTER ON PORT:" + port);
	this.server_socket = new ServerSocket();
	this.server_socket.setReuseAddress(true);
	this.server_socket.bind(new InetSocketAddress(port));
	this.data = new MasterData();
	this.parser = new Gson();
	this.executor = Executors.newCachedThreadPool();
    }

    public void run() {
	System.out.println("RUNNING MASTER SERVER");
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
	    } catch (IOException e) {}
	}
    }

    private void handle_client_connection(Socket socket) throws IOException{
	System.out.println("NEW CLIENT: " + socket);
	BufferedReader from_client =
	    new BufferedReader(new InputStreamReader(socket.getInputStream()));
	DataOutputStream to_client =
	    new DataOutputStream(socket.getOutputStream());
	while (true) {
	    String client_data_raw = from_client.readLine();
	    if (client_data_raw == null) return;
	    MasterMessage client_message =
		this.parser.fromJson(client_data_raw, MasterMessage.class);
	    switch (client_message.type) {
	    case REGISTER:
		this.data.add_client(client_message.name.get(),
				     addr_from_msg(client_message));
		to_client.writeBytes(parser.toJson(register_response()));
		break;
	    case QUERY:
		String name = client_message.name.get();
		if (this.data.contains(name)) {
		    to_client.writeBytes(
			      parser.toJson(
			      filled_query_response(
			      this.data.get_client(name))));
		} else {
		    to_client.writeBytes(parser.toJson(empty_query_response()));
		}
		break;
	    }
	    to_client.writeBytes("\n");
	}
    }

    private InetSocketAddress addr_from_msg(MasterMessage msg) {
	return new InetSocketAddress(msg.addr.get(), msg.port.get());
    }

    private MasterMessage register_response() {
	return new MasterMessage(ACCEPTED_REGISTER, Optional.empty(),
				 Optional.empty(), Optional.empty());
    }

    private MasterMessage filled_query_response(InetSocketAddress addr) {
	return new MasterMessage(PUBLISHER_INFO, Optional.empty(),
				 Optional.of(addr.getHostName()),
				 Optional.of(addr.getPort()));
    }

    private MasterMessage empty_query_response() {
	return new MasterMessage(NO_PUBLISHER, Optional.empty(),
				 Optional.empty(), Optional.empty());
    }
}
