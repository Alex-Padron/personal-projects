import java.net.InetSocketAddress;
import java.util.Hashtable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MasterData {
    private Hashtable<String, InetSocketAddress> name_to_server;
    private Lock lock;

    public MasterData() {
    	this.name_to_server = new Hashtable<>();
    	this.lock = new ReentrantLock();
    }

    public void add_client(String client_name, InetSocketAddress client_addr) {
    	this.lock.lock();
    	this.name_to_server.put(client_name, client_addr);
    	this.lock.unlock();
    }

    public void remove_client(String client_name) {
    	this.lock.lock();
    	if (this.name_to_server.contains(client_name)) {
    		this.name_to_server.remove(client_name);
    	}
    	this.lock.unlock();
    }

    public InetSocketAddress get_client(String client_name) {
    	this.lock.lock();
    	InetSocketAddress client_addr = this.name_to_server.get(client_name);
    	this.lock.unlock();
    	return client_addr;
    }

    public boolean contains(String client_name) {
	this.lock.lock();
	boolean result = this.name_to_server.contains(client_name);
	this.lock.unlock();
	return result;
    }
}
