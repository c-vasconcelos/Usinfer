import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.sun.org.apache.xml.internal.serialize.IndentPrinter;

import algorithm_structures.EPA;
import algorithm_structures.Usage;
import algorithm_structures.UsageBranch;
import algorithm_structures.UsageState;
import algorithms.TypestateConstruction;
import algorithms.UsageConstruction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import class_elements.*;
import exceptions.TooManyTransitionsException;
import helpers.MoolPrinter;
import logic.Prover;
import parser.MoolMinusParser;
import parser.ParseException;

import parser_structures.*;

public class Main {

	static MoolClass mool_class;
	static Map<String, MoolClass> mool_classes;

	 //static String example_file = "FileAll3";
	//static String example_file = "Petition";
	//static String example_file = "Auction";
	//static String example_file = "AAA";

	static String example_file;
	
	static boolean print_typestates;
	
	static Context ctx;

	/** Main entry point. 
	 * @throws IOException 
	 * @throws ParserException 
	 * @throws ParseException 
	 * @throws assertion_parser.ParseException */
	public static void main(String args[]) throws Exception, IOException, ParseException {

		//System.out.println(args.length);
		
		if(args.length == 0 || (args.length == 2 && !args[1].equals("-typestates"))) {
			System.out.println("Usage: java -jar uit.jar file.mools [-typestates]");
			return;
		}
		
		try {
			example_file = args[0];
			
			print_typestates = args.length == 2;
			
			getParsedClass();
			inferClassUsages();	
			setObjectUsateStates();		
			printProgram();
			System.out.println("Wrote " + example_file + ".mool");
		} catch(TooManyTransitionsException e) {
			System.out.println("-------------------------------------------");
			System.out.println("Exception: " + e.getLocalizedMessage());
		} catch(Exception e) {
			System.out.println("Exception: Unknown error.");
		}
	}

	private static void inferClassUsages() throws IOException, TooManyTransitionsException {
		Iterator<MoolClass> mool_class_it = mool_classes.values().iterator();
		
		if(print_typestates) {
			System.out.println("-------------------------------------------");
			System.out.println("Printing typestates");
			System.out.println("-------------------------------------------");
		}
		
		while(mool_class_it.hasNext()) {
			mool_class = mool_class_it.next();

			if(!mool_class.isUnrestricted()) {
				TypestateConstruction t = new TypestateConstruction(ctx, mool_class);
				EPA epa = t.generateEPA();

				if(print_typestates) {
					System.out.println(mool_class.getClassName());
					System.out.println("-------------------------------------------");
					epa.printEPA();
					System.out.println("-------------------------------------------");
				}

				UsageConstruction uc = new UsageConstruction();

				uc.generateUsage(epa.getInitialStates().get(0), epa);			
				Usage usage = uc.getUsage(mool_class.getClassName());
				mool_class.setUsage(usage);
			}
		}
	}

	private static void setObjectUsateStates() throws IOException {
		boolean changes = true;

		while(changes) {
			changes = false;

			Iterator<MoolClass> mool_class_it = mool_classes.values().iterator();

			while(mool_class_it.hasNext()) {


				mool_class = mool_class_it.next();
				Iterator<MoolField> it = mool_class.getClassConstructor().getMethodParams().iterator();
				if(!mool_class.isUnrestricted()) {
					while(it.hasNext()) {
						MoolField f = it.next();

						if(!(f.hasPrimitiveType()) && f.getFieldUsageState() == null && !mool_classes.get(f.getFieldType()).isUnrestricted()) {
							f.setFieldUsageState(getUsageState(mool_class, f));
							changes = true;
						}
					}

					mool_class.getClassConstructor().getMethodBody().changeFieldState(mool_class.getClassConstructor().getMethodParams(), mool_class, mool_class.getClassConstructor());
				}

				Iterator<MoolMethod> it_methods = mool_class.getClassMethods().iterator();

				while(it_methods.hasNext()) {
					MoolMethod m = it_methods.next();

					it = m.getMethodParams().iterator();

					while(it.hasNext()) {
						MoolField f = it.next();

						if(!(f.hasPrimitiveType()) && f.getFieldUsageState() == null && !mool_classes.get(f.getFieldType()).isUnrestricted()) {
							
							f.setFieldUsageState(getUsageState(mool_class, f));
							changes = true;
							
						}
					}

					m.getMethodBody().changeFieldState(m.getMethodParams(), mool_class, m);

					if(!(m.hasPrimitiveType()) && m.getMethodTypeUsageState() == null && !mool_classes.get(m.getMethodType()).isUnrestricted()) {
						m.setMethodTypeUsageState(getMethodUsageState(mool_class, m));
						changes = true;
					}
				}
			}
		}
	}

	private static void printProgram() throws FileNotFoundException, UnsupportedEncodingException {
		Iterator<MoolClass> mool_class_it = mool_classes.values().iterator();

		MoolPrinter printer = new MoolPrinter(example_file);

		while(mool_class_it.hasNext()) {
			mool_class = mool_class_it.next();

			mool_class.printToFile(printer);

			if(mool_class_it.hasNext())
				printer.printLine("");
		}

		printer.closeWriter();
	}

	private static void getParsedClass() throws Exception, ParseException, FileNotFoundException {
		MoolMinusParser parser;

		/**
		 * Parses the classes, gathering all the information about each class
		 * and its corresponding methods
		 */
		//parser = new MoolMinusParser(new java.io.FileInputStream("examples/"+ example_file +".mools"));
		parser = new MoolMinusParser(new java.io.FileInputStream(example_file +".mools"));
		parser.setParserContext(true);
		parser.Start();

		/**
		 * Parses the classes a second time, now building the assertions
		 * to be evaluated by Z3
		 */
		parser.ReInit(new java.io.FileInputStream(example_file +".mools"));
		parser.setParserContext(false);
		parser.Start();
		mool_classes = parser.getMoolClasses();
		ctx = parser.getSolverContext();
	}

	private static UsageState getUsageState(MoolClass mool_class, MoolField f) throws IOException {

		MoolMethod m;
		List<BoolExpr> propositions;
		boolean isState;// = false;

		BoolExpr assertion;// = (BoolExpr) mool_classes.get(f.belongsTo()).getClassInvariant().getAssertion(); 

		if(f.isClassField()) {
			return null;
		}

		if(mool_classes.get(f.getFieldType()).isUnrestricted())
			return null;



		if(mool_class.getClassName().equals(f.belongsTo())) {
			//assertion = (BoolExpr) mool_class.getClassConstructor().getMethodRequires().getAssertion(false);

			assertion = (BoolExpr) mool_class.getClassConstructor().getMethodRequires().getFieldAssertion(f.getFieldName());
		} else {
			//assertion = (BoolExpr) mool_class.getMethod(f.belongsTo()).getMethodRequires().getAssertion(false);
			assertion = (BoolExpr) mool_class.getMethod(f.belongsTo()).getMethodRequires().getFieldAssertion(f.getFieldName());
		}

		if(assertion == null)
			return null;

		
		
		Usage u = mool_classes.get(f.getFieldType()).getUsage();

		Iterator<UsageState> it = u.getUsageStates().iterator();

		while(it.hasNext()) {
			UsageState state = it.next();

			Iterator<UsageBranch> it_branches = state.getUsageBranches().iterator();

			isState = true;

			while(it_branches.hasNext()) {
				propositions = new ArrayList<BoolExpr>();

				propositions.add(assertion);

				MoolMethod abc = it_branches.next().getAction();
				propositions.add((BoolExpr) abc.getMethodRequires().getAssertion(false));

				if(!Prover.prove(ctx, propositions))
					isState = false;//return state.getUsageStateId();
			}

			if(isState)
				return state;
		}

		return null;
	}

	private static UsageState getMethodUsageState(MoolClass mool_class, MoolMethod m) throws IOException {


		List<BoolExpr> propositions;
		boolean isState;// = false;
		BoolExpr assertion = null;

		ASTNode body = m.getMethodBody();

		if(body instanceof ASTSeq) {
			ASTNode exp2 = ((ASTSeq) m.getMethodBody()).getExp2();

			while(exp2 instanceof ASTSeq)
				exp2 = ((ASTSeq) exp2).getExp2();

			if(exp2 instanceof ASTField || exp2 instanceof ASTNew)
				assertion = (BoolExpr) m.getMethodEnsures().getFieldAssertion(exp2.toString());
			//BoolExpr assertion = (BoolExpr) m.getMethodEnsures().getFieldAssertion(field);// = (BoolExpr) mool_classes.get(f.belongsTo()).getClassInvariant().getAssertion(); 
		} else if (body instanceof ASTId) {
			assertion = (BoolExpr) m.getMethodEnsures().getFieldAssertion(body.toString());
		} else if (body instanceof ASTField) {
			assertion = (BoolExpr) m.getMethodEnsures().getFieldAssertion(body.toString());
		} else if (body instanceof ASTNew) {
			assertion = (BoolExpr) m.getMethodEnsures().getFieldAssertion(body.toString());
		}

		if(assertion == null)
			return null;

		Usage u = mool_classes.get(m.getMethodType()).getUsage();

		Iterator<UsageState> it = u.getUsageStates().iterator();

		while(it.hasNext()) {
			UsageState state = it.next();

			Iterator<UsageBranch> it_branches = state.getUsageBranches().iterator();

			isState = true;

			while(it_branches.hasNext()) {
				propositions = new ArrayList<BoolExpr>();

				propositions.add(assertion);

				MoolMethod abc = it_branches.next().getAction();
				propositions.add((BoolExpr) abc.getMethodRequires().getAssertion(false));

				if(!Prover.prove(ctx, propositions))
					isState = false;//return state.getUsageStateId();
			}

			if(isState)
				return state;
		}

		return null;
	}
}