import java.io.IOException;

import org.junit.Test;

import Master.Master;
import Master.MasterClient;

public class TestMasterClient {

	@Test
	public void test() throws IOException {
		System.out.println("Testing Master Client...");
		int port = 8080;
		Master m = new Master(port);
		Thread t = new Thread(m);
		t.start();

		MasterClient mc = new MasterClient("localhost", port);
		mc.register_path("path1", "host1", 1);
		mc.register_path("path2", "host2", 2);

		assert(mc.get_path_addr("path1").get().getPort() == 1);
		assert(mc.get_path_addr("path1").get().getHostName().equals("host1"));

		mc.register_path("path1", "host1", 3);

		assert(mc.get_path_addr("path1").get().getPort() == 3);
		assert(mc.get_path_addr("path1").get().getHostName().equals("host1"));
		assert(mc.get_path_addr("path2").get().getPort() == 2);
		assert(mc.get_path_addr("path2").get().getHostName().equals("host2"));

		mc.remove_path("path2");

		assert(!mc.get_path_addr("path2").isPresent());

		mc.remove_path("path1");

		assert(!mc.get_path_addr("path1").isPresent());

		System.out.println("...passed");
	}

}
