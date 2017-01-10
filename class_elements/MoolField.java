package class_elements;

import java.util.Iterator;
import java.util.List;

import com.microsoft.z3.BoolExpr;

import algorithm_structures.UsageState;
import parser_structures.ASTNode;

public class MoolField {

	private String field_name;
	
	private String field_type;
	
	private boolean is_class_field;
	
	private String belongs_to;
	
	private UsageState field_usage_state;
	
	public MoolField(String field_name, String field_type, boolean is_class_field, String belongs_to) {
		this.field_name = field_name;
		this.field_type = field_type;
		this.is_class_field = is_class_field;
		this.belongs_to = belongs_to;
		this.field_usage_state = null;
	}
	public String getFieldName() {
		return field_name;
	}
	
	public String getFieldType() {
		return field_type;
	}
	
	public boolean isClassField() {
		return is_class_field;
	}
	
	public boolean hasPrimitiveType() {
		
		switch(this.field_type) {
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

	public String belongsTo() {
		return belongs_to;
	}
	
	public void setFieldUsageState(UsageState field_usage_state) {
		this.field_usage_state = field_usage_state;
	}
	
	public UsageState getFieldUsageState() {
		return field_usage_state;
	}
	
	public String toString() {
		return field_type + " " + field_name;
	}
}
