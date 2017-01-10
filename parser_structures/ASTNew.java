package parser_structures;

import java.util.Iterator;
import java.util.List;

import com.microsoft.z3.*;

import class_elements.MoolClass;
import class_elements.MoolField;
import class_elements.MoolMethod;
import helpers.MoolPrinter;

public class ASTNew implements ASTNode {

    String id;
    List<ASTNode> call_params;
    Context ctx;

    public ASTNew(String id, List<ASTNode> call_params, Context ctx) {
        this.id = id;
        this.call_params = call_params;
        this.ctx = ctx;
    }

	@Override
	public Expr getAssertion(boolean assertion_context) {
		return ctx.mkBool(true);
	}
	
	public Expr getFieldAssertion(String field) {
    	return ctx.mkBool(true);
    }
	
	public String toString() {
		return "new " + id + "()";
	}
	
	public void changeFieldState(List<MoolField> params, MoolClass mool_class, MoolMethod mool_method) {
    	return;
    }
	
	public void printToFile(MoolPrinter printer) {
    	printer.print("new " + id + "(");
    	
    	printer.deactivateIndentation();
    	
    	Iterator<ASTNode> call_params_it = call_params.iterator();
		
		while(call_params_it.hasNext()) {
			printer.print(call_params_it.next().toString());
			
			if(call_params_it.hasNext())
				printer.print(", ");
		}
		
		printer.print(")");
    }
}
