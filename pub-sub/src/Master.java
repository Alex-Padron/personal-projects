import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Optional;

import com.google.gson.Gson;

public class Master implements Runnable {
	public static final int REGISTER = 0;
	public static final int QUERY = 1;
	public static final int NO_PUBLISHER = 2;
	public static final int PUBLISHER_INFO = 3;
	public static final int ACCEPTED_REGISTER = 4;
	public static final int INVALID_REQUEST = 5;
	
	private ServerSocket server_socket;
	
	private Hashtable<String, InetSocketAddress> name_to_server = new Hashtable<>();
	
	public Master(int port) throws IOException {
		System.out.println("Binding to port " + port);
		this.server_socket = new ServerSocket();
		this.server_socket.setReuseAddress(true);
		this.server_socket.bind(new InetSocketAddress(port));
	}
	
	public void run() {
		System.out.println("running server");
		Gson parser = new Gson();
		while(true) {
			try {
				Socket socket = this.server_socket.accept();
				System.out.println("accepted client connection");
				BufferedReader from_client = 
						new BufferedReader(new InputStreamReader(socket.getInputStream()));
				DataOutputStream to_client = new DataOutputStream(socket.getOutputStream());
				String client_data = from_client.readLine();
				MasterMessage client_message = parser.fromJson(client_data, MasterMessage.class);
				System.out.println("got message from client with addr " + client_message.addr + " port " + client_message.port);
				switch(client_message.type) {
				case REGISTER:
					this.name_to_server.put(client_message.name.get(), new InetSocketAddress(client_message.addr.get(), client_message.port.get()));
					System.out.println("writing ack to client");
					to_client.writeBytes(parser.toJson(new MasterMessage(ACCEPTED_REGISTER, Optional.empty(), Optional.empty(), Optional.empty()), MasterMessage.class) + "\n");
					break;
				case QUERY:
					String name = client_message.name.get();
					if (name_to_server.containsKey(name)) {
						to_client.writeBytes(parser.toJson(new MasterMessage(PUBLISHER_INFO, Optional.empty(), Optional.of(name_to_server.get(name).getHostName()), Optional.of(name_to_server.get(name).getPort())), MasterMessage.class));
					} else {
						to_client.writeBytes(parser.toJson(new MasterMessage(NO_PUBLISHER, Optional.empty(), Optional.empty(), Optional.empty()), MasterMessage.class) + "\n");
					}
					break;
				default:
					throw new IOException("Unrecognized Message Type " + client_message.type);
				}
			} catch(Exception e) {
				System.out.println("Unable to get valid message for client ");
				e.printStackTrace();
				try {
					this.server_socket.close();
				} catch (IOException e1) {
					System.out.println("failed to close socket");
				}
				break;
			}
		}
	}
}
