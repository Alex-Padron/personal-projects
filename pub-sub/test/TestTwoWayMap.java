import org.junit.Test;

import DataStructures.TwoWayMap;

public class TestTwoWayMap {

	@Test
	public void test() {
		System.out.println("Testing TwoWayMap...");
		TwoWayMap<String, Integer> t = new TwoWayMap<>();
		t.insert("foo", 1);
		t.insert("bar", 2);
		assert(t.get_k(1).equals("foo"));
		assert(t.get_v("foo") == 1);
		assert(t.get_k(2).equals("bar"));
		assert(t.get_v("bar") == 2);
		
		assert(t.contains_k("foo"));
		assert(!t.contains_k("baz"));
		assert(t.contains_v(1));
		assert(!t.contains_v(0));
		
		t.remove_k("foo");
		
		assert(!t.contains_v(1));
		
		t.remove_v(2);
		
		assert(!t.contains_k("bar"));
		
		System.out.println("...passed");
	}

}
