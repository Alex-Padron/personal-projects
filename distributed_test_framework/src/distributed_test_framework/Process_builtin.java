package distributed_test_framework;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class Process_builtin implements Process {
	private HashSet<Integer> sendable_addresses = new HashSet<>();
	private int address;
	
	@Override
	public void set_address(int address) {
		this.address = address;
	}
	
	@Override
	public int get_address() {
		return this.address;
	}
	
	@Override
	public void add_neighbor(int address) {
		sendable_addresses.add(address);
	}
	
	@Override
	public HashSet<Integer> get_sendable_addresses() {
		HashSet<Integer> sendable_addresses_copy = new HashSet<>();
		for (int address : sendable_addresses) {
			sendable_addresses_copy.add(address);
		}
		return sendable_addresses_copy;		
	}
	
	@Override
	public void send(ArrayList<Message_user_defined> outgoing_messages, int reciever_address, Message_user_defined msg) {
		msg.set_reciever(reciever_address);
		msg.set_sender(this.address);
		outgoing_messages.add(msg);
	}
}
