package Messages;

import java.util.Optional;

public class PublisherMessage {
    public MessageTypes type;
    public Optional<String> path;
    public Optional<Integer> value;

    public PublisherMessage(MessageTypes type, Optional<String> path,
			    Optional<Integer> value) {
	this.type      = type;
	this.path = path;
	this.value     = value;
    }
}
