package com.amituofo.datatable;

public class InvalidDataException extends Exception {
	private static final long serialVersionUID = 627144628896771019L;

	public InvalidDataException() {
		super();
	}

	public InvalidDataException(String msg) {
		super(msg);
	}

	public InvalidDataException(Throwable cause) {
		super(cause);
	}

	public InvalidDataException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
