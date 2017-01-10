package algorithm_structures;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import class_elements.MoolMethod;


public class State {

	//private long id;
	//private boolean is_initial;
	private List<MoolMethod> enabled_actions;
	
	public State() {
		this.enabled_actions = new LinkedList<MoolMethod>();
	}
	
	public State( List<MoolMethod> actions ) {
		this.enabled_actions = actions;//new LinkedList<String>();
	}
	
	public void addAction(MoolMethod action) {
		enabled_actions.add(action);
	}
	
	public boolean hasAction(MoolMethod action) {
		return enabled_actions.indexOf(action) > -1;
	}

	public boolean hasActions(List<MoolMethod> actions) {
		
		Iterator<MoolMethod> it = actions.iterator();
		
		while(it.hasNext()) {
			if(enabled_actions.indexOf(it.next()) > -1)
				return true;
		}
		
		return false;
	}
	
	public List<MoolMethod> getActions() {
		return enabled_actions;
	}
	
	public String toString() {
		
		String str = "{";
		
		Iterator<MoolMethod> it1 = enabled_actions.iterator();
		
		while(it1.hasNext()) {
			//System.out.print(it1.next());
			str += it1.next().getMethodName();
			
			if(it1.hasNext())
				str += ", ";
		}
		
		return str + "}";
	}
	
	public boolean isSameState(State state2) {
	    
		if(this.enabled_actions.size() != state2.getActions().size()) {
			//System.out.println("States " + this + " and " + state2 + " are NOT the same");
			return false;
		}
		
		Iterator<MoolMethod> it2 = enabled_actions.iterator();
		
		
		while(it2.hasNext()) {
			MoolMethod m = it2.next();
			
			if(!state2.getActions().contains(m)) {
				//System.out.println("States " + this + " and " + state2 + " are NOT the same");
				return false;
			}
		}
		
		//System.out.println("States " + this + " and " + state2 + " are the same");
		return true;
	}
}
