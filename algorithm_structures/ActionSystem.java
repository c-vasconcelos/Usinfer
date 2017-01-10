package algorithm_structures;

import java.util.List;
import java.util.Map;

public class ActionSystem {

	private List<String> actions;
	private Map<String, String> requires;
	private Map<String, String> ensures;
	private String inv;
	private String init;
	
	public ActionSystem(List<String> actions, Map<String, String> requires, Map<String, String> ensures, String inv, String init) {
		this.actions = actions;
		this.requires = requires;
		this.ensures = ensures;
		this.inv = inv;
		this.init = init;
	}
	
	public List<String> getActions() {
		return actions;
	}
	
	public String getActionRequires(String action) {
		return requires.get(action);
	}
	
	public String getActionEnsures(String action) {
		return ensures.get(action);
	}
	
	public String getInvariant() {
		return inv;
	}
	
	public String getInitialCondition() {
		return init;
	}
}
