package Messages;

import java.util.Optional;
import java.util.Set;

public class MasterResponse {
    public enum T {
	INVALID_REQUEST,
	PUBLISHER_INFO,
	ACCEPT_UPDATE,
	NO_PUBLISHER_FOR_PATH,
	PATH_SET
    }
    public final T type;
    public final Optional<Set<String>> paths;
    public final Optional<String> hostname;
    public final Optional<Integer> port;

    /**
     * Master response without publisher data
     * @param type: of message. Should not be [PUBLISHER_INFO], since this
     * constructor does not take publisher info
     */
    public MasterResponse(T type) {
	this.type = type;
	this.hostname = Optional.empty();
	this.port = Optional.empty();
	this.paths = Optional.empty();
    }

    /**
     * Create a [PUBLISHER_INFO] response.
     * @param hostname: of publisher
     * @param port: of publisher
     */
    public MasterResponse(String hostname, int port) {
	this.type = T.PUBLISHER_INFO;
	this.hostname = Optional.of(hostname);
	this.port = Optional.of(port);
	this.paths = Optional.empty();
    }

    /**
     * Reponse that contains a set of paths
     * @param paths: to send to the client
     */
    public MasterResponse(Set<String> paths) {
	this.type = T.PATH_SET;
	this.paths = Optional.of(paths);
	this.hostname = Optional.empty();
	this.port = Optional.empty();
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) return false;
	if (!MasterResponse.class.isAssignableFrom(obj.getClass())) return false;
	MasterResponse other = (MasterResponse) obj;
	return other.type == this.type
	    && other.hostname.equals(this.hostname)
	    && other.port.equals(this.port)
	    && other.paths.equals(this.paths);
    }
}
