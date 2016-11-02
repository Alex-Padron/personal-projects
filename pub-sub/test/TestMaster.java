import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Optional;

import org.junit.Test;

import com.google.gson.Gson;

import Master.Master;
import Messages.E;
import Messages.MasterMessage;

public class TestMaster {
	private final int port = 8080;
	
	@Test
	public void test() throws IOException {
		System.out.println("Testing Master...");
		Master m = new Master(port);
		Thread t = new Thread(m);
		t.start();
		Socket socket = new Socket("localhost", port);
		DataOutputStream to_server = new DataOutputStream(socket.getOutputStream());
		BufferedReader from_server = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		Gson parser = new Gson();
		
		MasterMessage msg;
		String s;
		
		msg = new MasterMessage(E.REGISTER, Optional.of("path1"), Optional.of("localhost"), Optional.of(1111));
		to_server.writeBytes(parser.toJson(msg, MasterMessage.class) + "\n");
		s = from_server.readLine();
		msg = parser.fromJson(s, MasterMessage.class);
		assert(msg.type == E.ACCEPTED_REGISTER);
		assert(msg.addr.equals(Optional.empty()));
		assert(msg.name.equals(Optional.empty()));
		
		msg = new MasterMessage(E.QUERY, Optional.of("path1"), Optional.empty(), Optional.empty());
		to_server.writeBytes(parser.toJson(msg, MasterMessage.class) + "\n");
		s = from_server.readLine();
		msg = parser.fromJson(s, MasterMessage.class);
		assert(msg.type == E.PUBLISHER_INFO);
		assert(msg.addr.get().equals("localhost"));
		assert(msg.port.get() == 1111);
		
		msg = new MasterMessage(E.QUERY, Optional.of("path2"), Optional.empty(), Optional.empty());
		to_server.writeBytes(parser.toJson(msg, MasterMessage.class) + "\n");
		s = from_server.readLine();
		msg = parser.fromJson(s, MasterMessage.class);
		assert(msg.type == E.NO_PUBLISHER);
		
		msg = new MasterMessage(E.REGISTER, Optional.of("path2"), Optional.of("localhost2"), Optional.of(2222));
		to_server.writeBytes(parser.toJson(msg, MasterMessage.class) + "\n");
		s = from_server.readLine();
		msg = parser.fromJson(s, MasterMessage.class);
		assert(msg.type == E.ACCEPTED_REGISTER);
		assert(msg.addr.equals(Optional.empty()));
		assert(msg.name.equals(Optional.empty()));
		
		msg = new MasterMessage(E.QUERY, Optional.of("path2"), Optional.empty(), Optional.empty());
		to_server.writeBytes(parser.toJson(msg, MasterMessage.class) + "\n");
		s = from_server.readLine();
		msg = parser.fromJson(s, MasterMessage.class);
		assert(msg.type == E.PUBLISHER_INFO);
		assert(msg.addr.get().equals("localhost2"));
		assert(msg.port.get() == 2222);
		
		msg = new MasterMessage(E.REGISTER, Optional.of("path1"), Optional.of("localhost2"), Optional.of(2222));
		to_server.writeBytes(parser.toJson(msg, MasterMessage.class) + "\n");
		s = from_server.readLine();
		msg = parser.fromJson(s, MasterMessage.class);
		assert(msg.type == E.ACCEPTED_REGISTER);
		assert(msg.addr.equals(Optional.empty()));
		assert(msg.name.equals(Optional.empty()));
		
		msg = new MasterMessage(E.QUERY, Optional.of("path1"), Optional.empty(), Optional.empty());
		to_server.writeBytes(parser.toJson(msg, MasterMessage.class) + "\n");
		s = from_server.readLine();
		msg = parser.fromJson(s, MasterMessage.class);
		assert(msg.type == E.PUBLISHER_INFO);
		assert(msg.addr.get().equals("localhost2"));
		assert(msg.port.get() == 2222);
		
		msg = new MasterMessage(E.REMOVE, Optional.of("path1"), Optional.empty(), Optional.empty());
		to_server.writeBytes(parser.toJson(msg, MasterMessage.class) + "\n");
		s = from_server.readLine();
		msg = parser.fromJson(s, MasterMessage.class);
		assert(msg.type == E.ACCEPTED_REMOVE);
		
		msg = new MasterMessage(E.QUERY, Optional.of("path1"), Optional.empty(), Optional.empty());
		to_server.writeBytes(parser.toJson(msg, MasterMessage.class) + "\n");
		s = from_server.readLine();
		msg = parser.fromJson(s, MasterMessage.class);
		assert(msg.type == E.NO_PUBLISHER);
		
		System.out.println("...passed");
		socket.close();
	}

}
