package parser_structures;

import java.util.List;

import com.microsoft.z3.*;

import class_elements.MoolClass;
import class_elements.MoolField;
import class_elements.MoolMethod;
import helpers.MoolPrinter;

public class ASTField implements ASTNode {

    String field;
    String type;
    Context ctx;
    MoolField pointer;
    boolean is_local;
    boolean is_initialized;
    
    public ASTField(String f, String t, boolean is_local, MoolField pointer, Context ctx) {
        field = f;
        type = t;
        this.ctx = ctx;
        this.is_initialized = true;
        this.is_local = is_local;
        this.pointer = pointer;
    }

    public Expr getAssertion(boolean assertion_context) {
    	if (type.equals("int")) 
    		return ctx.mkIntConst(field);
        else if (type.equals("boolean")) 
        	return ctx.mkBoolConst(field);
        else if (type.equals("String")) 
        	return ctx.mkIntConst(field);
        else
        	return ctx.mkBoolConst(field);
    }
    
    public Expr getFieldAssertion(String field) {
    	return null;
    }
    
    public String toString() {
    	return field;
    }
    
    public void changeFieldState(List<MoolField> params, MoolClass mool_class, MoolMethod mool_method) {
    	return;
    }
    
    public void printToFile(MoolPrinter printer) {
    	if(is_local) {
    		if(pointer.getFieldUsageState() != null)
    			printer.print(type + "[" + pointer.getFieldUsageState().getUsageStateId() + "] " + field);
    		else
    			printer.print(type + " " + field);
    	} else
    		printer.print(field);
    }
}
