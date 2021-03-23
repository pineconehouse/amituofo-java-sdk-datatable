package com.amituofo.datatable;

import java.util.List;

public interface DataCatalog {

	public String getName();

	public void setName(String name);

	public int getTableCount();

	public List<DataTable> getTables() throws DataException;

	public DataTable getTable(int index);

	public DataTable getTable(String name) throws DataException;

	public DataTable dropTable(String name) throws DataException;

//	public void setDataTableDefinition(DataTableDefinition definition) throws DataException;

	public DataTable createTable(String name, DataTableDefinition definition) throws DataException, DefinitionException;

	public boolean isTableExists(String name);

	public void close();
}
