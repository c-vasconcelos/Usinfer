package algorithm_structures;

import class_elements.MoolMethod;

public class Transition {

	private State state_a;
	private MoolMethod action;
	private State state_b;
	private boolean variant;
	
	public Transition( State state_a, MoolMethod action, State state_b, boolean variant ) {
		this.state_a = state_a;
		this.action = action;
		this.state_b = state_b;
		this.variant = variant;
	}
	
	public State getStateA() {
		return state_a;
	}
	
	public State getStateB() {
		return state_b;
	}
	
	public MoolMethod getAction() {
		return action;
	}
	
	public boolean getVariant() {
		return variant;
	}
	
	public String toString() {
		return "delta(" + state_a + ", "+ action.getMethodName() +") = " + state_b;
	}
}
