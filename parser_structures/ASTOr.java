package parser_structures;

import java.util.List;

import com.microsoft.z3.*;

import class_elements.MoolClass;
import class_elements.MoolField;
import class_elements.MoolMethod;
import helpers.MoolPrinter;

public class ASTOr implements ASTNode {

    ASTNode left, right;
    Context ctx;
    
    public ASTOr(ASTNode l, ASTNode r, Context ctx) {
        left = l;
        right = r;
        this.ctx = ctx;
    }
    
    public ASTNode getLeft() {
    	return left;
    }
    
    public ASTNode getRight() {
    	return right;
    }
    
    public Expr getAssertion(boolean assertion_context) {
    	return ctx.mkOr((BoolExpr) left.getAssertion(assertion_context), (BoolExpr) right.getAssertion(assertion_context));
    }

    public Expr getFieldAssertion(String field) {
    	return ctx.mkBool(true);
    }
    
    public String toString() {
    	return left.toString() + " || " + right.toString();
    }
    
    public void changeFieldState(List<MoolField> params, MoolClass mool_class, MoolMethod mool_method) {
    	return;
    }
    
    public void printToFile(MoolPrinter printer) {
    	printer.printLine("This is an AST node");
    }
}
