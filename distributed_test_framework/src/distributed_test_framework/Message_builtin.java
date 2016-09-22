package distributed_test_framework;

public class Message_builtin {
	private int sender_address;
	private int reciever_address;
	
	public int get_sender() {
		return this.sender_address;
	}
	
	public int get_reciever() {
		return this.reciever_address;
	}
	
	public void set_sender(int sender_address) {
		this.sender_address = sender_address;
	}
	
	public void set_reciever(int reciever_address) {
		this.reciever_address = reciever_address;
	}
}
