import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import DataStructures.Path;
import Public.Master;
import Public.Publisher;
import Public.Subscriber;

public class TestSubscriber {

    @Test
    public void test() throws Exception {
	int port = 8200;
	String master_hostname = "localhost";
	int master_port = 8201;
	Map<Path, String> paths = new HashMap<>();
	paths.put(new Path("path1"), "a");
	paths.put(new Path("path2"), "b");

	Master ms = new Master(master_port);
	Thread t1 = new Thread(ms);
	t1.start();

	Publisher<String> p =
	    new Publisher<>(port, master_hostname, master_port, paths);
	Thread t2 = new Thread(p);
	t2.start();
	p.send_paths_to_master();

	System.out.println("Testing Subscriber...");
	Subscriber<String> s =
	    new Subscriber<>(master_hostname, master_port);

	assert(s.subscribe(new Path("path1")));
	assert(!s.subscribe(new Path("path3")));
	assert(s.get_value(new Path("path2")).get().equals("b"));
	assert(s.get_value(new Path("path1")).get().equals("a"));
	assert(!s.get_value(new Path("path3")).isPresent());

	System.out.println("...passed");
    }
}
