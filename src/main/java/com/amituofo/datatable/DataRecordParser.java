package com.amituofo.datatable;

public interface DataRecordParser {
	public DataTableDefinition getDefinition();

	public void setDefinition(DataTableDefinition definition);

	public DataRecord parseRecord(Object record) throws DataException;

//	public DataRecord parseFields(Object[] recordFields) throws DataException;
}

//public interface DataRecordParser<RECORD_TYPE, FIELD_TYPE> {
////	public DataRecordParser<RECORD_TYPE, FIELD_TYPE> clone();
//
//	public DataTableDefinition getDefinition();
//
//	public void setDefinition(DataTableDefinition definition);
//
//	public DataRecord parse(RECORD_TYPE record) throws DataException;
//
//	public DataRecord parse(FIELD_TYPE[] recordFields) throws DataException;
//}

