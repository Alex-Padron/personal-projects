import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Optional;

import org.junit.Test;

import com.google.gson.Gson;

public class TestMaster {
	private final int port = 8080;

	@Test
	public void test() throws IOException {
		Master m = new Master(port);
		
		//thread to run master
		Thread t = new Thread(m);
		t.start();
		Socket socket = new Socket("localhost", port);
		DataOutputStream to_server = new DataOutputStream(socket.getOutputStream());
		BufferedReader from_server = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		Gson parser = new Gson();
		InetSocketAddress a = new InetSocketAddress("localhost", port);
		
		MasterMessage msg = new MasterMessage(Master.REGISTER, Optional.of("path1"), Optional.of("localhost"), Optional.of(1111));
		System.out.println("writing bytes to server " + msg.addr.get());
		to_server.writeBytes(parser.toJson(msg, MasterMessage.class));
		to_server.writeBytes("\n");
		System.out.println("Reading bytes from server");
		String s = from_server.readLine();
		System.out.println(s);
		msg = parser.fromJson(s, MasterMessage.class);
		System.out.println("performing assertions");
		assert(msg.type == Master.ACCEPTED_REGISTER);
		assert(msg.addr.equals(Optional.empty()));
		assert(msg.name.equals(Optional.empty()));
		System.out.println("passed test");
		socket.close();
	}

}
