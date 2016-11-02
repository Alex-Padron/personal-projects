package Messages;

public enum MessageTypes {
	    // Messages between master and master client
	    REGISTER,
	    QUERY,
	    NO_PUBLISHER,
	    PUBLISHER_INFO,
	    ACCEPTED_REGISTER,
	    INVALID_REQUEST,
	    REMOVE,
	    ACCEPTED_REMOVE,

	    // Messages between publisher and subscriber
	    GET_VALUE,
	    QUERY_PATH,
	    PUBLISHING_PATH,
	    NOT_PUBLISHING,
	    VALUE_RESPONSE
}
