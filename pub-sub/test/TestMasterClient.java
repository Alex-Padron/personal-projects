import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import DataStructures.Path;
import Public.Master;
import Public.MasterClient;


public class TestMasterClient {

	private void add_paths(MasterClient mc, Path root, Set<String> extensions) throws IOException, Exception {
		for (String ext : extensions) {
			mc.register_path(root.append(ext), "localhost", 1111);
		}	
	}
	
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
	
	Path p = new Path("a");
	Set<String> s = new HashSet<>();
	s.add("b");
	s.add("c");
	s.add("d");
	add_paths(mc, p, s);
	s = mc.get_paths_under(p);
	assert(s.size() == 3);
	assert(s.contains("b"));
	assert(s.contains("c"));
	assert(s.contains("c"));
	
	s = mc.get_paths_under(new Path("path1"));
	assert(s.size() == 0);
	
	System.out.println("...passed");
    }
}
