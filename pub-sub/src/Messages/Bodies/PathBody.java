package Messages.Bodies;

import DataStructures.Path;
import Messages.Serializable;

public class PathBody extends Serializable {
    public final Path path;

    public PathBody(Path path) {
	this.path = path;
    }
}
