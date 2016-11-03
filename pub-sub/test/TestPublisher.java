import org.junit.Test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import Master.Master;
import Master.MasterClient;
import Messages.MessageTypes;
import Messages.PublisherMessage;
import Publisher.Publisher;
import java.lang.reflect.Type;

public class TestPublisher {

	@Test
	public void test() throws IOException {
		System.out.println("Testing Publisher...");
		Gson parser = new Gson();
		Type msg_type = new TypeToken<PublisherMessage<Integer>>(){}.getType();
		PublisherMessage<Integer> m;
		String s;
		
		int port = 8080;
		String master_hostname = "localhost";
		int master_port = 8081;
		Map<String, Integer> paths = new HashMap<>();
		paths.put("path1", 1);
		paths.put("path2", 2);
		
		Master ms = new Master(master_port);
		Thread t1 = new Thread(ms);
		t1.start();
		
		Publisher<Integer> p = new Publisher<>(port, master_hostname, master_port, paths);
		System.out.println("Without Master...");
		Thread t2 = new Thread(p);
		t2.start();
		
		
		Socket socket = new Socket("localhost", port);
		DataOutputStream to_server = new DataOutputStream(socket.getOutputStream());
		BufferedReader from_server = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		m = new PublisherMessage<Integer>(MessageTypes.GET_VALUE, Optional.of("path1"), Optional.empty());
		to_server.writeBytes(parser.toJson(m, PublisherMessage.class) + "\n");
		s = from_server.readLine();
		m = parser.fromJson(s, msg_type);
		assert(m.type == MessageTypes.VALUE_RESPONSE);
		assert(m.value.get() == 1);
		
		p.put_path("path3", 3);
		
		m = new PublisherMessage<>(MessageTypes.GET_VALUE, Optional.of("path3"), Optional.empty());
		to_server.writeBytes(parser.toJson(m, PublisherMessage.class) + "\n");
		s = from_server.readLine();
		m = parser.fromJson(s, msg_type);
		assert(m.type == MessageTypes.VALUE_RESPONSE);
		assert(m.value.get() == 3);
		
		m = new PublisherMessage<>(MessageTypes.GET_VALUE, Optional.of("path4"), Optional.empty());
		to_server.writeBytes(parser.toJson(m, PublisherMessage.class) + "\n");
		s = from_server.readLine();
		m = parser.fromJson(s, msg_type);
		assert(m.type == MessageTypes.NOT_PUBLISHING);

		m = new PublisherMessage<>(MessageTypes.GET_VALUE, Optional.of("path2"), Optional.empty());
		to_server.writeBytes(parser.toJson(m, PublisherMessage.class) + "\n");
		s = from_server.readLine();
		m = parser.fromJson(s, msg_type);
		assert(m.type == MessageTypes.VALUE_RESPONSE);
		assert(m.value.get() == 2);
		
		p.remove_path("path2");
		
		m = new PublisherMessage<>(MessageTypes.GET_VALUE, Optional.of("path2"), Optional.empty());
		to_server.writeBytes(parser.toJson(m, PublisherMessage.class) + "\n");
		s = from_server.readLine();
		m = parser.fromJson(s, msg_type);
		assert(m.type == MessageTypes.NOT_PUBLISHING);
		
		m = new PublisherMessage<>(MessageTypes.QUERY_PATH, Optional.of("path1"), Optional.empty());
		to_server.writeBytes(parser.toJson(m, PublisherMessage.class) + "\n");
		s = from_server.readLine();
		m = parser.fromJson(s, msg_type);
		assert(m.type == MessageTypes.PUBLISHING_PATH);
		
		m = new PublisherMessage<>(MessageTypes.QUERY_PATH, Optional.of("path2"), Optional.empty());
		to_server.writeBytes(parser.toJson(m, PublisherMessage.class) + "\n");
		s = from_server.readLine();
		m = parser.fromJson(s, msg_type);
		assert(m.type == MessageTypes.NOT_PUBLISHING);
		
		System.out.println("With Master...");
		
		MasterClient mc = new MasterClient("localhost", master_port);
		p.send_paths_to_master();
		assert(mc.get_path_addr("path1").get().getPort() == 8080);
		assert(!mc.get_path_addr("path2").isPresent());
		assert(mc.get_path_addr("path3").get().getPort() == 8080);
		
		p.put_path("path2", 1);
		p.send_paths_to_master();
			
		assert(mc.get_path_addr("path2").get().getPort() == 8080);
		
		p.remove_path("path3");
		p.send_paths_to_master();

		assert(!mc.get_path_addr("path3").isPresent());
		socket.close();
		System.out.println("...passed");
	}

}
