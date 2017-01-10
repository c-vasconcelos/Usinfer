package algorithm_structures;

import class_elements.MoolMethod;

public class UsageBranch {

	//private String action;
	private MoolMethod action;
	private UsageState[] next_states;
	
	public UsageBranch(MoolMethod action, UsageState[] next_states) {
		this.action = action;
		this.next_states = next_states;
	}
	
	public MoolMethod getAction() {
		return action;
	}
	
	public UsageState[] getNextStates() {
		return next_states;
	}
	
	public String toString() {
		
		/*if(next_states.length == 2)
			return action.getMethodName() + "; <" + next_states[0] + " + " + next_states[1] + ">";
		
		return action.getMethodName() + "; " + next_states[0];*/
		
		if(next_states.length == 2)
			return action.getMethodName() + "; <" + next_states[0].getUsageStateId() + " + " + next_states[1].getUsageStateId() + ">";
		
		return action.getMethodName() + "; " + next_states[0].getUsageStateId();
	}
}
