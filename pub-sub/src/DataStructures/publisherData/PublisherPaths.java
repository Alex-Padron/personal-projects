package DataStructures.publisherData;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.net.InetSocketAddress;

import DataStructures.Path;
import DataStructures.TwoWayMap;
import DataStructures.trie.Trie;

/*
 * Data store for relation from path to publisher address. [addrs] stores the
 * set of publisher paths in a two way map from int id to address. [trie] stores
 * a relation from path to int that can be indexed into [addrs]. [p_num] is a
 * strictly increasing index used to id publishers.
 *
 * This data structure is thread safe using a global lock
 */
public class PublisherPaths {
    private Trie<PathNode> trie;
    private TwoWayMap<Integer, PublisherNode> addrs;
    private int publisher_num;
    private Lock lock;

    public PublisherPaths() {
	this.trie = new Trie<>();
	this.addrs = new TwoWayMap<>();
	this.publisher_num = 0;
	this.lock = new ReentrantLock();
    }

    public boolean add(Path path, InetSocketAddress publisher_addr) {
    	return add(path, publisher_addr, "");
    }
    public boolean add(Path path, InetSocketAddress publisher_addr, String lock_code) {
	lock.lock();
	Optional<PathNode> maybe_currently_inserted = trie.get(path);
	if (maybe_currently_inserted.isPresent()) {
	    PathNode currently_inserted = maybe_currently_inserted.get();
	    // check whether the path is locked and verify the lock code
	    if (currently_inserted.lock_code.length() > 0 &&
		(!currently_inserted.lock_code.equals(lock_code))) {
		lock.unlock();
		return false;
	    }
	    // decrement the ref count of the publisher being removed
	    addrs.get_v(currently_inserted.publisher_id).refcount--;
	}
	PublisherNode publisher = new PublisherNode(publisher_addr);
	if (!addrs.contains_v(publisher)) {
	    addrs.insert(publisher_num, publisher);
	    publisher_num++;
	}
	int publisher_id = addrs.get_k(publisher);
	addrs.get_v(publisher_id).refcount++;
	PathNode to_insert = new PathNode(publisher_id, lock_code);
	trie.insert(path, to_insert);
	lock.unlock();
	return true;
    }

    public boolean remove(Path path) {
    	return remove(path, "");
    }

    public boolean remove(Path path, String lock_code) {
	lock.lock();
	Optional<PathNode> maybe_to_remove = trie.get(path);
	if (maybe_to_remove.isPresent()) {
	    PathNode to_remove = maybe_to_remove.get();
	    // check if the publisher has permission to delete from this path
	    if (to_remove.lock_code.length() > 0 && (!to_remove.lock_code.equals(lock_code))) {
		lock.unlock();
		return false;
	    }
	    boolean did_remove = trie.remove(path);
	    if (did_remove) {
		PublisherNode removed_from = addrs.get_v(to_remove.publisher_id);
		removed_from.refcount -= 1;
		if (removed_from.refcount == 0) {
		    addrs.remove_k(to_remove.publisher_id);
		}
	    }
	    lock.unlock();
	    return true;
	}
	lock.unlock();
	return true;
    }

    public InetSocketAddress get(Path path) {
	this.lock.lock();
	PathNode from_trie = trie.get(path).get();
	InetSocketAddress r = addrs.get_v(from_trie.publisher_id).addr;
	this.lock.unlock();
	return r;
    }

    public boolean contains(Path path) {
	this.lock.lock();
	boolean r = this.trie.contains(path);
	this.lock.unlock();
	return r;
    }

    public Set<String> get_paths_under(Path path) {
	return trie.get_paths_under(path);
    }
}
