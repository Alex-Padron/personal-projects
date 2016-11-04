package Subscriber;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Optional;
import java.net.InetSocketAddress;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import Master.MasterClient;
import Master.Paths.Path;
import Messages.PublisherRequest;
import Messages.PublisherResponse;

/**
 * Subscriber for data type T. Should only be used to connect to publishers
 * that are also publishing type T.
 */
public class Subscriber<T> {
    private final MasterClient MC;
    private final Hashtable<Path, InetSocketAddress> publishers;
    private final Hashtable<InetSocketAddress, Socket> sockets;
    private final Gson parser;
    private final Type msg_type;

    /*
      @param master_hostname, master_port: location of the master
     */
    public Subscriber(String master_hostname, int master_port) throws UnknownHostException, IOException {
	this.MC = new MasterClient(master_hostname, master_port);
	this.publishers = new Hashtable<>();
	this.sockets = new Hashtable<>();
	this.parser = new Gson();
	this.msg_type = new TypeToken<PublisherResponse<Integer>>(){}.getType();
    }

    /*
     * Ask the master about the publisher for a given path, and cache the
     * result if there is a publisher.
     * @param path: to query
     * @return whether the subscriber found a publisher for the given path
     */
    public boolean subscribe(Path path) throws IOException {
	Optional<InetSocketAddress> addr = MC.get_path_addr(path);
	if (addr.isPresent()) {
	    publishers.put(path, addr.get());
	    return true;
	} else {
	    if (publishers.containsKey(path)) {
		publishers.remove(path);
	    }
	    return false;
	}
    }

    /**
     * get the value for the specified path
     * @param path: to query
     * @return the value or empty if no-one is publishing that value
     */
    public Optional<T> get_value(Path path) throws IOException {
	while (true) {
	    if (this.publishers.containsKey(path)) {
		InetSocketAddress publisher_addr = publishers.get(path);
		Optional<T> result = get_from_publisher(publisher_addr,
							      path);
		if (result.isPresent())
		    return result;
	    }
	    // if we don't know the publisher or the publisher does not
	    // have the data, resubscribe to the path from the master
	    if (!this.subscribe(path)) return Optional.empty();
	}
    }

    // close all active publisher connections
    public void close_connections() {
    	// TODO
    }

    private Optional<T> get_from_publisher(InetSocketAddress addr,
						 Path path) throws UnknownHostException, IOException
    {
	Socket socket;
	if (sockets.containsKey(addr)) socket = sockets.get(addr);
	else {
	    socket = new Socket(addr.getHostName(), addr.getPort());
	    sockets.put(addr, socket);
	}
	DataOutputStream to_server = new DataOutputStream(
				     socket.getOutputStream());
	BufferedReader from_server = new BufferedReader(
				     new InputStreamReader(
				     socket.getInputStream()));
	PublisherRequest msg =
	    new PublisherRequest(PublisherRequest.T.GET_PATH_VALUE, path);
	to_server.writeBytes(parser.toJson(msg, PublisherRequest.class) + "\n");
	PublisherResponse<T> response =
	    parser.fromJson(from_server.readLine(), msg_type);
        return response.value;
	}
}
