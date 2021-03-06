package parser;

/* Generated By:JavaCC: Do not edit this line. ParserConstants.java */

/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface ParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int CLASS = 5;
  /** RegularExpression Id. */
  int INV = 6;
  /** RegularExpression Id. */
  int VOID = 7;
  /** RegularExpression Id. */
  int Id = 8;
  /** RegularExpression Id. */
  int Num = 9;
  /** RegularExpression Id. */
  int LPAR = 10;
  /** RegularExpression Id. */
  int RPAR = 11;
  /** RegularExpression Id. */
  int LBRACKET = 12;
  /** RegularExpression Id. */
  int RBRACKET = 13;
  /** RegularExpression Id. */
  int ADD_OP = 14;
  /** RegularExpression Id. */
  int MUL_OP = 15;
  /** RegularExpression Id. */
  int L = 16;
  /** RegularExpression Id. */
  int EL = 17;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\r\"",
    "\"\\n\"",
    "\"class\"",
    "\"//@ invariant \"",
    "\"void\"",
    "<Id>",
    "<Num>",
    "\"(\"",
    "\")\"",
    "\"{\"",
    "\"}\"",
    "\"+\"",
    "\"*\"",
    "\";\"",
    "\";;\"",
  };

}
