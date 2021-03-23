package com.amituofo.datatable;

public interface DataFieldType {
	public String getName();

	public DataFieldType clone();

	public void validate(DataField field) throws InvalidDataException;

	public DataField newField();

	public boolean isAllowEmpty();

	public void setAllowEmpty(boolean allowEmpty);

	public boolean isNoLength();

	public void setNoLength(boolean noLength);

	public int getIntegerLength();

	public void setIntegerLength(int integerLength);

	public int getDecimalLength();

	public void setDecimalLength(int decimalLength);

	public Object getDefaultValue();

	public void setDefaultValue(Object defaultValue);

	public String getDescription();

	public void setDescription(String description);

	public int getTotalLength();
}
