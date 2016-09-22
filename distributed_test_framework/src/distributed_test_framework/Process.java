package distributed_test_framework;

import java.util.ArrayList;
import java.util.HashSet;

public interface Process {
	
	/**
	 * message generation function mapping state to output messages
	 * @return list of output messages
	 */
	public ArrayList<Message_user_defined> msgs();
	
	/**
	 * Update state based on incoming messages
	 * @param incoming_messages - set of messages to process
	 */
	public void trans(ArrayList<Message_user_defined> incoming_messages);

	/**
	 * Send a message from this process to another process at the given address.
	 * swallows msg if address does not map to another node. 
	 * @param address - the address to send to 
	 * @param msg - the message to send
	 */
	public void send(ArrayList<Message_user_defined> outgoing_messages, int reciever_address, Message_user_defined msg);
	
	/**
	 * @return the address of this node
	 */
	public int get_address();
	
	/**
	 * @return whether this node is in a halted state
	 */
	public boolean is_halted();
	
	public HashSet<Integer> get_sendable_addresses();
	
	public void add_neighbor(int address);
	
	public void set_address(int address);
}
