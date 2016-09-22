package distributed_test_framework;

import java.util.ArrayList;
import java.util.HashMap;

public class Controller {
	private final static int ROUND_NUMBER_LIMIT = 10000;
	
	public static ArrayList<Process> initialize_processes() {
		ArrayList<Process> processes = new ArrayList<>();
		//create 10 nodes with UID's 0 to 9 and addresses equal to their UID's
		for (int i = 0; i < 100; i++) {
			Process new_process = new Process_user_defined(i);
			new_process.set_address(i);
			processes.add(new_process);
		}
		//connect the nodes in a ring fashion
		for (int i = 0; i < 100; i++) {
			processes.get(i).add_neighbor((i + 99) % 100);
		}
		return processes;
	}
	
	public static HashMap<Integer, ArrayList<Message_user_defined>> message_generation(ArrayList<Process> processes) {
		HashMap<Integer, ArrayList<Message_user_defined>> address_to_messages_map = new HashMap<>();
		for (Process process : processes) {
			for (Message_user_defined msg : process.msgs()) {
				if (!address_to_messages_map.containsKey(msg.get_reciever())) {
					address_to_messages_map.put(msg.get_reciever(), new ArrayList<>());
				}
				address_to_messages_map.get(msg.get_reciever()).add(msg);
			}
		}
		return address_to_messages_map;
	}
	
	public static void state_transition(ArrayList<Process> processes, HashMap<Integer, ArrayList<Message_user_defined>> address_to_messages_map) {
		for (Process process : processes) {
			process.trans(address_to_messages_map.getOrDefault(process.get_address(), new ArrayList<>()));
		}
	}
	
	public static boolean check_completion(ArrayList<Process> processes) {
		for (Process process : processes) {
			if (!process.is_halted()) {
				return false;
			}
		}
		return true;
	}
	
	public static void log_process_state(ArrayList<Process> processes) {
		System.out.println("---- PROCESS STATES ----");
		for (Process process : processes) {
			System.out.println("Address: " + process.get_address() +
							   " Halted: " + process.is_halted() +
							   " State: "  + process.toString());
		}
		System.out.println("---- END PROCESS STATES ---");
	}
	
	public static int count_messages(HashMap<Integer, ArrayList<Message_user_defined>> address_to_messages_map) {
		int message_count = 0;
		for (ArrayList<Message_user_defined> msgs : address_to_messages_map.values()) {
			message_count += msgs.size();
		}
		return message_count;
	}
	
	public static void run(ArrayList<Process> processes) {
		int round_number = 0;
		int message_count = 0;
		while ((!check_completion(processes)) && round_number < ROUND_NUMBER_LIMIT) {
			log_process_state(processes);
			HashMap<Integer, ArrayList<Message_user_defined>> address_to_messages_map = message_generation(processes);
			message_count += count_messages(address_to_messages_map); 
			state_transition(processes, address_to_messages_map);
			round_number++;
		}
		log_process_state(processes);
		System.out.println("Total number of rounds: " + round_number + " Total number of messages: " + message_count);
	}
	public static void main(String[] args) {
		ArrayList<Process> processes = initialize_processes();
		run(processes);
	}
}
