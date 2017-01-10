package algorithm_structures;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class UsageState {

	private String usage_state_id;
	private String usage_state_share;
	private List<UsageBranch> usage_branches;
	
	public UsageState(String usage_state_id) {
		this.usage_state_id = usage_state_id;
		usage_branches = new LinkedList<UsageBranch>();
		this.usage_state_share = getUsageStateType();
	}
	
	public void addUsageBranch(UsageBranch usage_branch) {
		usage_branches.add(usage_branch);
	}
	
	public String getUsageStateId() {
		return usage_state_id;
	}
	
	public void setUsageStateShareType() {
		usage_state_share = getUsageStateType();
	}
	
	public void setShareTypeUnrestricted() {
		usage_state_share = "un";
	}
	
	public String getUsageStateShareType() {
		return usage_state_share;
	}
	
	public List<UsageBranch> getUsageBranches() {
		return usage_branches;
	}
	
	public UsageBranch getBranch(String action) {
		Iterator<UsageBranch> usage_branch_iterator = usage_branches.iterator();
		
		UsageBranch branch = null;
		
		while(usage_branch_iterator.hasNext()) {
			branch = usage_branch_iterator.next();
			
			if(branch.getAction().equals(action))
				return branch;
		}
		
		return null;
	}
	
	public String toString() {
		//String res = usage_state_id + " = " + getUsageStateType() + "{";
		
		String res = usage_state_id + " = " + usage_state_share + "{";
		
		Iterator<UsageBranch> usage_branches_it = usage_branches.iterator();
		
		while(usage_branches_it.hasNext()) {
			res += usage_branches_it.next();
			
			if(usage_branches_it.hasNext()) {
				res += " + ";
			}
		}
		return res + "}";
	}
	
	private String getUsageStateType() {
		Iterator<UsageBranch> usage_branches_it = usage_branches.iterator();
		boolean is_lin = false;
		while(usage_branches_it.hasNext()) {
			UsageBranch usage_branch = usage_branches_it.next();
			
			if(usage_branch.getAction().isSync())
				return "un";
			else if(usage_branch.getNextStates().length == 2)
				is_lin = true;
			else if(!usage_branch.getNextStates()[0].getUsageStateId().equals(usage_state_id))
				is_lin = true;
		}
		
		if(is_lin)
			return "lin";
		
		return "un";
	}
	
	public boolean isAcceptingState() {
		Iterator<UsageBranch> usage_branches_it = usage_branches.iterator();
		
		if(this.usage_state_share.equals("un"))
			return true;
		
		while(usage_branches_it.hasNext()) {
			UsageBranch usage_branch = usage_branches_it.next();
			
			if(usage_branch.getNextStates().length == 2) {
				if(usage_branch.getNextStates()[0].toString().equals("end"))
					return true;
				else if(usage_branch.getNextStates()[1].toString().equals("end"))
					return true;
			} else if(usage_branch.getNextStates().length == 1) {
				if(usage_branch.getNextStates()[0].toString().equals("end"))
					return true;
			}
		}
		
		return false;
	}
}
