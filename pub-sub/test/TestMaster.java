import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import org.junit.Test;

import com.google.gson.Gson;

import Master.Master;
import Messages.MasterRequest;
import Messages.MasterResponse;

public class TestMaster {

	@Test
	public void test() throws IOException {
		System.out.println("Testing Master...");
		int port = 8080;
		Master m = new Master(port);
		Thread t = new Thread(m);
		t.start();
		Socket socket = new Socket("localhost", port);
		DataOutputStream to_server =
		    new DataOutputStream(socket.getOutputStream());
		BufferedReader from_server =
		    new BufferedReader(new InputStreamReader(socket.getInputStream()));
		Gson parser = new Gson();
		MasterRequest msg;
		MasterResponse res;
		String s;

		msg = new MasterRequest("path1", "localhost", 1111);
		
		to_server.writeBytes(parser.toJson(msg, MasterRequest.class) + "\n");
		s = from_server.readLine();
		res = parser.fromJson(s, MasterResponse.class);
		assert(res.type == MasterResponse.T.ACCEPT_UPDATE);
		assert(!res.hostname.isPresent());
		assert(!res.port.isPresent());

		msg = new MasterRequest(MasterRequest.T.GET_PUBLISHER_OF_PATH, "path1");
		to_server.writeBytes(parser.toJson(msg, MasterRequest.class) + "\n");
		s = from_server.readLine();
		res = parser.fromJson(s, MasterResponse.class);
		assert(res.type == MasterResponse.T.PUBLISHER_INFO);
		assert(res.hostname.get().equals("localhost"));
		assert(res.port.get() == 1111);

		msg = new MasterRequest(MasterRequest.T.GET_PUBLISHER_OF_PATH, "path2");
		to_server.writeBytes(parser.toJson(msg, MasterRequest.class) + "\n");
		s = from_server.readLine();
		res = parser.fromJson(s, MasterResponse.class);
		assert(res.type == MasterResponse.T.NO_PUBLISHER_FOR_PATH);

		msg = new MasterRequest("path2", "localhost2", 2222);

		to_server.writeBytes(parser.toJson(msg, MasterRequest.class) + "\n");
		s = from_server.readLine();
		res = parser.fromJson(s, MasterResponse.class);
		assert(res.type == MasterResponse.T.ACCEPT_UPDATE);
		assert(!res.hostname.isPresent());
		assert(!res.port.isPresent());

		msg = new MasterRequest(MasterRequest.T.GET_PUBLISHER_OF_PATH, "path2");
		to_server.writeBytes(parser.toJson(msg, MasterRequest.class) + "\n");
		s = from_server.readLine();
		res = parser.fromJson(s, MasterResponse.class);
		assert(res.type == MasterResponse.T.PUBLISHER_INFO);
		assert(res.hostname.get().equals("localhost2"));
		assert(res.port.get() == 2222);
		
		msg = new MasterRequest("path1", "localhost2", 2222);
		to_server.writeBytes(parser.toJson(msg, MasterRequest.class) + "\n");
		s = from_server.readLine();
		res = parser.fromJson(s, MasterResponse.class);
		assert(res.type == MasterResponse.T.ACCEPT_UPDATE);

		msg = new MasterRequest(MasterRequest.T.GET_PUBLISHER_OF_PATH, "path1");
		to_server.writeBytes(parser.toJson(msg, MasterRequest.class) + "\n");
		s = from_server.readLine();
		res = parser.fromJson(s, MasterResponse.class);
		assert(res.type == MasterResponse.T.PUBLISHER_INFO);
		assert(res.hostname.get().equals("localhost2"));
		assert(res.port.get() == 2222);

		msg = new MasterRequest(MasterRequest.T.REMOVE_PUBLISHER, "path1");
		to_server.writeBytes(parser.toJson(msg, MasterRequest.class) + "\n");
		s = from_server.readLine();
		res = parser.fromJson(s, MasterResponse.class);
		assert(res.type == MasterResponse.T.ACCEPT_UPDATE);

		msg = new MasterRequest(MasterRequest.T.GET_PUBLISHER_OF_PATH, "path1");
		to_server.writeBytes(parser.toJson(msg, MasterRequest.class) + "\n");
		s = from_server.readLine();
		res = parser.fromJson(s, MasterResponse.class);
		assert(res.type == MasterResponse.T.NO_PUBLISHER_FOR_PATH);
		assert(!res.hostname.isPresent());
		assert(!res.port.isPresent());

		socket.close();
		System.out.println("...passed");
	}

}
