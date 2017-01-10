package parser_structures;

import java.util.List;

import com.microsoft.z3.*;

import class_elements.MoolClass;
import class_elements.MoolField;
import class_elements.MoolMethod;
import helpers.MoolPrinter;

public class ASTSeq implements ASTNode {

    ASTNode exp1;
    ASTNode exp2;
    Context ctx;
    
    public ASTSeq(ASTNode exp1, ASTNode exp2, Context ctx) {
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.ctx = ctx;
    }

    public Expr getAssertion(boolean assertion_context) {
    	return exp2.getAssertion(assertion_context);
    }
    
    public ASTNode getExp2() {
    	return exp2;
    }
    
    public String toString() {
    	
    	String str = exp1.toString() + ";";// + ";\n" + exp2.toString();
    	
    	if(exp2 instanceof ASTSeq)
    		str += "\n" + exp2.toString();
    	
    	else
    		str += "\n\t" + exp2.toString();
    	
    	return str;
    }
    
    public Expr getFieldAssertion(String field) {
    	return ctx.mkBool(true);
    }
    
    public void changeFieldState(List<MoolField> params, MoolClass mool_class, MoolMethod mool_method) {
    	exp1.changeFieldState(params, mool_class, mool_method);
    	exp2.changeFieldState(params, mool_class, mool_method);
    }
    
    public void printToFile(MoolPrinter printer) {
    	//printer.printLine("This is an AST node");
    	exp1.printToFile(printer);
    	
    	printer.deactivateIndentation();
    	
    	if(exp1 instanceof ASTIf || exp1 instanceof ASTSpawn)
    		printer.print("\n");
    	else
    		printer.print(";\n");
    		
    	printer.activateIndentation();
    	
    	exp2.printToFile(printer);
    	
    	printer.deactivateIndentation();
    	
    	if(!(exp2 instanceof ASTSeq) && !(exp2 instanceof ASTIf) && !(exp2 instanceof ASTSpawn))
    		printer.print(";");
    }
}
