package Messages;

import DataStructures.Path;
import Messages.Bodies.PathBody;
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
    public MasterRequest(Path path, String hostname, int port) {
    	this.type = T.REGISTER_PUBLISHER;
    	this.body = new NewPathBody(path, hostname, port).json();
    }

    public boolean validate() {
	switch (this.type) {
	case REGISTER_PUBLISHER:
	    return Serializable.parse(body, NewPathBody.class).isPresent();
	case REMOVE_PUBLISHER:
	case GET_PUBLISHER_OF_PATH:
	case GET_PATHS_UNDER:
	    return Serializable.parse(body, PathBody.class).isPresent();
	default:
		return false;
	}
    }
}
