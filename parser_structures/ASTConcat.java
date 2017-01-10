package parser_structures;

import java.util.List;

import com.microsoft.z3.*;

import class_elements.MoolClass;
import class_elements.MoolField;
import class_elements.MoolMethod;
import helpers.MoolPrinter;

public class ASTConcat implements ASTNode {

    ASTNode left, right;
    Context ctx;
    
    public ASTConcat(ASTNode l, ASTNode r, Context ctx) {
        left = l;
        right = r;
        this.ctx = ctx;
    }
    
    public Expr getAssertion(boolean assertion_context) {
    	return ctx.mkBool(true);
    }
    
    public Expr getFieldAssertion(String field) {
    	return ctx.mkBool(true);
    }

    public String toString() {
    	return left + " ++ " + right;
    }
    
    public void changeFieldState(List<MoolField> params, MoolClass mool_class, MoolMethod mool_method) {
    	return;
    }
    
    public void printToFile(MoolPrinter printer) {
    	left.printToFile(printer);
    	
    	printer.deactivateIndentation();
    	
    	printer.print(" ++ ");
    	right.printToFile(printer);
    	printer.activateIndentation();
    }
}
