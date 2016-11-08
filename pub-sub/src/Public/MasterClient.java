package Public;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Optional;

import DataStructures.Path;
import Messages.MasterRequest;
import Messages.MasterResponse;
import Messages.Serializable;
import Messages.Bodies.AddrBody;

/**
 * Simpler API for writing to/querying the master
 */
public class MasterClient {
    private final Socket socket;
    private final DataOutputStream to_server;
    private final BufferedReader from_server;

    /**
     * @param hostname: of the master
     * @param port: of the master
     */
    public MasterClient(String hostname, int port) throws UnknownHostException, IOException {
	this.socket = new Socket(hostname, port);
	this.to_server = new DataOutputStream(socket.getOutputStream());
	this.from_server =
	    new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * @param path: to register
     * @param hostname: of the publisher for the path
     * @param port: of the publisher for the path
     */
    public boolean register_path(Path path, String hostname, int port) throws IOException {
	MasterRequest request = new MasterRequest(path, hostname, port);
	this.to_server.writeBytes(request.json() + "\n");
	String raw_response = from_server.readLine();
	Optional<MasterResponse> response =
	    Serializable.parse(raw_response, MasterResponse.class);
	return (response.isPresent()) && (response.get().validate()) &&
	    (response.get().type == MasterResponse.T.ACCEPT_UPDATE);
    }

    /**
     * @param path: to remove
     */
    public boolean remove_path(Path path) throws IOException {
	MasterRequest request = new MasterRequest(MasterRequest.T.REMOVE_PUBLISHER, path);
	this.to_server.writeBytes(request.json() + "\n");
	String raw_response = from_server.readLine();
	Optional<MasterResponse> response =
	    Serializable.parse(raw_response, MasterResponse.class);
	return (response.isPresent()) && (response.get().validate()) &&
	    (response.get().type == MasterResponse.T.ACCEPT_UPDATE);
    }

    /**
       @param path: of the path to query
       @return addr if one is being published, otherwise empty
     */
    public Optional<InetSocketAddress> get_path_addr(Path path) throws IOException {
	MasterRequest request =
	    new MasterRequest(MasterRequest.T.GET_PUBLISHER_OF_PATH, path);
	this.to_server.writeBytes(request.json() + "\n");
	String raw_response = from_server.readLine();
	Optional<MasterResponse> response =
	    Serializable.parse(raw_response, MasterResponse.class);
	if ((!response.isPresent()) ||
	    (!response.get().validate()) ||
	    (!response.get().type.equals(MasterResponse.T.PUBLISHER_INFO)))
	    return Optional.empty();
	AddrBody body =
	    Serializable.parse(response.get().body, AddrBody.class).get();
	return Optional.of(new InetSocketAddress(body.hostname, body.port));
    }
}
