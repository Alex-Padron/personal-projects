package Master;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Optional;

import com.google.gson.Gson;

import Messages.E;
import Messages.MasterMessage;

public class MasterClient {
	private final Socket socket;
    private final DataOutputStream to_server;
    private final BufferedReader from_server;
    private final Gson parser;

    public MasterClient(String hostname, int port) throws UnknownHostException, IOException {
	this.socket = new Socket(hostname, port);
	this.to_server = new DataOutputStream(socket.getOutputStream());
	this.from_server =
	    new BufferedReader(new InputStreamReader(socket.getInputStream()));
	this.parser = new Gson();
    }

    public void register_path(String path_name, String hostname, int port) throws IOException {
	MasterMessage msg = new MasterMessage(E.REGISTER,
					      Optional.of(path_name),
					      Optional.of(hostname),
					      Optional.of(port));
	this.to_server.writeBytes(parser.toJson(msg, MasterMessage.class) + "\n");
	String response = from_server.readLine();
	msg = parser.fromJson(response, MasterMessage.class);
	assert(msg.type == E.ACCEPTED_REGISTER);
    }

    public void remove_path(String path_name) throws IOException {
	MasterMessage msg = new MasterMessage(E.REMOVE,
					      Optional.of(path_name),
					      Optional.empty(),
					      Optional.empty());
	this.to_server.writeBytes(parser.toJson(msg, MasterMessage.class) + "\n");
	String response = from_server.readLine();
	msg = parser.fromJson(response, MasterMessage.class);
	assert(msg.type == E.ACCEPTED_REMOVE);
    }

    public Optional<InetSocketAddress> get_path_addr(String path_name) throws IOException {
	MasterMessage msg = new MasterMessage(E.QUERY,
					      Optional.of(path_name),
					      Optional.empty(),
					      Optional.empty());
	this.to_server.writeBytes(parser.toJson(msg, MasterMessage.class) + "\n");
	String response = from_server.readLine();
	msg = parser.fromJson(response, MasterMessage.class);
	if (msg.type == E.PUBLISHER_INFO) {
	    InetSocketAddress addr =
		new InetSocketAddress(msg.addr.get(), msg.port.get());
	    return Optional.of(addr);
	}
	return Optional.empty();
    }
}
