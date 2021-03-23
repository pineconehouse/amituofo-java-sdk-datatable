package com.amituofo.datatable;


public interface DataField {
//	public String getName();

//	public DataFieldDefinition getDefinition();

	public Object getAttribute(String name);

	public void setAttribute(String name, Object data);

	// public Formula getFormula();
	//
	// public void setFormula(Formula formula);

	// public String randerValue();

	public Object getValue();

	public void setValue(Object o);

//	public void validate();
//
//	public boolean isInvalid();
//
//	public List<String> getErrors();
//
//	public void appendError(String message);
//
//	public void clearErrors();

	public String toString();

	public boolean equals(DataField obj);
}
