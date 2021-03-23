package com.amituofo.datatable;

import java.io.File;
import java.net.URI;

public abstract class DataFile<TBL_DEF extends DataTableDefinition> extends File {
	private static final long serialVersionUID = 7203988118567253428L;
	
	protected TBL_DEF definition = null;
	protected DataAccessor<TBL_DEF> dataAccessor = null;

	public DataFile(TBL_DEF definition, String pathname) {
		super(pathname);
		this.definition = definition;
	}

	public DataFile(TBL_DEF definition, URI uri) {
		super(uri);
		this.definition = definition;
	}

	public DataFile(TBL_DEF definition, File file) {
		super(file.getAbsolutePath());
		this.definition = definition;
	}

	public TBL_DEF getDefinition() {
		return definition;
	}

	public DataCatalog openCatalog() throws DataException, DefinitionException {
		DataCatalog dc = dataAccessor.open(definition, this.getPath());
		return dc;
	}

	public void closeCatalog() throws DataException {
//		dataAccessor.commit();
		dataAccessor.close();
	}
}
