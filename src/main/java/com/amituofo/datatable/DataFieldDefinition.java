package com.amituofo.datatable;

public interface DataFieldDefinition {
	public String getName();

	public void setName(String name);

	public int getMappingIndex();

	public void setMappingIndex(int mappingIndex);

	// public DataFieldDefinition clone();

	public DataField newDataFieldInstance();
	
	public DataField getDefault();

	public DataFieldParser getParser();

	// public void setParser(DataFieldParser parser);

	public DataFieldRender getRender();

	// public void setRender(DataFieldRender Render);

	public DataFieldType getType();

	public void setType(DataFieldType type);

	// public Object getDefaultValue();

	// public void setDefaultValue(Object defaultValue);

	public void validate(DataField value) throws InvalidDataException;

	public DataField parse(Object value) throws DataException;

	public String rending(DataField value);
	
//	public String rendingDefault();

	public void setHidden(boolean hidden);
	
	public boolean isHidden();

	public String getDescription();

	public void setDescription(String description);

}
