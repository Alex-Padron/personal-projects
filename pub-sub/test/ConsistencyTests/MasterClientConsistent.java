package ConsistencyTests;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import DataStructures.Path;
import Public.MasterClient;

public class MasterClientConsistent {
    private MasterClient master_client;
    private Map<String, Integer> most_recent_values;
    private Set<String> all_paths_sent;
    private Set<String> locked_paths;
    private Lock lock;

    public MasterClientConsistent(int port) throws UnknownHostException, IOException {
	this.master_client = new MasterClient("localhost", port);
	this.most_recent_values = new HashMap<>();
	this.all_paths_sent = new HashSet<>();
	this.locked_paths = new HashSet<>();
	this.lock = new ReentrantLock();
    }

    public void add(String path_name, int port, String lock_code) throws IOException, Exception {
	lock.lock();
	boolean success = master_client.register_path(new Path(path_name),
						      "localhost",
						      port,
						      lock_code);
	if (success) {
	    all_paths_sent.add(path_name);
	    most_recent_values.put(path_name, port);
	    if (lock_code.length() > 0) {
		locked_paths.add(path_name);
	    }
	}
	lock.unlock();
    }

    public void remove(String path_name, String lock_code) throws IOException, Exception {
	lock.lock();
	boolean success =
	    master_client.remove_path(new Path(path_name), lock_code);
	if (success && most_recent_values.containsKey(path_name)) {
	    most_recent_values.remove(path_name);
	}
	this.lock.unlock();
    }

    public void freeze() {
	lock.lock();
    }

    public void unfreeze() {
	lock.unlock();
    }

    // only call when frozen
    public Map<String, Integer> get_most_recent_values() {
	return most_recent_values;
    }

    // only call when frozen
    public Set<String> get_locked_paths() {
	return locked_paths;
    }

    // only call when frozen
    public Set<String> get_all_paths_sent() {
	return this.all_paths_sent;
    }
}
