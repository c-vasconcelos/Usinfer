package class_elements;

import java.util.Iterator;
import java.util.List;

import com.microsoft.z3.BoolExpr;

import algorithm_structures.UsageState;
import helpers.MoolPrinter;
import parser_structures.ASTIf;
import parser_structures.ASTNode;
import parser_structures.ASTSeq;

public class MoolMethod {

	private boolean is_sync;
	private String method_type;
	private UsageState method_type_usage_state;
	private String method_name;
	//private String method_requires;
	//private String method_ensures;
	//private String method_body;
	private ASTNode method_body;
	
	//private BoolExpr method_requires;
		//private BoolExpr method_ensures;
	private ASTNode method_requires;
	private ASTNode method_ensures;

	private List<MoolField> method_params;
	private List<MoolField> method_variables;
	
	public MoolMethod(String method_name, String method_type, boolean is_sync, ASTNode method_requires, ASTNode method_ensures, ASTNode method_body, List<MoolField> params, List<MoolField> variables) {
		this.method_name = method_name;
		this.method_type = method_type;
		this.is_sync = is_sync;
		this.method_requires = method_requires;
		this.method_ensures = method_ensures;
		this.method_body = method_body;
		
		this.method_params = params;
		this.method_variables = variables;
	}
	
	public String getMethodName() {
		return method_name;
	}
	
	public String getMethodType() {
		return method_type;
	}
	
	public boolean isSync() {
		return is_sync;
	}
	
	public boolean hasPrimitiveType() {
		
		switch(this.method_type) {
		case "int":
			return true;
		case "void":
			return true;
		case "boolean":
			return true;
		case "string":
			return true;
		default:
			return false;
		}
	}
	
	public void setMethodTypeUsageState(UsageState method_type_usage_state) {
		this.method_type_usage_state = method_type_usage_state;
	}
	
	public UsageState getMethodTypeUsageState() {
		return method_type_usage_state;
	}
	public ASTNode getMethodRequires() {
		return method_requires;
	}
	
	public ASTNode getMethodEnsures() {
		return method_ensures;
	}
	
	public void setMethodRequires(ASTNode method_requires) {
		this.method_requires = method_requires;
	}
	
	public void setMethodEnsures(ASTNode method_ensures) {
		this.method_ensures = method_ensures;
	}
	
	public ASTNode getMethodBody() {
		return method_body;
	}
	
	public List<MoolField> getMethodParams() {
		return method_params;
	}
	
	public MoolField getMethodParameter(String parameter) {
		
		Iterator<MoolField> it = method_params.iterator();
		MoolField f;
		while(it.hasNext()) {
			f = it.next();
			
			if(f.getFieldName().equals(parameter))
				return f;
		}
		
		return null;
	}
	
	public MoolField getMethodVariable(String variable) {
		
		Iterator<MoolField> it = method_variables.iterator();
		MoolField f;
		while(it.hasNext()) {
			f = it.next();
			
			if(f.getFieldName().equals(variable))
				return f;
		}
		
		return null;
	}
	
	public List<MoolField> getMethodVariables() {
		return method_variables;
	}
	
	public boolean changesSystemState() {
		return method_requires.toString().equals(method_ensures.toString());
	}
	
	public void printToFile(MoolPrinter printer) {
		
		if(is_sync) {
			printer.print("sync ");
			printer.deactivateIndentation();
		}
		
		if(method_type_usage_state == null)
			printer.print(method_type + " " + method_name + "(");
		else
			printer.print(method_type + "[" + method_type_usage_state.getUsageStateId() + "] " + method_name + "(");
		
		
		
		printer.deactivateIndentation();
		
		Iterator<MoolField> fields_it = method_params.iterator();
		
		while(fields_it.hasNext()) {
			MoolField f = fields_it.next();
			
			if(f.getFieldUsageState() == null)
				printer.print(f.getFieldType() + " " + f.getFieldName());
			else
				printer.print(f.getFieldType() + "[" + f.getFieldUsageState().getUsageStateId() + "] " + f.getFieldName());
			
			if(fields_it.hasNext())
				printer.print(", ");
		}
			
		printer.print(") {\n");
		
		printer.activateIndentation();
		
		printer.increaseIndentation();
		
		method_body.printToFile(printer);
		printer.deactivateIndentation();
		
		if(!(method_body instanceof ASTIf) && !(method_body instanceof ASTSeq))
			printer.print(";");
		
		printer.print("\n");
		printer.activateIndentation();
		printer.decreaseIndentation();
		
		printer.printLine("}");
	}
}
