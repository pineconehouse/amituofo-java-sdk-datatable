package com.amituofo.datatable.impl.fil;

import java.io.File;

import com.amituofo.datatable.DataCatalog;
import com.amituofo.datatable.DataException;
import com.amituofo.datatable.DataFile;
import com.amituofo.datatable.DefinitionException;

public class FilDataFile extends DataFile<FilDataTableDefinition> {
	private static final long serialVersionUID = -7200875452550106437L;
	private int startRow;
	private boolean firstRowAsTitle;
	private String charset;
	private int[] eachColumnLength = null;

	public FilDataFile(File fileOrDir, String charset, boolean firstRowAsTitle, int[] eachColumnLength) {
		this(fileOrDir, charset, 0, firstRowAsTitle, eachColumnLength);
	}

	public FilDataFile(File fileOrDir, String charset, int startRow, boolean firstRowAsTitle, int[] eachColumnLength) {
		super(null, fileOrDir);
		this.dataAccessor = new FilDataAccessor();
		this.charset = charset;
		this.startRow = startRow;
		this.firstRowAsTitle = firstRowAsTitle;
		this.eachColumnLength = eachColumnLength;
	}

	public FilDataFile(FilDataTableDefinition definition, File fileOrDir) throws DefinitionException {
		super(definition, fileOrDir);

		// if (!(definition instanceof FilDataTableDefinition)) {
		// throw new DefinitionException("Definition must be instance of FilDataTableDefinition");
		// } else {
		this.dataAccessor = new FilDataAccessor();

		FilDataTableDefinition def = (FilDataTableDefinition) definition;
		this.charset = def.getCharset();
		this.startRow = def.getStartRow();
		this.firstRowAsTitle = def.isFirstRowAsTitle();
		// }
	}

	@Override
	public DataCatalog openCatalog() throws DataException, DefinitionException {
		DataCatalog dc = dataAccessor.open(definition, "file:///" + this.getPath() + "?charset=" + charset + "&startRow=" + startRow + "&firstRowAsTitle=" + firstRowAsTitle
				+ "&columnsLength=" + FilDataAccessor.columnsLength2String(eachColumnLength) + "&filePattern=*.fil&subFolder=false");
		return dc;
	}
}
