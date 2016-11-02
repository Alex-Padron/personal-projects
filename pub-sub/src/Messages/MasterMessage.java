package Messages;
import java.util.Optional;

/**
 * Represents messages and responses of the master.
 *
 *  [type] can be "register" or "query" for messages to the master. It can be
 *  "no publisher" or "valid" for master responses. Enum for these types in
 *  [Master]
 */
public class MasterMessage {
	public MessageTypes type;
	public Optional<String> name;
	public Optional<String> addr;
	public Optional<Integer> port;

	public MasterMessage(MessageTypes type, Optional<String> name,
			     Optional<String> addr, Optional<Integer> port) {
		this.type = type;
		this.name = name;
		this.addr = addr;
		this.port = port;
	}
}
