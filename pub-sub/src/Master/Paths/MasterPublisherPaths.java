package Master.Paths;
import java.net.InetSocketAddress;
import java.util.Hashtable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Multithreading-safe store of the paths/publishers that the master
 * knows about
 */
public class MasterPublisherPaths {
    private Hashtable<String, InetSocketAddress> name_to_server;
    private Lock lock;

    public MasterPublisherPaths() {
    	this.name_to_server = new Hashtable<>();
    	this.lock = new ReentrantLock();
    }

    public void add(String publisher_name, InetSocketAddress publisher_addr) {
    	this.lock.lock();
    	this.name_to_server.put(publisher_name, publisher_addr);
    	this.lock.unlock();
    }

    public void remove(String publisher_name) {
    	this.lock.lock();
    	if (this.name_to_server.containsKey(publisher_name)) {
	    this.name_to_server.remove(publisher_name);
    	}
    	this.lock.unlock();
    }

    public InetSocketAddress get(String publisher_name) {
    	this.lock.lock();
    	InetSocketAddress publisher_addr =
	    this.name_to_server.get(publisher_name);
    	this.lock.unlock();
    	return publisher_addr;
    }

    public boolean contains(String publisher_name) {
	this.lock.lock();
	boolean result = this.name_to_server.containsKey(publisher_name);
	this.lock.unlock();
	return result;
    }
}
