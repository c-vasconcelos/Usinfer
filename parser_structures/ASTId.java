package parser_structures;

import java.util.List;

import com.microsoft.z3.*;

import class_elements.MoolClass;
import class_elements.MoolField;
import class_elements.MoolMethod;
import helpers.MoolPrinter;

public class ASTId implements ASTNode {

    String id;
    Context ctx;

    public ASTId(String id, Context ctx) {
        this.id = id;
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
		return id;
	}
	
	public void changeFieldState(List<MoolField> params, MoolClass mool_class, MoolMethod mool_method) {
    	return;
    }
	
	public void printToFile(MoolPrinter printer) {
    	printer.print(id);
    }
}
