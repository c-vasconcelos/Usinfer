package parser_structures;

import java.util.List;

import com.microsoft.z3.*;

import class_elements.MoolClass;
import class_elements.MoolField;
import class_elements.MoolMethod;
import helpers.MoolPrinter;
import java.util.Iterator;

public class ASTAssign implements ASTNode {

	Context ctx;
    
	String l;
	ASTNode r;
	
    public ASTAssign(Context ctx, String left, ASTNode right) {
    	this.ctx = ctx;
    	this.l = left;
    	this.r = right;
    }

    public Expr getAssertion(boolean assertion_context) {
    	return ctx.mkBool(true);
    }
    
    public Expr getFieldAssertion(String field) {
    	return ctx.mkBool(true);
    }
    
    public String toString() {
    	return l + " = " + r.toString();
    }
    
    public void changeFieldState(List<MoolField> params, MoolClass mool_class, MoolMethod mool_method) {
    	
    	
    	Iterator<MoolField> params_it = params.iterator();
    	MoolField f;
    	
    	if(r instanceof ASTField) {
    		
    		while(params_it.hasNext()) {
        		f = params_it.next();
        		if(f.getFieldName().equals(r.toString()) && f.getFieldUsageState() != null) {
        			mool_class.getField(l).setFieldUsageState(f.getFieldUsageState());
        		}
        	}
    	} else if(r instanceof ASTCall) {
    		MoolMethod m = ((ASTCall) r).getMethodCalled();
    		
    		if(mool_class.getField(l) != null && m.getMethodTypeUsageState() != null)
    			mool_class.getField(l).setFieldUsageState(m.getMethodTypeUsageState());
    		else if(mool_method.getMethodVariable(l) != null && m.getMethodTypeUsageState() != null) {
    			
    			mool_method.getMethodVariable(l).setFieldUsageState(m.getMethodTypeUsageState());
    		}
    	}
    }
    
    public void printToFile(MoolPrinter printer) {
    	printer.print(l + " = ");
    	
    	printer.deactivateIndentation();
    	
    	r.printToFile(printer);
    	
    	printer.activateIndentation();
    }
}
