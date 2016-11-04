package Messages;

import Master.Paths.Path;

public class PublisherRequest {
    public enum T {
	GET_PATH_VALUE,
	QUERY_PUBLISHING_PATH,
    }
    public final T type;
    public final Path path;

    /**
     * @param type: of the request
     * @param path: to query on
     */
    public PublisherRequest(T type, Path path) {
	this.type = type;
	this.path = path;
    }
}
