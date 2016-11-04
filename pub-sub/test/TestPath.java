import java.util.Hashtable;

import org.junit.Test;

import Master.Paths.Path;

public class TestPath {

	public void create_throws(String s) {
		try {
			Path p = new Path(s);
			System.out.println("created path" + p);
			assert(false);
		} catch (Exception e) {}
	}
	
	@Test
	public void test() throws Exception {
		System.out.println("Testing Path...");
		
		create_throws("@#$$dadasdasd");
		create_throws("//hello");
		create_throws("hello/");
		
		Path p = new Path("hello");
		String[] comp = p.get_components();
		assert(comp[0].equals("hello"));
		
		p = new Path("hello/world");
		comp = p.get_components();
		assert(comp[0].equals("hello"));
		assert(comp[1].equals("world"));
		assert(p.toString().equals("hello/world"));
		
		Path p2 = new Path("foo/bar");
		Path p3 = p.append(p2);
		assert(p3.toString().equals("hello/world/foo/bar"));
		
		comp = p3.get_components();
		assert(comp[0].equals("hello"));
		assert(comp[1].equals("world"));
		assert(comp[2].equals("foo"));
		assert(comp[3].equals("bar"));
		
		Path p4 = p.append("foo");
		assert(p4.toString().equals("hello/world/foo"));
		assert(p4.get_suffix().equals("foo"));
		assert(p3.get_suffix().equals("bar"));
		
		comp = p3.get_prefix();
		assert(comp[0].equals("hello"));
		assert(comp[1].equals("world"));
		assert(comp[2].equals("foo"));
		assert(comp.length == 3);
		
		assert(p3.length() == 4);
		assert(p4.length() == 3);
		assert(p2.length() == 2);
		assert(p.length() == 2);
		
		Path p5 = new Path("hello/world");
		System.out.println(p);
		Hashtable<Path, Integer> h = new Hashtable<>();
		
		assert(p5.equals(p));
		assert(!p5.equals(p4));
		
		h.put(p, 1);
		assert(h.containsKey(p5));
		
		System.out.println("...passed");
	}

}
