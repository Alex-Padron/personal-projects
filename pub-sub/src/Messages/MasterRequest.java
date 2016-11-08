package Messages;

import java.net.InetSocketAddress;
import java.util.Optional;

import DataStructures.Path;
import Messages.Bodies.PathBody;
import Messages.Bodies.RemovePathBody;
import Messages.Bodies.NewPathBody;

public class MasterRequest extends Serializable {
    public enum T {
	REGISTER_PUBLISHER,
	REMOVE_PUBLISHER,
	GET_PUBLISHER_OF_PATH,
	GET_PATHS_UNDER,
    }
    public final T type;
    public final String body;

    /**
     * @param type: type of message to send. Use this constructor for
     * [REMOVE_PUBLISHER] or [GET_PUBLISHER_OF_PATH] messages
     * @param path: path corresponsing to this message.
     */
    public MasterRequest(T type, Path path) {
    	this.type = type;
    	this.body = new PathBody(path).json();
    }

    /**
     * Creates a new [REGISTER_PUBLISHER] message
     * @param path: to register publisher for
     * @param hostname: of publisher
     * @param port: of publisher
     */
    public MasterRequest(Path path, String hostname, int port, String lock_code) {
    	this.type = T.REGISTER_PUBLISHER;
    	this.body = new NewPathBody(path, hostname, port, lock_code).json();
    }
    
    /**
     * Create a master request to remove a path using the lock code provided
     */
    public MasterRequest(Path path, String lock_code) {
    	this.type = T.REMOVE_PUBLISHER;
    	this.body = new RemovePathBody(path, lock_code).json();
    }

    public boolean validate() {
	switch (this.type) {
	case REGISTER_PUBLISHER:
	    Optional<NewPathBody> pb = Serializable.parse(body, NewPathBody.class);
	    if (!pb.isPresent()) return false;
	    try {
	    	// just want to check whether the host and port are a valid address
	    	@SuppressWarnings("unused")
			InetSocketAddress i = new InetSocketAddress(pb.get().hostname, pb.get().port);
	    	return true;
	    } catch (Exception e) {
	    	return false;
	    }
	case REMOVE_PUBLISHER:
	case GET_PUBLISHER_OF_PATH:
	case GET_PATHS_UNDER:
	    return Serializable.parse(body, PathBody.class).isPresent();
	default:
		return false;
	}
    }
}
