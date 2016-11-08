package Public;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import DataStructures.Path;
import Messages.MasterRequest;
import Messages.MasterResponse;
import Messages.Serializable;
import Messages.Bodies.AddrBody;
import Messages.Bodies.PathSetBody;

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
     * Convenience wrapper to register an unlocked path, see doc below
     */
    public boolean register_path(Path path, String hostname, int port) throws IOException {
	return register_path(path, hostname, port, "");
    }

    /**
     * @param path: to register
     * @param hostname: of the publisher for the path
     * @param port: of the publisher for the path
     * @param lock_code: to use to lock the path, or "" to not lock
     */
    public boolean register_path(Path path,
				 String hostname,
				 int port,
				 String lock_code) throws IOException {
	MasterRequest request = new MasterRequest(path, hostname, port, lock_code);
	this.to_server.writeBytes(request.json() + "\n");
	String raw_response = from_server.readLine();
	Optional<MasterResponse> response =
	    Serializable.parse(raw_response, MasterResponse.class);
	return (response.isPresent()) && (response.get().validate()) &&
	    (response.get().type == MasterResponse.T.ACCEPT_UPDATE);
    }

    /**
     * Convenience wrapper to remove an unlocked path, see doc below
     */
    public boolean remove_path(Path path) throws IOException {
	return remove_path(path, "");
    }

    /**
     * @param path: to remove
     * @param lock_code: to lock the path with
     */
    public boolean remove_path(Path path, String lock_code) throws IOException {
	MasterRequest request = new MasterRequest(path, lock_code);
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
	    Serializable.parse_exn(response.get().body, AddrBody.class);
	return Optional.of(new InetSocketAddress(body.hostname, body.port));
    }

    /**
     * Gets all the possible extensions of a given path. If paths [a/b] and [a/c]
     * are being published, calling this method on path [a] will return [b] and [c]
     * as strings
     * @param path: to query on
     * @return: all possible extensions for this path
     */
    public Set<String> get_paths_under(Path path) throws IOException {
	MasterRequest request =
	    new MasterRequest(MasterRequest.T.GET_PATHS_UNDER, path);
	this.to_server.writeBytes(request.json() + "\n");
	String raw_response = from_server.readLine();
	Optional<MasterResponse> response =
	    Serializable.parse(raw_response, MasterResponse.class);
	if ((!response.isPresent()) ||
	    (!response.get().validate()) ||
	    (!response.get().type.equals(MasterResponse.T.PATH_SET)))
	    return new HashSet<>();
	PathSetBody body =
	    Serializable.parse_exn(response.get().body, PathSetBody.class);
	return body.paths;
    }
}
