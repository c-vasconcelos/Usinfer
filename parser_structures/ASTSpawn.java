package parser_structures;

import java.util.List;

import com.microsoft.z3.*;

import class_elements.MoolClass;
import class_elements.MoolField;
import class_elements.MoolMethod;
import helpers.MoolPrinter;

public class ASTSpawn implements ASTNode {

    ASTNode expression;
    Context ctx;
    
    public ASTSpawn(ASTNode expression, Context ctx) {
        this.expression = expression;
        this.ctx = ctx;
    }
   
    public Expr getAssertion(boolean assertion_context) {
    	return expression.getAssertion(assertion_context);
    }
    
    public Expr getFieldAssertion(String field) {
    	return ctx.mkBool(true);
    }
    
    public String toString() {
    	return "spawn " + expression.toString();
    }
    
    public void changeFieldState(List<MoolField> params, MoolClass mool_class, MoolMethod mool_method) {
    	expression.changeFieldState(params, mool_class, mool_method);
    }
    
    public void printToFile(MoolPrinter printer) {
    	printer.print("spawn {\n");
    	
    	printer.activateIndentation();
    	printer.increaseIndentation();
    	expression.printToFile(printer);
    	printer.print("\n");
    	printer.activateIndentation();
    	printer.decreaseIndentation();
    	printer.print("}");
    }
}
