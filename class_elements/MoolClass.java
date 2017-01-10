package class_elements;

import java.util.Iterator;
import java.util.List;

import com.microsoft.z3.BoolExpr;

import algorithm_structures.Usage;
import helpers.MoolPrinter;
import parser_structures.ASTNode;

public class MoolClass {

	private String class_name;
	private ASTNode class_invariant;
	private ASTNode class_initial_condition;
		
	private boolean is_unrestricted;
	
	private MoolMethod class_constructor;
	
	private List<MoolMethod> class_methods;
	
	private List<MoolField> class_fields;
	
	private Usage class_usage;
	
	public MoolClass(String class_name, boolean is_unrestricted, ASTNode class_invariant, ASTNode class_initial_condition, String constructor_name, ASTNode constructor_body, List<MoolMethod> methods, List<MoolField> fields, List<MoolField> constructor_params, List<MoolField> constructor_variables) {
		this.class_name = class_name;
		this.is_unrestricted = is_unrestricted;
		this.class_invariant = class_invariant;
		this.class_initial_condition = class_initial_condition;
		
		class_constructor = new MoolMethod(constructor_name, "void", false, class_invariant, class_initial_condition, constructor_body, constructor_params, constructor_variables);
		
		this.class_fields = fields;
		this.class_methods = methods;
		
		this.class_usage = null;
	}
	
	public String getClassName() {
		return class_name;
	}
	
	public boolean isUnrestricted() {
		return is_unrestricted;
	}
	
	public ASTNode getClassInvariant() {
		return class_invariant;
	}
	
	public ASTNode getClassInitialCondition() {
		return class_initial_condition;
	}
	
	public void setClassInvariant(ASTNode class_invariant) {
		this.class_invariant = class_invariant;
	}
	
	public void setClassInitialCondition(ASTNode class_initial_condition) {
		this.class_initial_condition = class_initial_condition;
	}
	
	public MoolMethod getClassConstructor() {
		return class_constructor;
	}
	
	public MoolMethod getMethod(String action) {
		
		Iterator<MoolMethod> it = class_methods.iterator();
		MoolMethod m;
		while(it.hasNext()) {
			m = it.next();
			
			if(m.getMethodName().equals(action))
				return m;
		}
		
		return null;
	}

	public List<MoolMethod> getClassMethods() {
		return class_methods;
	}
	
	public List<MoolField> getClassFields() {
		return class_fields;
	}
	
	public MoolField getField(String field) {
		
		Iterator<MoolField> it = class_fields.iterator();
		MoolField f;
		while(it.hasNext()) {
			f = it.next();
			
			if(f.getFieldName().equals(field))
				return f;
		}
		
		return null;
	}
	
	public void setUsage(Usage class_usage) {
		this.class_usage = class_usage;
	}
	
	public Usage getUsage() {
		return class_usage;
	}
	
	public void printToFile(MoolPrinter printer) {
		printer.printLine("class " + class_name + " {");

		printer.printLine("");

		printer.increaseIndentation();
		
		if(!this.is_unrestricted) {
			class_usage.printToFile(printer);
			printer.printLine("");
		}
		
		Iterator<MoolField> mool_class_fields_it = class_fields.iterator();
		MoolField f;
		
		while(mool_class_fields_it.hasNext()) {
			f = mool_class_fields_it.next();
			
			if(f.getFieldUsageState() == null)
				printer.printLine(f.getFieldType() + " " + f.getFieldName() + ";");
			else
				printer.printLine(f.getFieldType() + "[" + f.getFieldUsageState().getUsageStateId() + "] " + f.getFieldName() + ";");
		}
		
		printer.printLine("");
		
		class_constructor.printToFile(printer);
		
		printer.printLine("");
		
		Iterator<MoolMethod> mool_class_methods_it = class_methods.iterator();
		MoolMethod m;
		
		while(mool_class_methods_it.hasNext()) {
			m = mool_class_methods_it.next();
			m.printToFile(printer);
			printer.printLine("");
		}
		
		printer.decreaseIndentation();
		
		printer.printLine("}");
	}
}
