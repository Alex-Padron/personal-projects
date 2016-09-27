package distributed_test_framework;

import java.util.ArrayList;
import java.util.Optional;

public class Process_HS extends Process_builtin implements Process {
	private int UID;
	private Optional<Message_user_defined> send_left;
	private Optional<Message_user_defined> send_right;
	private boolean is_leader;
	private boolean is_halted;
	private int phase = 0;
	
	@Override
	public ArrayList<Message_user_defined> msgs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void trans(ArrayList<Message_user_defined> incoming_messages) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean is_halted() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override 
	public String toString() {
		return "";
	}
	
}
