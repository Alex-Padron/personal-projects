package Messages;

public class PublisherRequest {
	public enum T {
	    GET_PATH_VALUE,
	    QUERY_PUBLISHING_PATH,
	}
	public final T type;
	public final String path;
	
	public PublisherRequest(T type, String path) {
		this.type = type;
		this.path = path;
	}
}
