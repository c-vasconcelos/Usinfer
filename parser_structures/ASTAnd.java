package parser_structures;

import java.util.List;

import com.microsoft.z3.*;

import class_elements.MoolClass;
import class_elements.MoolField;
import class_elements.MoolMethod;
import helpers.MoolPrinter;

public class ASTAnd implements ASTNode {

    ASTNode left, right;
    Context ctx;
    
    public ASTAnd(ASTNode l, ASTNode r, Context ctx) {
        left = l;
        right = r;
        this.ctx = ctx;
    }
    
    public ASTNode getLeft() {
    	return left;
    }
    
    public ASTNode getRight() {
    	return right;
    }
    
    public Expr getAssertion(boolean assertion_context) {
    	return ctx.mkAnd((BoolExpr) left.getAssertion(assertion_context), (BoolExpr) right.getAssertion(assertion_context));
    }
    
    public Expr getFieldAssertion(String field) {
    	
    	BoolExpr b1 = (BoolExpr) left.getFieldAssertion(field);
    	BoolExpr b2 = (BoolExpr) right.getFieldAssertion(field);
    	
    	if(b1 != null) {
    		if(b2 != null)
    			return ctx.mkAnd(b1, b2);
    		else
    			return b1;
    	} else {
    		if(b2 != null)
    			return b2;
    	}
    	
    	return null;
    }
    
    public String toString() {
    	return left.toString() + " && " + right.toString();
    }
    
    public void changeFieldState(List<MoolField> params, MoolClass mool_class, MoolMethod mool_method) {
    	return;
    }
    
    public void printToFile(MoolPrinter printer) {
    	printer.printLine("This is an AST node");
    }
}
