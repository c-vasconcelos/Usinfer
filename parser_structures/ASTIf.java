package parser_structures;

import java.util.List;

import com.microsoft.z3.*;

import class_elements.MoolClass;
import class_elements.MoolField;
import class_elements.MoolMethod;
import helpers.MoolPrinter;

public class ASTIf implements ASTNode {

    ASTNode cnd, e1, e2;
    Context ctx;
    
    public ASTIf(ASTNode cnd, ASTNode e1, Context ctx) {
        this.cnd = cnd;
        this.e1 = e1;
        this.ctx = ctx;
    }
    
    public ASTIf(ASTNode cnd, ASTNode e1, ASTNode e2, Context ctx) {
        this.cnd = cnd;
        this.e1 = e1;
    	this.e2 = e2;
    	this.ctx = ctx;
    }

    public Expr getAssertion(boolean assertion_context) {
    	if( e1 != null)
    		return ctx.mkOr((BoolExpr) e1.getAssertion(assertion_context), (BoolExpr) e2.getAssertion(assertion_context));
    	
    	return e1.getAssertion(assertion_context);
    }
    
    public Expr getFieldAssertion(String field) {
    	return ctx.mkBool(true);
    }
    
    public String toString() {
    	return "if (" + cnd.toString() + ") {\n			" + e1.toString() + "\n		} else { \n			" + e2.toString() + "\n	}";
    }
    
    public void changeFieldState(List<MoolField> params, MoolClass mool_class, MoolMethod mool_method) {
    	cnd.changeFieldState(params, mool_class, mool_method);
    	e1.changeFieldState(params, mool_class, mool_method);
    	
    	if(e2 != null)
    		e2.changeFieldState(params, mool_class, mool_method);
    }
    
    public void printToFile(MoolPrinter printer) {
    	
    	printer.print("if(");
    	printer.deactivateIndentation();
    	cnd.printToFile(printer);
    	printer.print(") {\n");
    	
    	printer.activateIndentation();
    	printer.increaseIndentation();
    	e1.printToFile(printer);
    	printer.deactivateIndentation();
    	
    	if(!(e1 instanceof ASTSeq) && !(e1 instanceof ASTIf) && !(e1 instanceof ASTSpawn))
    		printer.print(";");
    	
    	printer.print("\n");
    	printer.activateIndentation();
    	printer.decreaseIndentation();
    	
    	if(e2 != null) {
	    	printer.print("} else {\n");
	    	
	    	printer.activateIndentation();
	    	printer.increaseIndentation();
	    	e2.printToFile(printer);
	    	printer.deactivateIndentation();
	    	if(!(e2 instanceof ASTSeq) && !(e2 instanceof ASTIf) && !(e2 instanceof ASTSpawn))
	    		printer.print(";");
	    	
	    	printer.print("\n");
	    	printer.activateIndentation();
	    	printer.decreaseIndentation();
	    	printer.print("}");
    	} else {
    		printer.print("}");
    	}
    	
    	
    }
}
