package com.amituofo.datatable.impl.txt;

import java.io.File;

import com.amituofo.datatable.DefinitionException;

public class BsvDataFile extends TxtDataFile {
	private static final long serialVersionUID = -6993383867884418003L;

	public BsvDataFile(File fileOrDir, String charset, boolean firstRowAsTitle, int startRow) {
		super(fileOrDir, charset, ' ', '\"', "*.*sv", startRow, firstRowAsTitle);
	}

	public BsvDataFile(File fileOrDir, String charset, boolean firstRowAsTitle) {
		super(fileOrDir, charset, ' ', '\"', "*.*sv", 1, firstRowAsTitle);
	}

	public BsvDataFile(TxtDataTableDefinition definition, File fileOrDir, int startRow) throws DefinitionException {
		super(definition, fileOrDir, "*.*sv", startRow);
	}

	public BsvDataFile(TxtDataTableDefinition definition, File fileOrDir) throws DefinitionException {
		super(definition, fileOrDir, "*.*sv", 1);
	}

	public BsvDataFile(TxtDataTableDefinition definition, File fileOrDir, String filePattern, int startRow) throws DefinitionException {
		super(definition, fileOrDir, filePattern, startRow);
	}
}
