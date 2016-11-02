package Messages;

import java.util.Optional;

public class PublisherMessage {
    public int type;
    public Optional<String> path;
    public Optional<Integer> value;

    public PublisherMessage(int type, Optional<String> path,
			    Optional<Integer> value) {
	this.type      = type;
	this.path = path;
	this.value     = value;
    }
}
