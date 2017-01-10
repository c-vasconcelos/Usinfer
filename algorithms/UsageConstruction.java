package algorithms;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import algorithm_structures.EPA;
import algorithm_structures.State;
import algorithm_structures.Usage;
import algorithm_structures.UsageBranch;
import algorithm_structures.UsageState;
import class_elements.MoolMethod;


public class UsageConstruction {

	List<UsageState> us;
	int phi;

	public UsageConstruction() {
		us = new LinkedList<UsageState>();
		phi = 0;
	}

	public void generateUsage(State n, EPA g) {

		List<State> initial_states = new LinkedList<State>();
		initial_states.add(n);

		generateUsage(n, g, new HashMap<State, UsageState>());
		
		//System.out.println("IS USAGE CORRECT? " + checkUsage(us.get(0), new HashMap<String, UsageState>()));
		
	}

	private UsageState existsInDelta(Map<State, UsageState> delta, State s) {

		Iterator<State> states_in_usage = delta.keySet().iterator();

		while(states_in_usage.hasNext()) {
			State s2 = states_in_usage.next();
			if( s2.isSameState(s))
				return delta.get(s2);
		}

		return null;
	}

	private UsageState generateUsage(State n, EPA g, Map<State, UsageState> delta) {

		UsageState usage_id = this.existsInDelta(delta, n);

		if(usage_id != null) {
			return usage_id;
		} else if (n.getActions().size() == 0) {
			return new UsageState("end");
		} else {
			UsageState usage_s = new UsageState("Q" + ++phi);

			delta.put(n, usage_s);
			
			Iterator<MoolMethod> it_a = n.getActions().iterator();

			while(it_a.hasNext()) {
				
				MoolMethod a = it_a.next();
				UsageState u1,u2;
				
				List<State> possible_transitions = g.getPossibleTransitions(n, a);

				if(possible_transitions.size() == 1) {

					u1 = generateUsage(possible_transitions.get(0), g, delta);

					UsageState[] next_usages = {u1};

					usage_s.addUsageBranch(new UsageBranch(a, next_usages ) );

				} else {

					u1 = generateUsage(possible_transitions.get(0), g, delta);
					u2 = generateUsage(possible_transitions.get(1), g, delta);

					UsageState[] next_usages = {u1, u2};

					usage_s.addUsageBranch(new UsageBranch(a, next_usages ) );
				}
			}

			usage_s.setUsageStateShareType();
			us.add(0, usage_s);
			return usage_s;	
		}
	}
	
	public List<UsageState> getUsageStates() {
		return us;
	}

	public Usage getUsage(String mool_class_name) {
		return new Usage(mool_class_name, us);
	}
	
	private boolean checkUsage(UsageState usage_state, HashMap<String, UsageState> checked_usage_states) {
		
		String usage_state_sh = usage_state.getUsageStateShareType();
		
		if(checked_usage_states.get(usage_state.getUsageStateId()) != null) {
			return true;
		}
		
		checked_usage_states.put(usage_state.getUsageStateId(), usage_state);
		
		Iterator<UsageBranch> usage_branches_it = usage_state.getUsageBranches().iterator();
		UsageBranch usage_branch;
		
		while(usage_branches_it.hasNext()) {
			usage_branch = usage_branches_it.next();
			if(usage_state_sh.equals("un")) {
				
				if(usage_branch.getNextStates().length == 2) {
					return false;
				}
				
				if(usage_branch.getNextStates()[0].getUsageStateShareType().equals("lin")) {
					return false;
				}
			}

			if(usage_branch.getNextStates().length == 2) {
				if(!this.checkUsage(usage_branch.getNextStates()[0], checked_usage_states))
					return false;
				if(!this.checkUsage(usage_branch.getNextStates()[1], checked_usage_states))
					return false;
				
			} else {
				if(!this.checkUsage(usage_branch.getNextStates()[0], checked_usage_states))
					return false;
			}
		}
		
		return true;
	}
}
