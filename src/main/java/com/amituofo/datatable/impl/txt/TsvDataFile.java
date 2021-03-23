package com.amituofo.datatable.impl.txt;

import java.io.File;

import com.amituofo.datatable.DefinitionException;

public class TsvDataFile extends TxtDataFile {
	private static final long serialVersionUID = -6993383867884418003L;

	public TsvDataFile(File fileOrDir, String charset, boolean firstRowAsTitle, int startRow) {
		super(fileOrDir, charset, '\t', '\"', "*.tsv", startRow, firstRowAsTitle);
	}

	public TsvDataFile(File fileOrDir, String charset, boolean firstRowAsTitle) {
		super(fileOrDir, charset, '\t', '\"', "*.tsv", 1, firstRowAsTitle);
	}

	public TsvDataFile(TxtDataTableDefinition definition, File fileOrDir, int startRow) throws DefinitionException {
		super(definition, fileOrDir, "*.tsv", startRow);
	}

	public TsvDataFile(TxtDataTableDefinition definition, File fileOrDir) throws DefinitionException {
		super(definition, fileOrDir, "*.tsv", 1);
	}

	public TsvDataFile(TxtDataTableDefinition definition, File fileOrDir, String filePattern, int startRow) throws DefinitionException {
		super(definition, fileOrDir, filePattern, startRow);
	}
}
