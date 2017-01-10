package exceptions;

public class TooManyTransitionsException extends Exception {

	TooManyTransitionsException() {

	}

	public TooManyTransitionsException(String message) {
	    super(message);
	}
}
