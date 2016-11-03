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

import Messages.MasterRequest;
import Messages.MasterResponse;

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
	MasterRequest request = new MasterRequest(path_name, hostname, port);
	this.to_server.writeBytes(parser.toJson(request, MasterRequest.class) + "\n");
	String raw_response = from_server.readLine();
	MasterResponse response = parser.fromJson(raw_response, MasterResponse.class);
	assert(response.type == MasterResponse.T.ACCEPT_UPDATE);
    }

    public void remove_path(String path_name) throws IOException {
	MasterRequest request = new MasterRequest(MasterRequest.T.REMOVE_PUBLISHER, path_name);
	this.to_server.writeBytes(parser.toJson(request, MasterRequest.class) + "\n");
	String raw_response = from_server.readLine();
	MasterResponse response = parser.fromJson(raw_response, MasterResponse.class);
	assert(response.type == MasterResponse.T.ACCEPT_UPDATE);
    }

    public Optional<InetSocketAddress> get_path_addr(String path_name) throws IOException {
	MasterRequest request = new MasterRequest(MasterRequest.T.GET_PUBLISHER_OF_PATH, path_name);
	this.to_server.writeBytes(parser.toJson(request, MasterRequest.class) + "\n");
	String raw_response = from_server.readLine();
	MasterResponse response = parser.fromJson(raw_response, MasterResponse.class);
	if (response.type == MasterResponse.T.PUBLISHER_INFO) {
	    InetSocketAddress addr =
		new InetSocketAddress(response.hostname.get(), response.port.get());
	    return Optional.of(addr);
	}
	return Optional.empty();
    }
}
