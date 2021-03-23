package com.amituofo.datatable.impl.txt;

import java.io.File;

import com.amituofo.datatable.DataCatalog;
import com.amituofo.datatable.DataException;
import com.amituofo.datatable.DataFile;
import com.amituofo.datatable.DefinitionException;

public class TxtDataFile extends DataFile<TxtDataTableDefinition> {
	private static final long serialVersionUID = -1894097447693102468L;

	protected int startRow;
	protected boolean firstRowAsTitle;
	protected String charset;
	protected char delimiter;
	protected char smartQuote;
	protected String filePattern;

	public TxtDataFile(File fileOrDir, String charset, char delimiter, char smartQuote, String filePattern, int startRow, boolean firstRowAsTitle) {
		super(null, fileOrDir);
		this.dataAccessor = new TxtDataAccessor();
		this.charset = charset;
		this.startRow = startRow;
		this.firstRowAsTitle = firstRowAsTitle;
		this.delimiter = delimiter;
		this.smartQuote = smartQuote;
		this.filePattern = filePattern;
	}

	public TxtDataFile(TxtDataTableDefinition definition, File fileOrDir, String filePattern, int startRow) {
		super(definition, fileOrDir);
		// if (definition != null && !(definition instanceof TxtDataTableDefinition)) {
		// throw new DefinitionException("Definition must be instance of " + TxtDataTableDefinition.class.getName());
		// }

		this.dataAccessor = new TxtDataAccessor();

		this.charset = definition.getCharset();
		this.startRow = definition.getStartRow();
		this.firstRowAsTitle = definition.isFirstRowAsTitle();
		this.delimiter = definition.getDelimiter();
		this.smartQuote = definition.getQuote();
		this.filePattern = filePattern;
	}

	public TxtDataFile(File file, String charset, char delimiter, char smartQuote, int startRow, boolean firstRowAsTitle) {
		this(file, charset, delimiter, smartQuote, "*.*", startRow, firstRowAsTitle);
	}

	public DataCatalog openCatalog() throws DataException, DefinitionException {
		DataCatalog dc = dataAccessor.open(definition, "file:///" + this.getPath() + "?charset=" + charset + "&startRow=" + startRow + "&firstRowAsTitle=" + firstRowAsTitle
				+ "&delimiter=" + delimiter + "&smartQuote=" + smartQuote + "&filePattern=" + filePattern + "&subFolder=true");
		return dc;
	}
}
