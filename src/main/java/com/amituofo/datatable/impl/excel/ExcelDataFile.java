package com.amituofo.datatable.impl.excel;

import java.io.File;

import com.amituofo.datatable.DataCatalog;
import com.amituofo.datatable.DataException;
import com.amituofo.datatable.DataFile;
import com.amituofo.datatable.DefinitionException;

public class ExcelDataFile extends DataFile<ExcelDataTableDefinition> {
	private static final long serialVersionUID = -756675084921496671L;
	private int startRow;
	private int startColumn;
	private boolean firstRowAsTitle;

	public ExcelDataFile(File file, int startRow, boolean firstRowAsTitle) {
		this(null, file, startRow, 0, firstRowAsTitle);
	}

	public ExcelDataFile(File file, int startRow, int startColumn, boolean firstRowAsTitle) {
		this(null, file, startRow, startColumn, firstRowAsTitle);
	}

	public ExcelDataFile(ExcelDataTableDefinition definition, File file, int startRow, boolean firstRowAsTitle) {
		this(definition, file, startRow, 0, firstRowAsTitle);
	}

	public ExcelDataFile(ExcelDataTableDefinition definition, File file, int startRow, int startColumn, boolean firstRowAsTitle) {
		super(definition, file);
		this.startRow = startRow;
		this.startColumn = startColumn;
		this.firstRowAsTitle = firstRowAsTitle;
		this.dataAccessor = new ExcelDataAccessor();
	}

	@Override
	public DataCatalog openCatalog() throws DataException, DefinitionException {
		DataCatalog dc = dataAccessor.open(definition, "file:///" + this.getPath() + "?startReadRow=" + startRow + "&startReadColumn=" + startColumn + "&firstRowAsTitle="
				+ firstRowAsTitle);
		return dc;
	}
}
