package parser_structures;

import java.util.List;

import com.microsoft.z3.*;

import class_elements.MoolClass;
import class_elements.MoolField;
import class_elements.MoolMethod;
import helpers.MoolPrinter;

public class ASTInteger implements ASTNode {

    int val;
    Context ctx;
    
    public ASTInteger(int n, Context ctx) {
        val = n;
        this.ctx = ctx;
    }

    public Expr getAssertion(boolean assertion_context) {
    	return ctx.mkInt(val);
    }
    
    public Expr getFieldAssertion(String field) {
    	return ctx.mkBool(true);
    }
    
    public String toString() {
    	return Integer.toString(val);
    }
    
    public void changeFieldState(List<MoolField> params, MoolClass mool_class, MoolMethod mool_method) {
    	return;
    }
    
    public void printToFile(MoolPrinter printer) {
    	printer.print(Integer.toString(val));
    }
}
