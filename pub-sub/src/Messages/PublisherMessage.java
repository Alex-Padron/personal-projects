package Messages;

import java.util.Optional;

public class PublisherMessage<T> {
    public MessageTypes type;
    public Optional<String> path;
    public Optional<T> value;

    public PublisherMessage(MessageTypes type, Optional<String> path,
			    Optional<T> value) {
	this.type      = type;
	this.path = path;
	this.value     = value;
    }
}
