package com.amituofo.datatable.impl.fil;

import java.io.File;
import java.util.List;
import java.util.Properties;

import com.amituofo.datatable.DataAccessor;
import com.amituofo.datatable.DataException;
import com.amituofo.datatable.DataTable;
import com.amituofo.datatable.DefinitionException;

public class FilDataAccessor implements DataAccessor<FilDataTableDefinition> {

	private FilDataCatalog workDataCatalog = null;

	public FilDataAccessor() {
	}

	@Override
	public FilDataCatalog open(String url) throws DataException, DefinitionException {
		return open(null, url, null);
	}

	@Override
	public FilDataCatalog open(String url, Properties properties) throws DataException, DefinitionException {
		return open(null, url, properties);
	}

	@Override
	public FilDataCatalog open(FilDataTableDefinition definition, String url) throws DataException, DefinitionException {
		return open(definition, url, null);
	}

	@Override
	public FilDataCatalog open(FilDataTableDefinition definition, String url, Properties properties) throws DataException, DefinitionException {
		if (url == null || url.length() == 0) {
			throw new DataException("Invalid url " + url);
		}

		File dirOrFile = null;
		String charset = "utf-8";
		int startRow = 1;
		boolean firstRowAsTitle = false;
		String filePattern = "*.fil";
		boolean subFolder = false;
		int[] columnsLength = null;

		int indexFileend = url.indexOf('?');
		if (indexFileend == -1) {
			dirOrFile = new File(url);
		} else {
			String paramPart = url.substring(indexFileend + 1);
			// 8="file:///"
			String filepath = url.substring(8, indexFileend);//.toLowerCase().replace("file:///", "");
			dirOrFile = new File(filepath);
			String[] params = paramPart.split("&");
			for (String param : params) {
				String[] namevalue = param.split("=");
				if (namevalue.length == 2) {
					// throw new DataException("Invalid parameter " + param);

					if (namevalue[0].equalsIgnoreCase("charset")) {
						charset = namevalue[1];
					} else if (namevalue[0].equalsIgnoreCase("startRow")) {
						startRow = Integer.parseInt(namevalue[1]);
					} else if (namevalue[0].equalsIgnoreCase("firstRowAsTitle")) {
						firstRowAsTitle = Boolean.parseBoolean(namevalue[1]);
					} else if (namevalue[0].equalsIgnoreCase("columnsLength")) {
						columnsLength = valueOfColumnsLength(namevalue[1]);
					} else if (namevalue[0].equalsIgnoreCase("filePattern")) {
						filePattern = namevalue[1].trim();
					} else if (namevalue[0].equalsIgnoreCase("subFolder")) {
						subFolder = Boolean.parseBoolean(namevalue[1].toLowerCase());
					}
				}
			}
		}

		if (properties != null) {
			charset = properties.getProperty("charset", charset);
			startRow = Integer.parseInt(properties.getProperty("startRow", "" + startRow));
			firstRowAsTitle = Boolean.parseBoolean(properties.getProperty("firstRowAsTitle", "" + firstRowAsTitle).toLowerCase());
			columnsLength = valueOfColumnsLength(properties.getProperty("columnsLength", columnsLength2String(columnsLength)));
			filePattern = properties.getProperty("filePattern", filePattern);
			subFolder = Boolean.parseBoolean(properties.getProperty("subFolder", "" + subFolder).toLowerCase());
		}

		String name = dirOrFile.isDirectory() ? dirOrFile.getName() : dirOrFile.getParentFile().getName();
		workDataCatalog = new FilDataCatalog(name, definition,dirOrFile, filePattern, subFolder, charset, startRow, firstRowAsTitle, columnsLength);
		return workDataCatalog;
	}

	public static int[] valueOfColumnsLength(String str) {
		String[] ls = str.split(",");
		int[] columnsLength = new int[ls.length];
		for (int i = 0; i < ls.length; i++) {
			String length = ls[i].trim();
			columnsLength[i] = length.length() == 0 ? 0 : Integer.parseInt(length);
		}

		return columnsLength;
	}

	public static String columnsLength2String(int[] colsLength) {
		StringBuffer lengths = new StringBuffer();
		if (colsLength != null && colsLength.length > 0) {
			for (int i = 0; i < colsLength.length - 1; i++) {
				lengths.append(colsLength[i]);
				lengths.append(",");
			}

			lengths.append(colsLength[colsLength.length - 1]);
		}

		return lengths.toString();
	}

	@Override
	public void close() throws DataException {
		List<DataTable> all = workDataCatalog.getTables();
		for (DataTable txtDataTable : all) {
			txtDataTable.close();
		}
		
		workDataCatalog.close();
		workDataCatalog = null;
	}
}
