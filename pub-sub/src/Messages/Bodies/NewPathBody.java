package Messages.Bodies;

import DataStructures.Path;
import Messages.Serializable;

public class NewPathBody extends Serializable {
    public final Path path;
    public final String hostname;
    public final int port;
    public final String lock_code;

    public NewPathBody(Path path, String hostname, int port, String lock_code) {
	this.path = path;
	this.hostname = hostname;
	this.port = port;
	this.lock_code = lock_code;
    }
}
