package com.db.awmd.challenge.exception;

public class SystemException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String errorCode;
	
	public String getErrorCode() {
		return errorCode;
	}

	public SystemException(String message, String errorCode) {
		super(message);

		this.errorCode = errorCode;
	}
}
