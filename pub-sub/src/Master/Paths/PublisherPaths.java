package Master.Paths;

import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.net.InetSocketAddress;

import Master.Trie.Trie;

/*
 * Data store for relation from path to publisher address. [addrs] stores the
 * set of publisher paths in a two way map from int id to address. [trie] stores
 * a relation from path to int that can be indexed into [addrs]. [p_num] is a
 * strictly increasing index used to id publishers.
 *
 * This data structure is thread safe using a global lock
 */
public class PublisherPaths {
    private Trie<Integer> trie;
    private TwoWayMap<Integer, PublisherNode> addrs;
    private int publisher_num;
    private Lock lock;

    public PublisherPaths() {
	this.trie = new Trie<>();
	this.addrs = new TwoWayMap<>();
	this.publisher_num = 0;
	this.lock = new ReentrantLock();
    }

    public void add(Path path, InetSocketAddress publisher_addr) {
	this.lock.lock();
	PublisherNode publisher_node = new PublisherNode(publisher_addr);
	if (!addrs.contains_v(publisher_node)) {
	    addrs.insert(publisher_num, publisher_node);
	    publisher_num++;
	}
	// check if there is a path that will be overwritten and decrement the
	// refcount
	Optional<Integer> id_to_remove = trie.get(path);
	if (id_to_remove.isPresent()) {
	    addrs.get_v(id_to_remove.get()).refcount++;
	}
	int publisher_id = addrs.get_k(publisher_node);
	addrs.get_v(publisher_id).refcount++;
	trie.insert(path, publisher_id);
	this.lock.unlock();
    }

    // throws exception if path not in trie
    public void remove(Path path) {
	this.lock.lock();
	int publisher_id = trie.get(path).get();
	if (trie.remove(path)) {
	    PublisherNode r = addrs.get_v(publisher_id);
	    r.refcount -= 1;
	    if (r.refcount == 0) {
		addrs.remove_k(publisher_id);
	    }
	}
	this.lock.unlock();
    }

    // throws exception if path is not in trie
    public InetSocketAddress get(Path path) {
	this.lock.lock();
	int publisher_id = trie.get(path).get();
	InetSocketAddress r = addrs.get_v(publisher_id).addr;
	this.lock.unlock();
	return r;
    }

    public boolean contains(Path path) {
	this.lock.lock();
	boolean r = this.trie.contains(path);
	this.lock.unlock();
	return r;
    }
}
