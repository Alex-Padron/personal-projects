package Messages.Bodies;

import DataStructures.Path;
import Messages.Serializable;

public class RemovePathBody extends Serializable {
	public final Path path;
	public final String lock_code;
	
	public RemovePathBody(Path path, String lock_code) {
		this.path = path;
		this.lock_code = lock_code;
	}
}
