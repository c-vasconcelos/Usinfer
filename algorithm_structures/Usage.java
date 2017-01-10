package algorithm_structures;

import java.util.Iterator;
import java.util.List;

import helpers.MoolPrinter;

public class Usage {

	private String mool_class_name;
	private List<UsageState> usage_states;
	
	public Usage(String mool_class_name, List<UsageState> usage_states) {
		this.mool_class_name = mool_class_name;
		this.usage_states = usage_states;
	}
	
	public List<UsageState> getUsageStates() {
		return usage_states;
	}
	
	/*public void verifyUsageShareType() {
		Iterator<UsageState> states = usage_states.iterator();
		
		//Check for states without accepting states
		boolean has_accepting_state = false;
		
		while(states.hasNext() && !has_accepting_state) {
			if(states.next().isAcceptingState())
				has_accepting_state = true;
		}
		
		if(!has_accepting_state) {
			states = usage_states.iterator();
			
			while(states.hasNext()) {
				states.next().setShareTypeUnrestricted();
				System.out.println("ALERT!!!ALERT!!!ALERT!!!ALERT!!!ALERT!!!ALERT!!!ALERT!!!ALERT!!!ALERT!!!	" + usage_states.get(0).getUsageBranches().get(0).getAction());

			}
		}
		
		//Check for sync methods
		states = usage_states.iterator();
		
		while(states.hasNext() && !has_accepting_state) {
			if(states.next().isAcceptingState())
				has_accepting_state = true;
		}
	}*/
	
	/*public String[] getNextState(String current_state, String action) {
		
		Iterator<UsageState> usage_state_iterator = usage_states.iterator();
		
		UsageState state = null;
		
		while(usage_state_iterator.hasNext() && state == null) {
			state = usage_state_iterator.next();
			
			if(!state.getUsageStateId().equals(current_state))
				state = null;
		}
		
		return state.getBranch(action).getNextStates();
	}*/
	
	public String toString() {
		//List<UsageState> us = uc.getUsageStates();
		
		String usage = "\tusage lin{" + mool_class_name + ";";
		
		if( usage_states.size() == 0 ) {
			usage += " end};";
		} else {
			
			usage += " " + usage_states.get(0).getUsageStateId() + "} where \n";
					
			Iterator<UsageState> usage_states_it = usage_states.iterator();
			
			while(usage_states_it.hasNext()) {
				usage += "\t\t" + usage_states_it.next();
				
				if(usage_states_it.hasNext())
					usage += "\n";
				else
					usage += ";";
			}
		}
		
		return usage;
	}
	
	public void printToFile(MoolPrinter printer) {
		
		boolean aaa = true;
		
		String usage = "usage lin{" + mool_class_name + ";";
				
		if( usage_states.size() == 0 ) {
			usage += " end};";
			
			printer.printLine(usage);
		} else {
			
			usage += " " + usage_states.get(0).getUsageStateId() + "} where";
					
			printer.printLine(usage);
			printer.increaseIndentation();
			Iterator<UsageState> usage_states_it = usage_states.iterator();
			
			UsageState state;
			while(usage_states_it.hasNext()) {
				state = usage_states_it.next();
				usage = state.toString();
				
				if(!usage_states_it.hasNext())
					usage += ";";
				
				printer.printLine(usage);
				
				if(state.getUsageStateShareType().equals("un")) {
					aaa = false;
				}
			}
			//printer.printLine(usage);
			printer.decreaseIndentation();
		}
	}
}
