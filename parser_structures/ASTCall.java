package parser_structures;

import java.util.List;

import com.microsoft.z3.*;

import class_elements.MoolClass;
import class_elements.MoolField;
import class_elements.MoolMethod;
import helpers.MoolPrinter;
import java.util.Iterator;

public class ASTCall implements ASTNode {

	Context ctx;
    
	String l;
	String r;
	List<ASTNode> call_params;
	ASTNode body;
	MoolMethod m;
	MoolMethod caller;
	boolean is_negation;
	
    public ASTCall(Context ctx, String left, String right, List<ASTNode> call_params, boolean is_negation, MoolMethod caller, MoolMethod m) {
    	this.ctx = ctx;
    	this.l = left;
    	this.r = right;
    	this.call_params = call_params;
    	this.is_negation = is_negation;
    	this.caller = caller;
    	this.m = m;
    }
    
    public Expr getAssertion(boolean assertion_context) {

    	if(caller.getMethodParameter(l) != null && assertion_context) {
    		return ctx.mkBool(true);
    	}
    	
    	if(m == null) {
    		return ctx.mkBool(true);
    	}
  
    	ASTNode assertion = m.getMethodEnsures();
    	ASTNode assertion_left, assertion_right;
    	
        if (assertion instanceof ASTOr)
        {
          if (is_negation) assertion = ((ASTOr) assertion).getRight();
          else assertion = ((ASTOr) assertion).getLeft();
        }
        else
        {
          if (assertion instanceof ASTAnd)
          {
        	assertion_left = ((ASTAnd) assertion).getLeft();
        	assertion_right = ((ASTAnd) assertion).getRight();
            if (assertion_left instanceof ASTOr)
            {
            	if(is_negation)
            		assertion = new ASTAnd(((ASTOr) assertion_left).getRight(), assertion_right, ctx);
            	else
            		assertion = new ASTAnd(((ASTOr) assertion_left).getLeft(), assertion_right, ctx);
            }
            else if (assertion_right instanceof ASTOr)
            {
            	if(is_negation)
            		assertion = new ASTAnd(assertion_left, ((ASTOr) assertion_right).getRight(), ctx);
            	else
            		assertion = new ASTAnd(assertion_left, ((ASTOr) assertion_right).getLeft(), ctx);
            }
          }
        }
        
        if (is_negation) assertion = new ASTNot(assertion, ctx);
        
        return assertion.getAssertion(assertion_context); 
    }
    
    public MoolMethod getMethodCalled() {
    	return m;
    }
    
    public Expr getFieldAssertion(String field) {
    	
    	if(field.equals(l)) {
    		return getAssertion(false);
    	}
    	
    	return null;//ctx.mkBool(true);
    }
    
    public String toString() {
    	return l + "." + r + "()";
    }
    
    public void changeFieldState(List<MoolField> params, MoolClass mool_class, MoolMethod mool_method) {
    	return;
    }
    
    public void printToFile(MoolPrinter printer) {
    	if(l.equals("this"))
    		printer.print(r + "(");	
    	else
    		printer.print(l + "." + r + "(");
    	
    	printer.deactivateIndentation();
    	
    	Iterator<ASTNode> call_params_it = call_params.iterator();
		
		while(call_params_it.hasNext()) {
			printer.print(call_params_it.next().toString());
			
			if(call_params_it.hasNext())
				printer.print(", ");
		}
		
		printer.print(")");
		
		//printer.activateIndentation();
    }
}
