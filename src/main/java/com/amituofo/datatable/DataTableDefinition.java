package com.amituofo.datatable;


public interface DataTableDefinition {// extends DataRecordParser<Object, Object>, DataRecordRender {

	public DataRecord newDataRecordInstance(boolean initFields);
//	public DataRecord newDataRecordInstance();
//	public void newDataRecordInstance(boolean initFields);

	public String getName();

	public void setName(String name);

	// public DataTableDefinition clone();
	
//	public DataFieldDefinition createObjectField();
//	public DataFieldDefinition createStringField();
//	public DataFieldDefinition createDecimalField();
//	public DataFieldDefinition createDateField();
	public DataFieldDefinition createField(String name, DataFieldType fieldType);

	public void addColumn(DataFieldDefinition fieldDefinition) throws DefinitionException;

	public void insertColumn(int index, DataFieldDefinition fieldDefinition) throws DefinitionException;

	public void addColumns(DataFieldDefinition[] all) throws DefinitionException;

	public DataFieldDefinition getColumn(int column);

	public DataFieldDefinition getColumn(String columnName);

	public int getColumnIndex(String name);

	public DataFieldDefinition hiddenColumn(int index);

	public DataFieldDefinition hiddenColumn(String name);

	public DataFieldDefinition[] getColumns();

	public int getColumnCount();

	public String[] getColumnNames();
	
	public void setColumnNames(String... names);

	// public boolean isFirstRowAsTitle();

//	public void removeAll();

	public void resetColumn(DataTableDefinition fromDefinition) throws DefinitionException;

	// -------------------------------------------------------------

	public DataRecordParser getParser();

	public void setParser(DataRecordParser parser);

	public DataRecordRender getRender();

	public void setRender(DataRecordRender Render);

//	public DataRecordFilter getFilter();

//	public void setFilter(DataRecordFilter filter);

	// -------------------------------------------------------------

	public void validate(DataRecord value) throws InvalidDataException;

//	public void parseTitle(Object value) throws DefinitionException, DataException;

	public DataRecord parse(Object value) throws DataException;

	public String rending(DataRecord value, int[] fieldIndexMapping);

	// public String[] rending(DataField[] values);

	public String rendingTitle();
	
//	public boolean isMatched(DataRecord record);


}
