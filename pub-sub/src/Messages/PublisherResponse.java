package Messages;

import java.util.Optional;

public class PublisherResponse<V> {
	public enum T {
	    VALUE_RESPONSE,
	    NOT_PUBLISHING_PATH,
	    AM_PUBLISHING_PATH
	}
	public final T type;
	public final Optional<V> value;
	
	public PublisherResponse(T type) {
		this.value = Optional.empty();
		this.type = type;
	}
	
	public PublisherResponse(V value) {
		this.type = T.VALUE_RESPONSE;
		this.value = Optional.of(value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof PublisherResponse<?>)) return false;
		PublisherResponse<?> other = (PublisherResponse<?>) obj;
		return other.type.equals(this.type) && other.value.equals(this.value);
	}
}
