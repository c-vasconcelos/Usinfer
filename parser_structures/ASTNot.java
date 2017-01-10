package parser_structures;

import java.util.List;

import com.microsoft.z3.*;

import class_elements.MoolClass;
import class_elements.MoolField;
import class_elements.MoolMethod;
import helpers.MoolPrinter;

public class ASTNot implements ASTNode {

    ASTNode node;
    Context ctx;
    
    public ASTNot(ASTNode n, Context ctx) {
        node = n;
        this.ctx = ctx;
    }
    
    public Expr getAssertion(boolean assertion_context) {
    	return ctx.mkNot((BoolExpr) node.getAssertion(assertion_context));
    }
    
    public Expr getFieldAssertion(String field) {
    	return ctx.mkBool(true);
    }
    
    public String toString() {
    	return "!" + node.toString();
    }
    
    public void changeFieldState(List<MoolField> params, MoolClass mool_class, MoolMethod mool_method) {
    	return;
    }
    
    public void printToFile(MoolPrinter printer) {
    	printer.print("!");
    	
    	printer.deactivateIndentation();
    	node.printToFile(printer);
    	printer.activateIndentation();
    }
}
