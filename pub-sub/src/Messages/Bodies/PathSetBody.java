package Messages.Bodies;

import java.util.Set;

import Messages.Serializable;

public class PathSetBody extends Serializable {
	public final Set<String> paths;
	
	public PathSetBody(Set<String> paths) {
		this.paths = paths;
	}
}
