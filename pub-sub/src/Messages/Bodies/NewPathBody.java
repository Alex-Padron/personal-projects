package Messages.Bodies;

import DataStructures.Path;
import Messages.Serializable;

public class NewPathBody extends Serializable {
    public final Path path;
    public final String hostname;
    public final int port;

    public NewPathBody(Path path, String hostname, int port) {
	this.path = path;
	this.hostname = hostname;
	this.port = port;
    }
}
