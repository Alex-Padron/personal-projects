package Messages;

import java.util.Set;

import Messages.Bodies.PathSetBody;
import Messages.Bodies.AddrBody;

public class MasterResponse extends Serializable {
    public enum T {
	INVALID_REQUEST,
	PUBLISHER_INFO,
	ACCEPT_UPDATE,
	REJECT_UPDATE,
	NO_PUBLISHER_FOR_PATH,
	PATH_SET
    }
    public final T type;
    public final String body;
    
    /**
     * Master response without publisher data
     * @param type: of message. Should not be [PUBLISHER_INFO], since this
     * constructor does not take publisher info
     */
    public MasterResponse(T type) {
    	this.type = type;
    	this.body = "";
    }

    /**
     * Create a [PUBLISHER_INFO] response.
     * @param hostname: of publisher
     * @param port: of publisher
     */
    public MasterResponse(String hostname, int port) {
    	this.type = T.PUBLISHER_INFO;
    	this.body = new AddrBody(hostname, port).json();
    }

    /**
     * Reponse that contains a set of paths
     * @param paths: to send to the client
     */
    public MasterResponse(Set<String> paths) {
	this.type = T.PATH_SET;
	this.body = new PathSetBody(paths).json();
    }
    
    public boolean validate() {
    	switch (type) {
    	case INVALID_REQUEST:
    	case ACCEPT_UPDATE:
    	case NO_PUBLISHER_FOR_PATH:
    		return body.equals("");
    	case PUBLISHER_INFO:
    		return Serializable.parse(body, AddrBody.class).isPresent();
    	case PATH_SET:
    		return Serializable.parse(body, PathSetBody.class).isPresent();
    	default:
    		return false;
    	}
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) return false;
	if (!MasterResponse.class.isAssignableFrom(obj.getClass())) return false;
	MasterResponse other = (MasterResponse) obj;
	return other.type.equals(this.type) && other.body.equals(this.body);
    }
}
