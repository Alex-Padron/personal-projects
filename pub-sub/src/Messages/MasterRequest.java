package Messages;

import java.util.Optional;

import Master.Paths.Path;

public class MasterRequest {
    public enum T {
	REGISTER_PUBLISHER,
	REMOVE_PUBLISHER,
	GET_PUBLISHER_OF_PATH,
	GET_PATHS_UNDER,
    }
    public final T type;
    public final Path path;
    public final Optional<String> hostname;
    public final Optional<Integer> port;

    /**
     * @param type: type of message to send. Use this constructor for
     * [REMOVE_PUBLISHER] or [GET_PUBLISHER_OF_PATH] messages
     * @param path: path corresponsing to this message.
     */
    public MasterRequest(T type, Path path) {
	this.type = type;
	this.path = path;
	this.hostname = Optional.empty();
	this.port = Optional.empty();
    }

    /**
     * Creates a new [REGISTER_PUBLISHER] message
     * @param path: to register publisher for
     * @param hostname: of publisher
     * @param port: of publisher
     */
    public MasterRequest(Path path, String hostname, int port) {
	this.type = T.REGISTER_PUBLISHER;
	this.path = path;
	this.hostname = Optional.of(hostname);
	this.port = Optional.of(port);
    }
}
