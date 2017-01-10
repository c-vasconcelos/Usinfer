package parser_structures;

import java.util.List;

import com.microsoft.z3.*;

import class_elements.MoolClass;
import class_elements.MoolField;
import class_elements.MoolMethod;
import helpers.MoolPrinter;

public class ASTImplies implements ASTNode {

    ASTNode left, right;
    Context ctx;
    
    public ASTImplies(ASTNode l, ASTNode r, Context ctx) {
        left = l;
        right = r;
        this.ctx = ctx;
    }
    
    public Expr getAssertion(boolean assertion_context) {
    	//return ctx.mkImplies((BoolExpr) left.getAssertion(), (BoolExpr) right.getAssertion());
    	BoolExpr b1 = (BoolExpr) left.getAssertion(assertion_context);
		BoolExpr b2 = (BoolExpr) right.getAssertion(assertion_context);
		
		
        //BoolExpr l = (BoolExpr) b3.getArgs()[0];
        //BoolExpr r = (BoolExpr) b3.getArgs()[1];
        
        
        //System.out.println(ctx.mkAnd((BoolExpr) ctx.mkNot(l).simplify(), r));
        
        //System.out.println(ctx.mkAnd((BoolExpr) ctx.mkNot(r).simplify(), l));
        //return ctx.mkOr(ctx.mkAnd(b1, b2), ctx.mkAnd((BoolExpr) ctx.mkNot(b1).simplify(), (BoolExpr) ctx.mkNot(b2).simplify()));
		return ctx.mkImplies(b1, b2);
    }
    
    public Expr getFieldAssertion(String field) {
    	return ctx.mkBool(true);
    }
    
    public String toString() {
    	return left.toString() + " -> " + right.toString();
    }
    
    public void changeFieldState(List<MoolField> params, MoolClass mool_class, MoolMethod mool_method) {
    	return;
    }
    
    public void printToFile(MoolPrinter printer) {
    	printer.printLine("This is an AST node");
    }
}
