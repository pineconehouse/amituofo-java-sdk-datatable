package com.amituofo.datatable.impl.txt;

import java.io.File;

import com.amituofo.datatable.DefinitionException;

public class CsvDataFile extends TxtDataFile {
	private static final long serialVersionUID = 2059524785761490340L;

	public CsvDataFile(File fileOrDir, String charset, boolean firstRowAsTitle, int startRow) {
		super(fileOrDir, charset, ',', '\"', "*.csv", startRow, firstRowAsTitle);
	}

	public CsvDataFile(File fileOrDir, String charset, boolean firstRowAsTitle) {
		super(fileOrDir, charset, ',', '\"', "*.csv", 1, firstRowAsTitle);
	}

	public CsvDataFile(TxtDataTableDefinition definition, File fileOrDir, int startRow) throws DefinitionException {
		super(definition, fileOrDir, "*.csv", startRow);
	}

	public CsvDataFile(TxtDataTableDefinition definition, File fileOrDir) throws DefinitionException {
		super(definition, fileOrDir, "*.csv", 1);
	}

	public CsvDataFile(TxtDataTableDefinition definition, File fileOrDir, String filePattern, int startRow) throws DefinitionException {
		super(definition, fileOrDir, filePattern, startRow);
	}
}
