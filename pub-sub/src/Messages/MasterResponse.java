package Messages;

import java.util.Optional;

public class MasterResponse {
	public enum T {
	    INVALID_REQUEST,
	    PUBLISHER_INFO,
	    ACCEPT_UPDATE,
	    NO_PUBLISHER_FOR_PATH
	}
	public final T type;
	public final Optional<String> hostname;
	public final Optional<Integer> port;
	
	public MasterResponse(T type) {
		this.type = type;
		this.hostname = Optional.empty();
		this.port = Optional.empty();
	}
	
	public MasterResponse(String hostname, int port) {
		this.type = T.PUBLISHER_INFO;
		this.hostname = Optional.of(hostname);
		this.port = Optional.of(port);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!MasterResponse.class.isAssignableFrom(obj.getClass())) return false;
		MasterResponse other = (MasterResponse) obj;
		return other.type == this.type && other.hostname.equals(this.hostname) && other.port.equals(this.port);
	}
}
