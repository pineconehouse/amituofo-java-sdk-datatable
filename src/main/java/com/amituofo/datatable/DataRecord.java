package com.amituofo.datatable;

import java.io.Serializable;

public interface DataRecord extends Serializable {
	// public DataTableDefinition getDefinition();

	public DataField get(int index);

	public DataField get(String name);

	public DataField[] getFields();

	public Object getValue(int index);

	public Object getValue(String name);

	public String getValueAsString(int index);

	public String getValueAsString(String name);

	public Number getValueAsDecimal(int index);

	public Number getValueAsDecimal(String name);

	public Object[] getValues();

	// public String[] renderValues(DataTableDefinition definition);

	// public String getFieldName(int index);
	//
	// public String[] getFieldNames();

	public int getFieldCount();

	public int getFieldIndex(String name);

	// public DataField set(int index, DataField value);

	// public DataField parse(int index, Object value) throws DataException;

	// public DataField set(String name, DataField value);

	// public DataField parse(String name, Object value) throws DataException;

	// public DataRecord parse(Object[] fieldValues) throws DataException;

	// public DataField set(String name, Object value) throws DataException;

	public DataField set(int index, DataField value) throws DataException;

	public DataField set(int index, Object value) throws DataException;

	public DataRecord set(Object[] fieldValues) throws DataException;

	// public void validate(DataTableDefinition definition) throws DefinitionException;
	//
	// public boolean isValid();
	//
	// public List<String> getErrors();
	//
	// public void appendError(String message);
	//
	// public void clearErrors();

	// public DataRecord clone();

	public String toString();

	public String toString(String delimiter);

	public String[] toStringValues();

	public boolean equals(DataRecord obj);

	// public DataRecord parse(Object record);

}
