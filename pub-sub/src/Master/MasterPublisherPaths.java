package Master;
import java.net.InetSocketAddress;
import java.util.Hashtable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MasterPublisherPaths {
    private Hashtable<String, InetSocketAddress> name_to_server;
    private Lock lock;

    public MasterPublisherPaths() {
    	this.name_to_server = new Hashtable<>();
    	this.lock = new ReentrantLock();
    }

    public void add(String client_name, InetSocketAddress client_addr) {
    	this.lock.lock();
    	this.name_to_server.put(client_name, client_addr);
    	this.lock.unlock();
    }

    public void remove(String client_name) {
    	this.lock.lock();
    	if (this.name_to_server.containsKey(client_name)) {
    		this.name_to_server.remove(client_name);
    	}
    	this.lock.unlock();
    }

    public InetSocketAddress get(String client_name) {
    	this.lock.lock();
    	InetSocketAddress client_addr = this.name_to_server.get(client_name);
    	this.lock.unlock();
    	return client_addr;
    }

    public boolean contains(String client_name) {
	this.lock.lock();
	boolean result = this.name_to_server.containsKey(client_name);
	this.lock.unlock();
	return result;
    }
}
