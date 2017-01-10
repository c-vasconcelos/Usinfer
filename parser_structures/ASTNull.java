package parser_structures;

import java.util.List;

import com.microsoft.z3.*;

import class_elements.MoolClass;
import class_elements.MoolField;
import class_elements.MoolMethod;
import helpers.MoolPrinter;

public class ASTNull implements ASTNode {

	Context ctx;
	
    public ASTNull(Context ctx) {
    	this.ctx = ctx;
    }

    public Expr getAssertion(boolean assertion_context) {
    	return ctx.mkBool(false);
    }
    
    public Expr getFieldAssertion(String field) {
    	return ctx.mkBool(false);
    }
    
    public String toString() {
    	return "null";
    }
    
    public void changeFieldState(List<MoolField> params, MoolClass mool_class, MoolMethod mool_method) {
    	return;
    }
    
    public void printToFile(MoolPrinter printer) {
    	printer.print("null");
    }
}