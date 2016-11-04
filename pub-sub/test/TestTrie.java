import org.junit.Test;

import Master.Paths.Path;
import Master.Trie.Trie;

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
}
