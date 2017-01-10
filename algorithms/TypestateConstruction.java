package algorithms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.math3.util.Combinations;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;

import algorithm_structures.EPA;
import algorithm_structures.State;
import algorithm_structures.Transition;
import class_elements.MoolClass;
import class_elements.MoolMethod;
import exceptions.TooManyTransitionsException;
import logic.Prover;
import net.sf.tweety.commons.ParserException;

public class TypestateConstruction {

	Context ctx;
	
	MoolClass mool_class;
	List<MoolMethod> mool_class_methods;

	List<MoolMethod> disabled_actions;
	List<MoolMethod> enabled_actions;
	//List<String> b_plus;
	//List<String> b_minus;
	
	
	List<State> states;
	List<State> initial_states;
	List<Transition> delta;
	
	List<State> w;
	
	State A, B;
	MoolMethod a;
	
	public TypestateConstruction(Context ctx, MoolClass mool_class) {
		this.ctx = ctx;
		
		this.mool_class = mool_class;
		this.mool_class_methods = mool_class.getClassMethods();
		
		this.w = new LinkedList<State>();
		
		states = new LinkedList<State>();
		initial_states = new ArrayList<State>();
		delta = new ArrayList<Transition>();
	}

	public EPA generateEPA() throws ParserException, IOException, TooManyTransitionsException {
		this.buildDisabledActionsA();
		this.buildEnabledActionsA();
		this.buildInitialStates();
		this.theRest();
		
		return new EPA(mool_class_methods, states, initial_states, delta);
	}

	private void buildDisabledActionsA() throws ParserException, IOException {

		Iterator<MoolMethod> it = mool_class_methods.iterator();
		MoolMethod m;
		List<BoolExpr> propositions;

		disabled_actions = new ArrayList<MoolMethod>();

		while(it.hasNext()) {
			m = it.next();

			propositions = new ArrayList<BoolExpr>();
			
			propositions.add((BoolExpr) mool_class.getClassInitialCondition().getAssertion(true));
			propositions.add((BoolExpr) m.getMethodRequires().getAssertion(true));

			//System.out.println(mool_class.getClassInitialCondition());
			//System.out.println(m.getMethodRequires().getAssertion());
			
			if( !Prover.prove(ctx, propositions)) {
				disabled_actions.add(m);
			}
		}
		
		/*System.out.println("A-");
		
		Iterator<MoolMethod> aux_it = disabled_actions.iterator();
		
		while(aux_it.hasNext())
			System.out.println(aux_it.next().getMethodName());*/
	}

	private void buildEnabledActionsA() throws ParserException, IOException {
		Iterator<MoolMethod> it = mool_class_methods.iterator();
		MoolMethod m;
		List<BoolExpr> propositions;

		enabled_actions = new ArrayList<MoolMethod>();

		while(it.hasNext()) {
			m = it.next();

			propositions = new ArrayList<BoolExpr>();

			propositions.add((BoolExpr) mool_class.getClassInitialCondition().getAssertion(true));
			propositions.add(ctx.mkNot((BoolExpr) m.getMethodRequires().getAssertion(true).simplify()));

			//System.out.println(m.getMethodName());
			//System.out.println((BoolExpr) mool_class.getClassInitialCondition().getAssertion(true));
			//System.out.println(ctx.mkNot((BoolExpr) m.getMethodRequires().getAssertion(true)));
			
			if( disabled_actions.indexOf(m) == -1 && !Prover.prove(ctx, propositions)) {
				enabled_actions.add(m);
			}
		}
		
		/*System.out.println("A+");
		Iterator<MoolMethod> aux_it = enabled_actions.iterator();
		
		while(aux_it.hasNext())
			System.out.println(aux_it.next().getMethodName());*/
	}
	
	private void buildInitialStates() throws ParserException, IOException {
		Iterator<MoolMethod> it = mool_class_methods.iterator();
		MoolMethod m;
		List<BoolExpr> propositions;

		List<State> initial_candidate_states = new ArrayList<State>();
		
		initial_candidate_states = calculateCandidateStates(mool_class_methods, disabled_actions, enabled_actions);
		
		Iterator<State> initial_candidate_states_it = initial_candidate_states.iterator();
		
		while(initial_candidate_states_it.hasNext()) {
			it = mool_class_methods.iterator();
			
			propositions = new ArrayList<BoolExpr>();
			
			propositions.add((BoolExpr) mool_class.getClassInvariant().getAssertion(true));
			
			State s = initial_candidate_states_it.next();
			
			while(it.hasNext()) {
				m = it.next();
				if(s.hasAction(m) )
					propositions.add((BoolExpr) m.getMethodRequires().getAssertion(true));
				else
					propositions.add(ctx.mkNot((BoolExpr) m.getMethodRequires().getAssertion(true)));
			}
			
			propositions.add((BoolExpr) mool_class.getClassInitialCondition().getAssertion(true));
			
			//System.out.println("OverPredOf" + s);
			
			if(Prover.prove(ctx, propositions)) {
				initial_states.add(s);
				w.add(s);
			}
		}
		
		
		/*Iterator<State> aux_it = initial_states.iterator();
		
		while(aux_it.hasNext())
			System.out.println(aux_it.next());*/
	}
	
	private void theRest() throws ParserException, IOException, TooManyTransitionsException {
		
		while(!w.isEmpty()) {
	
			A = w.remove(0);
			states.add(A);
			
			Iterator<MoolMethod> actions = A.getActions().iterator();
			
			while(actions.hasNext()) {
				
				enabled_actions = new ArrayList<MoolMethod>();
				disabled_actions = new ArrayList<MoolMethod>();
				
				a = actions.next();
				
				this.buildDisabledActionsB();
				this.buildEnabledActionsB();
				this.buildTransitions();
			}
		}
	}

	private void buildDisabledActionsB() throws ParserException, IOException {
		Iterator<MoolMethod> it = mool_class_methods.iterator();
		MoolMethod m;
		List<BoolExpr> propositions;
		
		while(it.hasNext()) {
			m = it.next();
		
			propositions = new ArrayList<BoolExpr>();
			propositions.add((BoolExpr) m.getMethodRequires().getAssertion(true));
			propositions.add((BoolExpr) a.getMethodEnsures().getAssertion(true));
			
			if(!Prover.prove(ctx, propositions)) {
				disabled_actions.add(m);
			}
			
			propositions.remove(propositions.size()-1);
			propositions.remove(propositions.size()-1);
		}
		
		/*System.out.println("B- of " + a.getMethodName());
		Iterator<MoolMethod> aux_it = disabled_actions.iterator();
		
		while(aux_it.hasNext())
			System.out.println(aux_it.next().getMethodName());*/
	}
	
	private void buildEnabledActionsB() throws ParserException, IOException {
		Iterator<MoolMethod> it = mool_class_methods.iterator();
		MoolMethod m;
		List<BoolExpr> propositions;

		while(it.hasNext()) {
			m = it.next();
		
			propositions = new ArrayList<BoolExpr>();

			propositions.add(ctx.mkNot((BoolExpr) m.getMethodRequires().getAssertion(true)));
			propositions.add((BoolExpr) a.getMethodEnsures().getAssertion(true));
			
			
			
			if( disabled_actions.indexOf(m) == -1 && !Prover.prove(ctx, propositions)) {
				enabled_actions.add(m);
			}
			
			propositions.remove(propositions.size()-1);
			propositions.remove(propositions.size()-1);
		}
		
		/*System.out.println("B+ of " + a.getMethodName());
		Iterator<MoolMethod> aux_it = enabled_actions.iterator();
		
		while(aux_it.hasNext())
			System.out.println(aux_it.next().getMethodName());*/
	}
	
	private void buildTransitions() throws ParserException, IOException, TooManyTransitionsException {
		Iterator<MoolMethod> it = mool_class_methods.iterator();
		MoolMethod m;
		List<BoolExpr> propositions;
		
		List<State> candidate_states = calculateCandidateStates(mool_class_methods, disabled_actions, enabled_actions);
		
		/*
		Iterator<State> aux_it = candidate_states.iterator();
		
		while(aux_it.hasNext())
			System.out.println(aux_it.next());*/
		
		
		Iterator<State> candidate_states_it = candidate_states.iterator();
		
		List<State> next_states = new ArrayList<State>();
		
		while(candidate_states_it.hasNext()) {
			
			it = mool_class_methods.iterator();
			
			propositions = new ArrayList<BoolExpr>();
			
			propositions.add((BoolExpr) mool_class.getClassInvariant().getAssertion(true));
			
			B = candidate_states_it.next();
			
			if(a.changesSystemState() && A.isSameState(B)) {
				delta.add(new Transition(A, a, B, true));
			} else if(!a.changesSystemState()) {
				while(it.hasNext()) {
					m = it.next();
				
					if(B.hasAction(m) )
						propositions.add((BoolExpr) m.getMethodRequires().getAssertion(true));
					else
						propositions.add(ctx.mkNot((BoolExpr) m.getMethodRequires().getAssertion(true)));
				}
				
				if(a.getMethodType().equals("boolean"))
					propositions.add(Prover.unfold(ctx, (BoolExpr) a.getMethodEnsures().getAssertion(true)));
				else
					propositions.add((BoolExpr) a.getMethodEnsures().getAssertion(true));

				if(Prover.prove(ctx, propositions)) {
					next_states.add(B);
					
					if(next_states.size() > 2)
						throw new TooManyTransitionsException("Method " + a.getMethodName() + " has too many transitions"); 
					
					if(a.getMethodType().equals("boolean") && B.getActions().size() > 0) {
						
						BoolExpr ens = Prover.unfold(ctx, (BoolExpr) a.getMethodEnsures().getAssertion(true));
						
						//System.out.println("ensures: " + ens);
						BoolExpr left = (BoolExpr) ens.getArgs()[0];
						BoolExpr right = (BoolExpr) ens.getArgs()[1];
						
						delta.add(new Transition(A, a, B, Prover.getVariant(ctx, left, right, mool_class, B.getActions())));
					} else {
						delta.add(new Transition(A, a, B, true));
					}
				}
			}
		}
		
		/*
		Iterator<Transition> aux_it = delta.iterator();
		
		while(aux_it.hasNext())
			System.out.println(aux_it.next());*/
			
		
		Iterator<State> states_to_explore = next_states.iterator();
		
		while(states_to_explore.hasNext()) {
			
			
			State s = states_to_explore.next();
			
			Iterator<State> states_it = states.iterator();
			boolean to_add = true;
			
			while(states_it.hasNext() && to_add) {
				State g = states_it.next();
				if(g.isSameState(s)) {
					//System.out.println("States " + g + " and " + s + " are the same");
					to_add = false;
				} else {
					//System.out.println("States " + g + " and " + s + " are NOT the same");
				}
			}
			
			Iterator<State> w_it = w.iterator();
			while(w_it.hasNext() && to_add) {
				State g = w_it.next();
				if(g.isSameState(s)) {
					//System.out.println("States " + g + " and " + s + " are the same");
					to_add = false;
				} else {
					//System.out.println("States " + g + " and " + s + " are NOT the same");
				}
			}
			
			if(to_add)
				w.add(s);
		}
		
	}
	
	private static List<State> calculateCandidateStates(List<MoolMethod> methods, List<MoolMethod> disabled_actions, List<MoolMethod> enabled_actions) {
		
		//System.out.println("building candidates... " + disabled_actions.size() + ", " + enabled_actions.size());
		List<State> states = new ArrayList<State>();
		
		for(int a = 1; a <= methods.size(); a++ ) {
			Iterator<int[]> it = new Combinations(methods.size(), a).iterator();
			
			while(it.hasNext()) {
				int[] combs = it.next();
				State s = new State();

				for(int i = 0; i < combs.length; i++) {
					
					if(disabled_actions.indexOf(methods.get(combs[i])) > -1) {
						s = null; i = combs.length;
					} else
						s.addAction(methods.get(combs[i]));
					
				}
				
				if(s != null && (s.hasActions(enabled_actions) || enabled_actions.size() == 0)) {
					states.add(s);
				}
			}
		}
		
		if( enabled_actions.size() == 0 )
			states.add(new State());
		
		//System.out.println("I've build " + states.size());
		return states;
	}
}
