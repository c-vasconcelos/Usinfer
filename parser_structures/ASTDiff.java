package parser_structures;

import java.util.List;

import com.microsoft.z3.*;

import class_elements.MoolClass;
import class_elements.MoolField;
import class_elements.MoolMethod;
import helpers.MoolPrinter;

public class ASTDiff implements ASTNode {

    ASTNode left, right;
    Context ctx;
    
    public ASTDiff(ASTNode l, ASTNode r, Context ctx) {
        left = l;
        right = r;
        this.ctx = ctx;
    }

    public Expr getAssertion(boolean assertion_context) {
    	
    	if(left instanceof ASTNull) {
    		if(right instanceof ASTNull)
    			return ctx.mkBool(true);
    		else {
    			return right.getAssertion(assertion_context);
    		}
    	} else if(right instanceof ASTNull) {
    		return left.getAssertion(assertion_context);
    	}
    		
    	return ctx.mkNot(ctx.mkEq((ArithExpr) left.getAssertion(assertion_context), (ArithExpr) right.getAssertion(assertion_context)));
    }
    
    public Expr getFieldAssertion(String field) {
    	return ctx.mkBool(true);
    }
    
    public String toString() {
    	return left + " != " + right;
    }
    
    public void changeFieldState(List<MoolField> params, MoolClass mool_class, MoolMethod mool_method) {
    	return;
    }
    
    public void printToFile(MoolPrinter printer) {
left.printToFile(printer);
    	
    	printer.deactivateIndentation();
    	
    	printer.print(" != ");
    	
    	right.printToFile(printer);
    }
}