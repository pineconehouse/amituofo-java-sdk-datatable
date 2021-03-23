package com.amituofo.datatable;

public class DataException extends Exception {
	private static final long serialVersionUID = 5326623420218069629L;
	private int column = -1;
	private int row = -1;

	public DataException() {
	}

	public DataException(String message) {
		super(message);
	}

	public DataException(Throwable cause) {
		super(cause);
	}

	public DataException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataException(int row, int column, String message) {
		super(message);
		this.row = row;
		this.column = column;
	}

	public int getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

}
