package Messages;

import DataStructures.Path;
import Messages.Bodies.PathBody;

public class PublisherRequest extends Serializable {
    public enum T {
	GET_PATH_VALUE,
	QUERY_PUBLISHING_PATH,
    }
    public final T type;
    public final String body;

    /**
     * @param type: of the request
     * @param path: to query on
     */
    public PublisherRequest(T type, Path path) {
	this.type = type;
	this.body = new PathBody(path).json();
    }

    /**
     * @return whether the body of the message parses to a valid type
     * matching the specified type.
     */
    public boolean validate() {
	switch (this.type) {
	case GET_PATH_VALUE:
	case QUERY_PUBLISHING_PATH:
	    return Serializable.parse(body, PathBody.class).isPresent();
	default:
		return false;
	}
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) return false;
	if (!PublisherRequest.class.isAssignableFrom(obj.getClass())) return false;
	PublisherRequest other = (PublisherRequest) obj;
	return other.type.equals(this.type) && other.body.equals(this.body);
    }

    @Override
    public int hashCode() {
	return this.type.hashCode() + this.body.hashCode();
    }
}
