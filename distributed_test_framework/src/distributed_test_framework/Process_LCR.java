package distributed_test_framework;

import java.util.ArrayList;

public class Process_LCR extends Process_builtin implements Process {
	//user defined state
	private boolean sent_highest_seen;
	private int highest_seen;
	private final int UID;
	private boolean is_leader;
	private boolean is_halted;
	
	public Process_LCR(int UID) {
		this.highest_seen = UID;
		this.sent_highest_seen = false;
		this.UID = UID;
		this.is_leader = false;
		this.is_halted = false;
	}
	
	@Override
	public ArrayList<Message_user_defined> msgs() {
		if (!sent_highest_seen || this.is_leader) {
			if (this.is_leader) this.is_halted = true;
			ArrayList<Message_user_defined> outgoing_msgs = new ArrayList<Message_user_defined>();
			for (int neighbor_address : this.get_sendable_addresses()) {
				this.send(outgoing_msgs, neighbor_address, new Message_user_defined(this.highest_seen));
			}
			sent_highest_seen = true;
			return outgoing_msgs;
		}
		return new ArrayList<>();
	}
	

	@Override
	public void trans(ArrayList<Message_user_defined> msgs) {
		for (Message_user_defined msg : msgs) {
			if (msg.get_uid() > this.highest_seen) {
				this.highest_seen = msg.get_uid();
				this.sent_highest_seen = false;
			} else if (msg.get_uid() == this.UID) { //if we see our own uid again make ourselves leader
				this.is_leader = true;
			} else if (msg.get_uid() == this.highest_seen) { // if we see the highest uid again must be broadcast by leader
				this.is_halted = true;
				this.sent_highest_seen = false; //signal to pass on message from leader
			}
		}
	}

	@Override
	public boolean is_halted() {
		return this.is_halted;
	}
	
	@Override 
	public String toString() {
		return "UID = " + this.UID + " highest seen: " + this.highest_seen + " sent highest seen: " + this.sent_highest_seen;
	}
}
