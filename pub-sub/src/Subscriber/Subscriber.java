package Subscriber;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Optional;
import java.net.InetSocketAddress;

import com.google.gson.Gson;

import Master.MasterClient;
import Messages.MessageTypes;
import Messages.PublisherMessage;

public class Subscriber {
    private final MasterClient MC;
    private final Hashtable<String, InetSocketAddress> publishers;
    private final Hashtable<InetSocketAddress, Socket> sockets;
    private final Gson parser;

    public Subscriber(String master_hostname, int master_port) throws UnknownHostException, IOException {
	this.MC = new MasterClient(master_hostname, master_port);
	this.publishers = new Hashtable<>();
	this.sockets = new Hashtable<>();
	this.parser = new Gson();
    }

    // Ask the master and cache the publisher for the path
    public boolean subscribe(String path_name) throws IOException {
	Optional<InetSocketAddress> addr = MC.get_path_addr(path_name);
	if (addr.isPresent()) {
	    publishers.put(path_name, addr.get());
	    return true;
	} else {
	    if (publishers.containsKey(path_name)) {
		publishers.remove(path_name);
	    }
	    return false;
	}
    }

    // get the value at the path, or empty if there is no
    // value for this path
    public Optional<Integer> get_value(String path_name) throws IOException {
	while (true) {
	    if (this.publishers.containsKey(path_name)) {
		InetSocketAddress publisher_addr = publishers.get(path_name);
		Optional<Integer> result = get_from_publisher(publisher_addr,
							      path_name);
		if (result.isPresent())
		    return result;
	    }
	    // if we don't know the publisher or the publisher does not
	    // have the data, resubscribe to the path from the master
	    if (!this.subscribe(path_name)) return Optional.empty();
	}
    }

    // close all active publisher connections
    public void close_connections() {
    }

    private Optional<Integer> get_from_publisher(InetSocketAddress addr,
						 String path) throws UnknownHostException, IOException
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
	PublisherMessage msg = new PublisherMessage(MessageTypes.GET_VALUE,
						    Optional.of(path),
						    Optional.empty());
	to_server.writeBytes(parser.toJson(msg, PublisherMessage.class) + "\n");
	msg = parser.fromJson(from_server.readLine(), PublisherMessage.class);
        return msg.value;
	}
}
