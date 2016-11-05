package Master;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread safe set of sockets to be closed on shutdown by the master
 */
public class SocketSet {
    private Lock lock;
    private Map<Integer, Socket> sockets;
    private int socket_id;

    public SocketSet() {
	this.lock = new ReentrantLock();
	this.sockets = new HashMap<>();
    }

    public int add(Socket sock) {
	lock.lock();
	sockets.put(socket_id, sock);
	int to_return = socket_id;
	socket_id += 1;
	lock.unlock();
	return to_return;
    }

    public void remove(int socket_id) {
	lock.lock();
	sockets.remove(socket_id);
	lock.unlock();
    }

    public List<Socket> to_close() {
	ArrayList<Socket> to_close = new ArrayList<>();
	for (int key : sockets.keySet()) {
	    to_close.add(sockets.get(key));
	}
	return to_close;
    }
}
