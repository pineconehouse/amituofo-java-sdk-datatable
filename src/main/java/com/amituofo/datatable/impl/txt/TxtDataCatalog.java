package com.amituofo.datatable.impl.txt;

import java.io.File;
import java.util.List;

import com.amituofo.common.util.FileUtils;
import com.amituofo.datatable.DataException;
import com.amituofo.datatable.DataTable;
import com.amituofo.datatable.DataTableDefinition;
import com.amituofo.datatable.DefinitionException;
import com.amituofo.datatable.impl.basic.AbsDataCatalog;

public class TxtDataCatalog extends AbsDataCatalog {
	private File dir = null;
//	private String charset = "UTF-8";
//	private int startRow = 1;
//	private boolean firstRowAsTitle;

	public TxtDataCatalog(String name, TxtDataTableDefinition definition, File dirOrFile, String filePattern, boolean subFolder, String charset, char delimiter, char quote,
			int startRow, boolean firstRowAsTitle) throws DataException, DefinitionException {
		super(name);
//		this.charset = charset;
//		this.startRow = startRow;
//		this.firstRowAsTitle = firstRowAsTitle;

		if (!dirOrFile.exists() || dirOrFile.isFile()) {
			this.dir = dirOrFile.getParentFile();

			TxtDataTableDefinition dtd = definition == null ? TxtDataDefinitionFactory.create(name, charset, delimiter, quote, startRow, firstRowAsTitle) : definition;
			TxtDataTable dt = new TxtDataTable(dirOrFile.getName(), dtd, dirOrFile);
			setDataTable(dt.getName(), dt);
		} else if (dirOrFile.isDirectory()) {
			this.dir = dirOrFile;

			List<File> files = FileUtils.findFiles(dirOrFile.getPath(), filePattern, subFolder);
			for (File file : files) {
				TxtDataTableDefinition dtd = definition == null ? TxtDataDefinitionFactory.create(name, charset, delimiter, quote, startRow, firstRowAsTitle) : definition;
				TxtDataTable dt = new TxtDataTable(file.getName(), dtd, file);
				setDataTable(dt.getName(), dt);
			}
		}
	}

	public DataTable createTable(String name, DataTableDefinition definition) throws DataException, DefinitionException {
		if (this.isTableExists(name) && getDataTableFile(name).length() != 0) {
			throw new DataException("Data table exist!");
		}

		TxtDataTable dt = new TxtDataTable(name, (TxtDataTableDefinition)definition, getDataTableFile(name));

		setDataTable(dt.getName(), dt);
		return dt;
	}

	@Override
	public DataTable dropTable(String name) throws DataException {
		DataTable dt = getTable(name);
		if (dt != null) {
			dt.close();
			boolean deled = getDataTableFile(name).delete();
			if (!deled) {
				throw new DataException("Failed to drop " + name);
			}
		}

		return super.dropTable(name);
	}

	private File getDataTableFile(String name) {
		String newfile = dir.getPath() + File.separator + name;
		return new File(newfile);
	}
}
