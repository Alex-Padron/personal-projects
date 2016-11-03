package Messages;

import java.util.Optional;

public class MasterRequest {
	public enum T {
	    REGISTER_PUBLISHER,
	    REMOVE_PUBLISHER,
	    GET_PUBLISHER_OF_PATH,
	}
	public final T type;
	public final String path;
	public final Optional<String> hostname;
	public final Optional<Integer> port;
	
	public MasterRequest(T type, String path) {
		this.type = type;
		this.path = path;
		this.hostname = Optional.empty();
		this.port = Optional.empty();
	}
	
	public MasterRequest(String path, String hostname, int port) {
		this.type = T.REGISTER_PUBLISHER;
		this.path = path;
		this.hostname = Optional.of(hostname);
		this.port = Optional.of(port);
	}
	
}
