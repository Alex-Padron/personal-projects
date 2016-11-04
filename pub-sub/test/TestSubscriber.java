import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import Master.Master;
import Master.Paths.Path;
import Publisher.Publisher;
import Subscriber.Subscriber;

public class TestSubscriber {

    @Test
    public void test() throws Exception {
	int port = 8200;
	String master_hostname = "localhost";
	int master_port = 8201;
	Map<Path, Integer> paths = new HashMap<>();
	paths.put(new Path("path1"), 1);
	paths.put(new Path("path2"), 2);

	Master ms = new Master(master_port);
	Thread t1 = new Thread(ms);
	t1.start();

	Publisher<Integer> p =
	    new Publisher<>(port, master_hostname, master_port, paths);
	Thread t2 = new Thread(p);
	t2.start();
	p.send_paths_to_master();

	System.out.println("Testing Subscriber...");
	Subscriber<Integer> s =
	    new Subscriber<>(master_hostname, master_port);

	assert(s.subscribe(new Path("path1")));
	assert(!s.subscribe(new Path("path3")));
	assert(s.get_value(new Path("path2")).get() == 2);
	assert(s.get_value(new Path("path1")).get() == 1);
	assert(!s.get_value(new Path("path3")).isPresent());

	System.out.println("...passed");
    }
}
