package com.amituofo.datatable;

import java.util.List;

public interface DataTable {

	public void close();

	public String getName();

	public void setName(String name);

	public DataTableDefinition getDefinition();

	// public void setDefinition(DataTableDefinition definition);

	// public String[] getColumnNames();

	public int currentReadingLine();

	public int currentWritenLine();

	public void truncate() throws DataException;

//	public int getFilteredRecordCount() throws DataException;
	
	public long getRecordCount() throws DataException;

	public DataRecord readFirstRecord() throws DataException;

	public DataRecord readNextRecord() throws DataException;

	public List<DataRecord> readRecords(int startRowNo, int readCount) throws DataException;

	public List<DataRecord> readFirstRecords(int readCount) throws DataException;

	public List<DataRecord> readNextRecords(int readCount) throws DataException;

	// public void appendRecord(String[] line) throws DataException;

//	public void appendRecord(Object[] values) throws DataException;

	public void appendRecord(DataRecord line) throws DataException;

	public void appendRecords(List<DataRecord> lines) throws DataException;

	public void appendRecord(DataRecord line, int[] fieldIndexMapping) throws DataException;

	public void appendRecords(List<DataRecord> lines, int[] fieldIndexMapping) throws DataException;

//	public void appendAll(DataTable tableSet) throws DataException;
	public void appendAll(DataTable tableSet, boolean includeTitle, IndexMapping indexMapping) throws DataException;

	public void writeTitle() throws DataException;

	public void resetColumn(DataTableDefinition definition) throws DefinitionException;
	
	public void setFilter(DataRecordFilter recordFilter);
	
//	public void clearFilter();

	DataRecordFilter getFilter();
}
