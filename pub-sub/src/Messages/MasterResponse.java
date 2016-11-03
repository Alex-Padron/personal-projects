package Messages;

import java.util.Optional;

public class MasterResponse {
    public enum T {
	INVALID_REQUEST,
	PUBLISHER_INFO,
	ACCEPT_UPDATE,
	NO_PUBLISHER_FOR_PATH
    }
    public final T type;
    public final Optional<String> hostname;
    public final Optional<Integer> port;

    /**
     * Master response without publisher data
     * @param type: of message. Should not be [PUBLISHER_INFO], since this
     * constructor does not take publisher info
     */
    public MasterResponse(T type) {
	this.type = type;
	this.hostname = Optional.empty();
	this.port = Optional.empty();
    }

    /**
     * Create a [PUBLISHER_INFO] response.
     * @param hostname: of publisher
     * @param port: of publisher
     */
    public MasterResponse(String hostname, int port) {
	this.type = T.PUBLISHER_INFO;
	this.hostname = Optional.of(hostname);
	this.port = Optional.of(port);
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) return false;
	if (!MasterResponse.class.isAssignableFrom(obj.getClass())) return false;
	MasterResponse other = (MasterResponse) obj;
	return other.type == this.type
	    && other.hostname.equals(this.hostname)
	    && other.port.equals(this.port);
    }
}
