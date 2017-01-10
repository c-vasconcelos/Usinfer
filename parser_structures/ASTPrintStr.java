package parser_structures;

import java.util.List;

import com.microsoft.z3.*;

import class_elements.MoolClass;
import class_elements.MoolField;
import class_elements.MoolMethod;
import helpers.MoolPrinter;

public class ASTPrintStr implements ASTNode {

    String str;
    Context ctx;

    public ASTPrintStr(String str, Context ctx) {
        this.str = str;
        this.ctx = ctx;
    }

	@Override
	public Expr getAssertion(boolean assertion_context) {
		return ctx.mkInt(str.length());
	}
	
	public Expr getFieldAssertion(String field) {
    	return ctx.mkBool(true);
    }
	
	public String toString() {
		return "printStr(" + str + ")";
	}
	
	public void changeFieldState(List<MoolField> params, MoolClass mool_class, MoolMethod mool_method) {
    	return;
    }
	
	public void printToFile(MoolPrinter printer) {
    	printer.print("printStr(" + str + ")");
    }
}
