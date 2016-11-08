import java.net.InetSocketAddress;
import java.util.Hashtable;
import java.util.Random;
import java.util.Set;

import org.junit.Test;

import DataStructures.Path;
import DataStructures.publisherData.PublisherPaths;

public class TestPublisherPaths {
    PublisherPaths p;

    boolean insert(String path_name, int port) throws Exception {
    	return insert(path_name, port, "");
    }
    
    boolean insert(String path_name, int port, String lock_code) throws Exception {
	InetSocketAddress i = new InetSocketAddress("localhost", port);
	return p.add(new Path(path_name), i, lock_code);
    }

    void check(String path_name, int expected_port) throws Exception {
	assert(p.get(new Path(path_name)).getPort() == expected_port);
    }

    int get(String path_name) throws Exception {
	return p.get(new Path(path_name)).getPort();
    }

    boolean contains(String path_name) throws Exception {
	return p.contains(new Path(path_name));
    }

    boolean remove(String path_name) throws Exception {
    	return remove(path_name, "");
    }
    
    boolean remove(String path_name, String lock_code) throws Exception {
	return p.remove(new Path(path_name), lock_code);
    }

    @Test
    public void test() throws Exception {
	System.out.println("Testing PublisherPaths...");
	p = new PublisherPaths();

	// basic sanity checks
	insert("path1", 1111);
	check("path1", 1111);
	assert(contains("path1"));
	remove("path1");
	assert(!contains("path1"));

	// nested paths
	String s = "a";
	for (int x = 0; x < 10; x++) {
		s += "/" + x;
	    insert(s, x);
	    check(s, x);
	}
	for (int x = 9; x > 0; x--) {
	    s = s.substring(0, s.length() - 2);
	    remove(s);
	    assert(!contains(s));
	}

	System.out.println("...passed");
    }

    @Test
    public void test2() throws Exception {
	System.out.println("Testing PublisherPaths nested...");
	p = new PublisherPaths();
	insert("a/b", 1111);
	insert("a/c", 1111);
	insert("a/d", 1111);
	Set<String> s = p.get_paths_under(new Path("a"));
	assert(s.size() == 3);
	assert(s.contains("b"));
	assert(s.contains("c"));
	assert(s.contains("d"));
	System.out.println("...passed");
    }

    @Test
    public void test3() throws Exception {
	System.out.println("Test PublisherPaths randomized...");
	p = new PublisherPaths();
	Hashtable<String, Integer> mirror = new Hashtable<>();
	Hashtable<String, Integer> all_inserts = new Hashtable<>();
	Random r = new Random();
	for (int i = 0; i < 10000; i++) {
	    int length = r.nextInt() % 10;
	    String s = Integer.toString(r.nextInt(Integer.SIZE - 1) % 10);
	    for (int j = 0; j < length + 1; j++) {
		s += "/" + r.nextInt(Integer.SIZE - 1);
	    }
	    if (r.nextInt() % 2 == 0) {
		int to_insert = (r.nextInt(Integer.SIZE - 1) % 2000) + 1000;
		insert(s, to_insert);
		mirror.put(s, to_insert);
		all_inserts.put(s, to_insert);
	    } else {
		remove(s);
		if (mirror.containsKey(s)) mirror.remove(s);
	    }
	}
	for (String key : all_inserts.keySet()) {
	    if (mirror.containsKey(key)) {
	        assert(mirror.get(key) == get(key));
	    } else {
		assert(!contains(key));
	    }
	}
	System.out.println("...passed");
    }
    
    @Test 
    public void test4() throws Exception {
    	System.out.println("Test Locking Paths...");
    	p = new PublisherPaths();
    	assert(insert("a/b", 1, "locked"));
    	assert(insert("a/b/c", 1, ""));
    	assert(get("a/b") == 1);
    	assert(!remove("a/b", ""));
    	assert(!insert("a/b", 1, "asdadad"));
    	assert(insert("a/b", 2, "locked"));
    	assert(get("a/b") == 2);
    	assert(remove("a/b", "locked"));
    	assert(!contains("a/b"));
    	System.out.println("...passed");
    }
}


























