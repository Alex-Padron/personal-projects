import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import Master.Master;
import Publisher.Publisher;
import Subscriber.Subscriber;

public class TestSubscriber {

	@Test
	public void test() throws IOException {
		int port = 8080;
		String master_hostname = "localhost";
		int master_port = 8081;
		Map<String, Integer> paths = new HashMap<>();
		paths.put("path1", 1);
		paths.put("path2", 2);
		
		Master ms = new Master(master_port);
		Thread t1 = new Thread(ms);
		t1.start();
		
		Publisher p = new Publisher(port, master_hostname, master_port, paths);
		Thread t2 = new Thread(p);
		t2.start();
		p.send_paths_to_master();
		
		System.out.println("Testing Subscriber...");
		Subscriber s = new Subscriber(master_hostname, master_port);
		assert(s.subscribe("path1"));
		assert(!s.subscribe("path3"));
		
		assert(s.get_value("path2").get() == 2);
		assert(s.get_value("path1").get() == 1);
		assert(!s.get_value("path3").isPresent());
		System.out.println("...passed");
	}

}
