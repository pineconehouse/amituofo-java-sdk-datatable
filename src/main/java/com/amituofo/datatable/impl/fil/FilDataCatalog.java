package com.amituofo.datatable.impl.fil;

import java.io.File;
import java.util.List;

import com.amituofo.common.util.FileUtils;
import com.amituofo.datatable.DataException;
import com.amituofo.datatable.DataTable;
import com.amituofo.datatable.DataTableDefinition;
import com.amituofo.datatable.DefinitionException;
import com.amituofo.datatable.impl.basic.AbsDataCatalog;

public class FilDataCatalog extends AbsDataCatalog {
	private File dir = null;

	public FilDataCatalog(String name, FilDataTableDefinition definition, File dirOrFile, String filePattern, boolean subFolder, String charset, int startRow, boolean firstRowAsTitle,
			int[] columnsLength) throws DataException, DefinitionException {
		super(name);

		if (!dirOrFile.exists() || dirOrFile.isFile()) {
			this.dir = dirOrFile.getParentFile();

			FilDataTableDefinition dtd = definition == null ? FilDataDefinitionFactory.create(name, charset, startRow, firstRowAsTitle, columnsLength) : definition;
			FilDataTable dt = new FilDataTable(dirOrFile.getName(), dtd, dirOrFile);
			setDataTable(dt.getName(), dt);
		} else if (dirOrFile.isDirectory()) {
			this.dir = dirOrFile;

			List<File> files = FileUtils.findFiles(dirOrFile.getPath(), filePattern, subFolder);
			for (File file : files) {
				FilDataTableDefinition dtd = definition == null ? FilDataDefinitionFactory.create(name, charset, startRow, firstRowAsTitle, columnsLength) : definition;
				FilDataTable dt = new FilDataTable(file.getName(), dtd, file);
				setDataTable(dt.getName(), dt);
			}
		}
	}

	@Override
	public DataTable createTable(String name, DataTableDefinition definition) throws DataException, DefinitionException {
		if (this.isTableExists(name) && getDataTableFile(name).length() != 0) {
			throw new DataException("Data table exist!");
		}

		FilDataTable dt = new FilDataTable(name, (FilDataTableDefinition)definition, getDataTableFile(name));

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
