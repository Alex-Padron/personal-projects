import java.net.InetSocketAddress;
import java.util.Set;

import org.junit.Test;

import Master.Paths.Path;
import Master.Paths.PublisherPaths;

public class TestPublisherPaths {

	@Test
	public void test() throws Exception {
		System.out.println("Testing PublisherPaths...");
		PublisherPaths p = new PublisherPaths();
		Path h;
		InetSocketAddress i;
		
		i = new InetSocketAddress("localhost", 1111);
		h = new Path("foo/bar");
		p.add(h, i);
		
		assert(p.get(h).equals(i));
		assert(p.contains(h));
		
		p.remove(h);
		
		assert(!p.contains(h));
		
		i = new InetSocketAddress("localhost", 1111);
		p.add(h, i);
		
		assert(p.get(h).equals(i));
		
		String s = "a";
		for (int x = 0; x < 10; x++) {
			s += "/" + x;
			h = new Path(s);
			i = new InetSocketAddress("localhost", 1000 + x);
			p.add(h, i);
			assert(p.get(h).equals(i));
		}

		for (int x = 9; x > 0; x--) {
			s = s.substring(0, s.length() - 2);
			h = new Path(s);
			p.remove(h);
			assert(!p.contains(h));
		}
		System.out.println("...passed");
	}
	
	void insert(PublisherPaths p, String path_name, InetSocketAddress i) throws Exception {
		p.add(new Path(path_name), i);
	}
	
	@Test
	public void test2() throws Exception {
		System.out.println("Testing PublisherPaths nested...");
		PublisherPaths p = new PublisherPaths();
		InetSocketAddress i = new InetSocketAddress("localhost", 1111);
		insert(p, "a/b", i);
		insert(p, "a/c", i);
		insert(p, "a/d", i);
		Set<String> s = p.get_paths_under(new Path("a"));
		assert(s.size() == 3);
		assert(s.contains("b"));
		assert(s.contains("c"));
		assert(s.contains("d"));
		System.out.println("...passed");
		
	}

}
