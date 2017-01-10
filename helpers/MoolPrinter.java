package helpers;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class MoolPrinter {

	private PrintWriter writer;
	private int indent_level;
	private boolean deactivate_indent;
	
	public MoolPrinter(String filename) throws FileNotFoundException, UnsupportedEncodingException {
		writer = new PrintWriter(filename + ".mool", "UTF-8");
		indent_level = 0;
		deactivate_indent = false;
	}
	
	public void increaseIndentation() {
		indent_level++;
	}
	
	public void decreaseIndentation() {
		indent_level--;
	}
	
	public void activateIndentation() {
		deactivate_indent = false;
	}
	
	public void deactivateIndentation() {
		deactivate_indent = true;
	}
	
	public void print(String line) {
		if(deactivate_indent)
			writer.print(line);
		else
			writer.print(getIndentation() + line);
	}
	public void printLine(String line) {
		if(deactivate_indent)
			writer.println(line);
		else
			writer.println(getIndentation() + line);
	}
	
	private String getIndentation() {
		
		String indentation = "";
		
		for(int i = 0; i < indent_level; i++) {
			indentation += "\t";
		}
		
		return indentation;
	}
	
	public void closeWriter() {
		writer.close();
	}
}
