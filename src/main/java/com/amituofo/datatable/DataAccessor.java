package com.amituofo.datatable;

import java.util.Properties;

public interface DataAccessor<T extends DataTableDefinition> {
//	public DataCatalog open(String url, String user, String password) throws DataException;
	
	public DataCatalog open(String url) throws DataException, DefinitionException;

	public DataCatalog open(String url, Properties properties) throws DataException, DefinitionException;

	public DataCatalog open(T definition, String url) throws DataException, DefinitionException;

	public DataCatalog open(T definition, String url, Properties properties) throws DataException, DefinitionException;
	
//	public void commit() throws DataException;

	public void close() throws DataException;
}
