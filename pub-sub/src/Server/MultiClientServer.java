package Server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.google.gson.Gson;

public abstract class MultiClientServer implements Runnable{
	protected ServerSocket server_socket;
	protected Executor executor = Executors.newCachedThreadPool();
	protected SocketSet socket_set = new SocketSet();
	protected Gson parser = new Gson();
	protected String server_name;
	
	public MultiClientServer() {
		
	}
	
    public void run() {
	while(true) {
	    try {
		final Socket socket = this.server_socket.accept();
		this.executor.execute(new Runnable() {
			public void run() {
			    final int socket_id = socket_set.add(socket);
			    try {
				handle_client_connection(socket);
			    } catch (IOException e) {
				System.out.println(server_name + " CLIENT CONNECTION"
						   + " ERROR - CLOSING " + e);
				socket_set.remove(socket_id);
			    }
			}
		    });
	    } catch (IOException e) {
		System.out.println(server_name + " IO ERROR - CLOSING " + e);
		return;
	    }
	}
    }
    
    public void close() throws IOException {
	this.server_socket.close();
	for (Socket s : socket_set.to_close()) {
	    s.close();
	}
    }
    
	protected abstract void handle_client_connection(Socket socket) throws IOException;
}
