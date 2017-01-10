package parser_structures;

import java.util.List;

import com.microsoft.z3.*;

import class_elements.MoolClass;
import class_elements.MoolField;
import class_elements.MoolMethod;
import helpers.MoolPrinter;

public interface ASTNode {

	Expr getAssertion(boolean assertion_context);

	Expr getFieldAssertion(String field);
	
	void changeFieldState(List<MoolField> params, MoolClass mool_class, MoolMethod mool_method);
	
	void printToFile(MoolPrinter printer);
}
