package fr.cmci.biblio.security.exceptions;

public class SecurityException extends Exception{

	public SecurityException() {
		super() ;
	}
	
	public SecurityException(String message) {
		super(message) ;
	}
	
	public SecurityException(String message, Throwable t) {
		super(message, t) ;
	}
}
