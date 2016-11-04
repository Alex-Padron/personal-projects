import org.junit.Test;

import Master.Master;
import Master.MasterClient;
import Master.Paths.Path;

public class TestMasterClient {

    @Test
    public void test() throws Exception {
	System.out.println("Testing Master Client...");
	int port = 8090;
	Master m = new Master(port);
	Thread t = new Thread(m);
	t.start();

	MasterClient mc = new MasterClient("localhost", port);
	mc.register_path(new Path("path1"), "host1", 1);
	mc.register_path(new Path("path2"), "host2", 2);

	assert(mc.get_path_addr(new Path("path1")).get().getPort() == 1);
	assert(mc.get_path_addr(new Path("path1")).get().getHostName().equals("host1"));

	mc.register_path(new Path("path1"), "host1", 3);

	assert(mc.get_path_addr(new Path("path1")).get().getPort() == 3);
	assert(mc.get_path_addr(new Path("path1")).get().getHostName().equals("host1"));
	assert(mc.get_path_addr(new Path("path2")).get().getPort() == 2);
	assert(mc.get_path_addr(new Path("path2")).get().getHostName().equals("host2"));

	mc.remove_path(new Path("path2"));

	assert(!mc.get_path_addr(new Path("path2")).isPresent());

	mc.remove_path(new Path("path1"));

	assert(!mc.get_path_addr(new Path("path1")).isPresent());

	System.out.println("...passed");
    }
}
