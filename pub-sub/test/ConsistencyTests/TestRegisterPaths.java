package ConsistencyTests;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;

import org.junit.Test;

import DataStructures.Path;
import Public.Master;
import Public.MasterClient;

public class TestRegisterPaths {
    private Master master;
    private MasterClientConsistent[] clients;
    private MasterClient to_verify;
    private AtomicMessageCounter counter;

    private Set<Integer> get_most_recent_values(String path_name) {
	Set<Integer> most_recent_values = new HashSet<>();
	for (int i = 0; i < clients.length; i++) {
	    Map<String, Integer> client_values = clients[i].get_most_recent_values();
	    if (client_values.containsKey(path_name)) {
		most_recent_values.add(client_values.get(path_name));
	    }
	}
	return most_recent_values;
    }

    private void check_consistency() throws IOException, Exception {
	for (int i = 0; i < clients.length; i++) {
	    clients[i].freeze();
	}
	Set<String> all_sent_paths = new HashSet<>();
	for (int i = 0; i < clients.length; i++) {
	    all_sent_paths.addAll(clients[i].get_all_paths_sent());
	}
	// check that each path is correctly present and that the current
	// value on the master is one of the most recent values sent to it
	for (String path_name : all_sent_paths) {
	    Set<Integer> most_recent_values = get_most_recent_values(path_name);
	    if (most_recent_values.size() == 0) {
		assert(!to_verify.get_path_addr(new Path(path_name)).isPresent());
	    } else {
		int port =
		    to_verify.get_path_addr(new Path(path_name)).get().getPort();
		assert(most_recent_values.contains(port));
	    }
	}
	// check that locked paths have the correct value
	for (int i = 0; i < clients.length; i++) {
	    Set<String> locked_paths = clients[i].get_locked_paths();
	    for (String path_name : locked_paths) {
		int recent_value = clients[i].get_most_recent_values().get(path_name);
		assert(recent_value ==
		       to_verify.get_path_addr(new Path(path_name)).get().getPort());
	    }
	}
	for (int i = 0; i < clients.length; i++) {
	    clients[i].unfreeze();
	}
    }

    public void run_client(int client_index) throws IOException, Exception {
	String lock_code = Integer.toString(client_index);
	MasterClientConsistent client = clients[client_index];
	Random rand = new Random();
	for (int i = 0; i < 200; i++) {
	    int length = rand.nextInt() % 10; // paths of max length 10
	    String s = Integer.toString(rand.nextInt(Integer.SIZE - 1) % 10);
	    for (int j = 0; j < length + 1; j++) {
		// each component of the path is a single digit
		s += "/" + (rand.nextInt(Integer.SIZE - 1) % 10);
	    }
	    counter.inc();
	    if (rand.nextInt() % 20 == 0) {
		client.add(s, client_index, lock_code);
	    } else {
		client.add(s, client_index, "");
	    }
	}
    }

    @Test
    public void test() throws IOException, InterruptedException {
    System.out.println("Testing Master Consistency...");
    
    // TODO: add remove 
    
    int port = 9000;
	master = new Master(port);
	Thread master_thread = new Thread(master);
	master_thread.start();
	to_verify = new MasterClient("localhost", port);
	counter = new AtomicMessageCounter(new Callable<Void>() {
			public Void call() throws IOException, Exception {
				check_consistency();
				return null;
			}
		});
	int CLIENT_COUNT = 5;
	clients = new MasterClientConsistent[CLIENT_COUNT];
	for (int i = 0; i < CLIENT_COUNT; i++) {
	    clients[i] = new MasterClientConsistent(port);
	}
	Thread[] client_threads = new Thread[CLIENT_COUNT];
	for (int i = 0; i < CLIENT_COUNT; i++) {
		final int j = i;
	    client_threads[i] = new Thread(new Runnable() {
		    public void run() {
			try {
				run_client(j);
			} catch (Exception e) {
				e.printStackTrace();
				assert(false);
			}
		    }
		});
	}
	for (int i = 0; i < CLIENT_COUNT; i++) {
	    client_threads[i].start();
	}
	for (int i = 0; i < CLIENT_COUNT; i++) {
	    client_threads[i].join();
	}
	System.out.println("...passed");
    }
}
