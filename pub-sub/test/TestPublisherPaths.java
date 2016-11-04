import java.net.InetSocketAddress;

import org.junit.Test;

import Master.Paths.Path;
import Master.Paths.PublisherPaths;

public class TestPublisherPaths {

	@Test
	public void test() throws Exception {
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
		System.out.println(s);

		for (int x = 9; x > 0; x--) {
			s = s.substring(0, s.length() - 2);
			h = new Path(s);
			p.remove(h);
			assert(!p.contains(h));
		}
	}

}
