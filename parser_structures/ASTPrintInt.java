package parser_structures;

import java.util.List;

import com.microsoft.z3.*;

import class_elements.MoolClass;
import class_elements.MoolField;
import class_elements.MoolMethod;
import helpers.MoolPrinter;

public class ASTPrintInt implements ASTNode {

    String integer;
    Context ctx;

    public ASTPrintInt(String integer, Context ctx) {
        this.integer = integer;
        this.ctx = ctx;
    }

	@Override
	public Expr getAssertion(boolean assertion_context) {
		return ctx.mkInt(0);
	}
	
	public Expr getFieldAssertion(String field) {
    	return ctx.mkBool(true);
    }
	
	public String toString() {
		return "printStr(" + integer + ")";
	}
	
	public void changeFieldState(List<MoolField> params, MoolClass mool_class, MoolMethod mool_method) {
    	return;
    }
	
	public void printToFile(MoolPrinter printer) {
    	printer.print("printInt(" + integer + ")");
    }
}
