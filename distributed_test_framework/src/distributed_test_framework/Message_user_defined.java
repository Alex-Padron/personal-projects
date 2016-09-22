package distributed_test_framework;

public class Message_user_defined extends Message_builtin implements Message {
	private int uid;
	
	public Message_user_defined(int uid) {
		this.uid = uid;
	}
	
	public int get_uid() {
		return this.uid;
	}
}
