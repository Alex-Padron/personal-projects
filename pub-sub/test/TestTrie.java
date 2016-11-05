import java.util.Set;

import org.junit.Test;

import DataStructures.Path;
import DataStructures.Trie;

public class TestTrie {

    @Test
    public void test() throws Exception {
	System.out.println("Testing Trie...");
	Trie<Integer> t = new Trie<>();
	Path p = new Path("foo");
	t.insert(p, 1);

	assert(t.head.children.get("foo").is_terminal);
	assert(t.head.children.get("foo").value == 1);

	p = new Path("baz");
	t.insert(p, 2);

	assert(t.head.children.get("baz").is_terminal);
	assert(t.head.children.get("baz").value == 2);

	p = new Path("foo/bar");
	t.insert(p, 3);

	assert(t.head.children.get("foo").is_terminal);
	assert(t.head.children.get("foo").value == 1);
	assert(t.head.children.get("foo").children.get("bar").is_terminal);
	assert(t.head.children.get("foo").children.get("bar").value == 3);
	assert(t.head.children.get("foo").children.size() == 1);

	t.insert(p, 4);

	assert(t.head.children.get("foo").is_terminal);
	assert(t.head.children.get("foo").value == 1);
	assert(t.head.children.get("foo").children.get("bar").is_terminal);
	assert(t.head.children.get("foo").children.get("bar").value == 4);

	p = new Path("foo");
	t.insert(p, 5);

	assert(t.head.children.get("foo").is_terminal);
	assert(t.head.children.get("foo").value == 5);
	assert(t.head.children.get("foo").children.get("bar").is_terminal);
	assert(t.head.children.get("foo").children.get("bar").value == 4);
	assert(t.head.children.get("foo").children.size() == 1);

	assert(t.get(p).get() == 5);

	p = new Path("abc");

	assert(!t.get(p).isPresent());
	assert(!t.contains(p));

	p = new Path("foo/bar");

	assert(t.get(p).get() == 4);
	assert(t.contains(p));

	p =  new Path("baz");

	assert(t.get(p).get() == 2);
	assert(t.contains(p));

	assert(t.remove(p));

	assert(!t.head.children.containsKey("baz"));
	assert(t.head.children.get("foo").value == 5);
	assert(!t.get(p).isPresent());
	
	p = new Path("fizz");
	assert(!t.remove(p));

	p = new Path("foo");
	assert(t.remove(p));

	assert(!t.head.children.get("foo").is_terminal);
	assert(t.head.children.get("foo").children.get("bar").is_terminal);
	assert(t.head.children.get("foo").children.get("bar").value == 4);

	assert(!t.get(p).isPresent());

	p = new Path("foo/bar");

	assert(t.get(p).get() == 4);

	assert(t.remove(p));

	assert(!t.get(p).isPresent());
	assert(!t.head.children.containsKey("foo"));
	

	System.out.println("...passed");
    }
    
    public void insert(Trie<Integer> t, String path_name, int value) throws Exception {
    	t.insert(new Path(path_name), value);
    }
    
    public void a_get(Trie<Integer> t, String path_name, int desired_value) throws Exception {
    	assert(t.get(new Path(path_name)).get() == desired_value);
    }
    
    @Test
    public void test2() throws Exception {
    	System.out.println("Testing Trie Nested Paths...");
    	Trie<Integer> t = new Trie<>();
    	insert(t, "foo/a", 1);
    	insert(t, "foo/b", 2);
    	insert(t, "foo/c", 3);
    	Set<String> suffixes = t.get_paths_under(new Path("foo"));
    	assert(suffixes.size() == 3);
    	assert(suffixes.contains("a"));
    	assert(suffixes.contains("b"));
    	assert(suffixes.contains("c"));
    	a_get(t, "foo/a", 1);
    	a_get(t, "foo/b", 2);
    	a_get(t, "foo/c", 3);
    	
    	assert(t.get_paths_under(new Path("asdasd")).size() == 0);
    	
    	insert(t, "foo/a/b", 4);
    	insert(t, "foo/a/c", 5);
    	
    	suffixes = t.get_paths_under(new Path("foo/a"));
    	assert(suffixes.size() == 2);
    	assert(suffixes.contains("b"));
    	assert(suffixes.contains("c"));
    	assert(t.get_paths_under(new Path("foo")).size() == 3);
    	
    	System.out.println("...passed");
    }
}
