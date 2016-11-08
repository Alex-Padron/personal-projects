package Messages;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import Messages.Bodies.ValueBody;

/**
   Response from publisher to subscriber. [V] is the datatype being sent,
   must be serializable by Gson.
 */
public class PublisherResponse<V> extends Serializable {
	private static final Type t = new TypeToken<ValueBody<T>>(){}.getType();
    public enum T {
	VALUE_RESPONSE,
	NOT_PUBLISHING_PATH,
	AM_PUBLISHING_PATH,
	INVALID_REQUEST
    }
    public final T type;
    public final String body;

    /**
     * Publisher response without a value
     * @param type: of the message
     */
    public PublisherResponse(T type) {
    	this.type = type;
    	this.body = "";
    }

    /**
     * Create a response with a value
     * @param value: of the response
     */
    public PublisherResponse(V value) {
	this.type = T.VALUE_RESPONSE;
	this.body = new ValueBody<V>(value).json();
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) return false;
	if (!(obj instanceof PublisherResponse<?>)) return false;
	PublisherResponse<?> other = (PublisherResponse<?>) obj;
	return other.type.equals(this.type) && other.body.equals(this.body);
    }
    
    public boolean validate() {
	switch (this.type) {
	case NOT_PUBLISHING_PATH:
	case AM_PUBLISHING_PATH:
	case INVALID_REQUEST:
	    return body.equals("");
	case VALUE_RESPONSE:
		return Serializable.parse(body, t).isPresent(); 
	default:
		return false;
	}
    }
}
