package com.amituofo.datatable.impl.fil;

import java.io.File;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.DefinitionException;
import com.amituofo.datatable.impl.txt.TxtDataTable;

public class FilDataTable extends TxtDataTable {

	public FilDataTable(String name, FilDataTableDefinition definition, File file) throws DataException, DefinitionException {
		super(name, definition, file);
	}
}
