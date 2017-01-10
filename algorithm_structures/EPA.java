package algorithm_structures;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import class_elements.MoolMethod;

public class EPA {

	private List<MoolMethod> actions;
	private List<State> states;
	private List<State> initial_states;
	private List<Transition> transitions;
	
	public EPA() {
		actions = new LinkedList<MoolMethod>();
		states = new LinkedList<State>();
		transitions = new LinkedList<Transition>();
	}
	
	public EPA(List<MoolMethod> actions, List<State> states, List<State> initial_states, List<Transition> transitions) {
		this.actions = actions;
		this.states = states;
		this.initial_states = initial_states;
		this.transitions = transitions;
	}
	
	public List<MoolMethod> getActions() {
		return actions;
	}
	
	public List<State> getStates() {
		return states;
	}
	
	public void addState(State s) {
		states.add(s);
	}
	
	public List<State> getInitialStates() {
		return initial_states;
	}
	
	public List<Transition> getTransitions() {
		return transitions;
	}
	
	public List<State> getPossibleTransitions(State state_a, MoolMethod action) {
		
		List<State> possible_transitions = new LinkedList<State>();
		
		Iterator<Transition> it_transitions = transitions.iterator();
		//System.out.println("KOJHJB: " + Math.random());
		while(it_transitions.hasNext()) {
			Transition t = it_transitions.next();
			
			if(t.getStateA().isSameState(state_a) && t.getAction().equals(action)) {
				if(possible_transitions.size() == 1 && t.getVariant()) {
					possible_transitions.add(0, t.getStateB());
				} else 
					possible_transitions.add(t.getStateB());
			}
		}
		
		return possible_transitions;
	}
	
	public void addTransition(Transition t) {
		transitions.add(t);
	}
	
	public void printEPA() {
		System.out.println("EPA:");
		System.out.print("Sigma: {");
		
		Iterator<MoolMethod> actions_it = actions.iterator();
		
		while(actions_it.hasNext()) {
			System.out.print(actions_it.next().getMethodName());
			
			if( actions_it.hasNext() )
				System.out.print(", ");
			else
				System.out.print("}\n");
		}
		
		System.out.print("S: {");
		
		Iterator<State> states_it = states.iterator();
		
		while(states_it.hasNext()) {
			
			System.out.print(states_it.next());
			
			if( states_it.hasNext() )
				System.out.print(", ");
			else
				System.out.print("}\n");
		}
		
		System.out.print("S0: {");
		
		states_it = initial_states.iterator();
		
		while(states_it.hasNext()) {
			
			System.out.print(states_it.next());
			
			if( states_it.hasNext() )
				System.out.print(", ");
			else
				System.out.print("}\n");
		}
		
		System.out.println("Delta:");
		
		Iterator<Transition> transitions_it = transitions.iterator();
		
		while(transitions_it.hasNext()) {
			
			System.out.println(transitions_it.next().toString());
			
		}
	}
}
