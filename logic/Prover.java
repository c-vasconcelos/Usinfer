package logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sf.tweety.commons.ParserException;
import net.sf.tweety.logics.pl.parser.PlParser;
import net.sf.tweety.logics.pl.sat.Sat4jSolver;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;

import com.microsoft.z3.*;

import algorithm_structures.State;
import class_elements.MoolClass;
import class_elements.MoolMethod;

public class Prover {

	public static boolean prove(Context ctx, List<BoolExpr> propositions) throws ParserException, IOException {

		Iterator<BoolExpr> it_prepositions = propositions.iterator();

		Solver solver = ctx.mkSolver();
		BoolExpr p;
		while(it_prepositions.hasNext()) {
			p = it_prepositions.next();

			//proposition += "(" + p + ")";

			solver.add(p);

			//System.out.println(p);
		}

		//PropositionalFormula formula = (PropositionalFormula) parser.parseFormula(proposition);

				//Sat4jSolver solver = new Sat4jSolver();

		//System.out.println(solver.check());
        
		return solver.check().toString().equals("SATISFIABLE");
	}
	
	public static boolean getVariant(Context ctx, BoolExpr left, BoolExpr right, MoolClass mool_class, List<MoolMethod> possible_actions) throws ParserException, IOException {
		
		Iterator<MoolMethod> state_actions_it = possible_actions.iterator();
		
		while(state_actions_it.hasNext()) {
			
			MoolMethod a = state_actions_it.next();
			
			ArrayList<BoolExpr> propositions2 = new ArrayList<BoolExpr>();
			ArrayList<BoolExpr> propositions3 = new ArrayList<BoolExpr>();
			propositions2.add(left);
			propositions3.add(right);
			
			BoolExpr req = (BoolExpr) mool_class.getMethod(a.getMethodName()).getMethodRequires().getAssertion(true);
			
			propositions2.add(req);
			propositions3.add(req);
			
			if(prove(ctx, propositions2) && !prove(ctx, propositions3)) {
				return true;
			} else if(!prove(ctx, propositions2) && prove(ctx, propositions3))
				return false;
		}
		
		return false;
	}
	
	public static BoolExpr unfold(Context ctx, BoolExpr b) {
		
		if(b.isAnd()) {
			Expr b1 = b.getArgs()[0];
			Expr b2 = b.getArgs()[1];
			
			if(b1.isOr()) {
				b = ctx.mkOr(
						ctx.mkAnd(
								(BoolExpr) b1.getArgs()[0], 
								(BoolExpr) b2), 
						ctx.mkAnd(
								(BoolExpr) b1.getArgs()[1], 
								(BoolExpr) b2));
			} else if(b2.isOr()) {
				b = ctx.mkOr(
						ctx.mkAnd(
								(BoolExpr) b1,
								(BoolExpr) b2.getArgs()[0]), 
						ctx.mkAnd(
								(BoolExpr) b1,
								(BoolExpr) b2.getArgs()[1]));
			} else if(b1.isImplies()) {
				//b = ctx.mkAnd(unfold(ctx, (BoolExpr) b1), (BoolExpr) b2);
				//System.out.println(b);
				b = unfold(ctx, ctx.mkAnd( unfold(ctx, (BoolExpr) b1), (BoolExpr) b2));
			} else if(b2.isImplies()) {
				//b = ctx.mkAnd((BoolExpr) b1, unfold(ctx, (BoolExpr) b2));
				b = unfold(ctx, ctx.mkAnd( unfold(ctx, (BoolExpr) b1), (BoolExpr) b2));
			}
		} else if(b.isImplies()) {
			
			BoolExpr b1 = (BoolExpr) b.simplify().getArgs()[0];
			BoolExpr b2 = (BoolExpr) b.simplify().getArgs()[1];
			
	        //BoolExpr l = (BoolExpr) b3.getArgs()[0];
	        //BoolExpr r = (BoolExpr) b3.getArgs()[1];
	        
	        
	        //System.out.println(ctx.mkAnd((BoolExpr) ctx.mkNot(l).simplify(), r));
	        
	        //System.out.println(ctx.mkAnd((BoolExpr) ctx.mkNot(r).simplify(), l));
	        b = ctx.mkOr(ctx.mkAnd((BoolExpr) ctx.mkNot(b1).simplify(), b2), ctx.mkAnd((BoolExpr) ctx.mkNot(b2).simplify(), b1));
		
	        //System.out.println("UNFOLDED: " + b);
	        
	        
		}
		
		
		return b;
	}
}
