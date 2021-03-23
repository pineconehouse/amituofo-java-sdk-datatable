package com.amituofo.datatable.impl.txt;

import java.io.File;
import java.util.List;
import java.util.Properties;

import com.amituofo.datatable.DataAccessor;
import com.amituofo.datatable.DataException;
import com.amituofo.datatable.DataTable;
import com.amituofo.datatable.DefinitionException;

public class TxtDataAccessor implements DataAccessor<TxtDataTableDefinition> {

	private TxtDataCatalog workDataCatalog = null;

	public TxtDataAccessor() {
	}

	public TxtDataCatalog open(String url) throws DataException, DefinitionException {
		return open(null, url, null);
	}

	public TxtDataCatalog open(String url, Properties properties) throws DataException, DefinitionException {
		return open(null, url, properties);
	}

	public TxtDataCatalog open(TxtDataTableDefinition definition, String url) throws DataException, DefinitionException {
		return open(definition, url, null);
	}

	public TxtDataCatalog open(TxtDataTableDefinition definition, String url, Properties properties) throws DataException, DefinitionException {
		if (url == null || url.length() < 10) {
			throw new DataException("Invalid url " + url);
		}

		File dirOrFile = null;
		String charset = "utf-8";
		int startRow = 1;
		boolean firstRowAsTitle = false;
		char delimiter = ',';
		char smartQuote = '\'';
		// PARSE_MODE parseMode = PARSE_MODE.DEFINITION_PRIORITY;
		String filePattern = "*.csv";
		boolean subFolder = false;

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
				if (namevalue.length != 2) {
					throw new DataException("Invalid parameter " + param);
				}

				if (namevalue[0].equalsIgnoreCase("charset")) {
					charset = namevalue[1];
				} else if (namevalue[0].equalsIgnoreCase("startRow")) {
					startRow = Integer.parseInt(namevalue[1]);
				} else if (namevalue[0].equalsIgnoreCase("firstRowAsTitle")) {
					firstRowAsTitle = Boolean.parseBoolean(namevalue[1]);
				} else if (namevalue[0].equalsIgnoreCase("delimiter")) {
					delimiter = namevalue[1].charAt(0);
				} else if (namevalue[0].equalsIgnoreCase("smartQuote")) {
					smartQuote = namevalue[1].charAt(0);
					// } else if (namevalue[0].equalsIgnoreCase("parseMode")) {
					// parseMode = PARSE_MODE.valueOf(namevalue[1].toUpperCase());
				} else if (namevalue[0].equalsIgnoreCase("filePattern")) {
					filePattern = namevalue[1].trim();
				} else if (namevalue[0].equalsIgnoreCase("subFolder")) {
					subFolder = Boolean.parseBoolean(namevalue[1].toLowerCase());
				}
			}
		}

		if (properties != null) {
			charset = properties.getProperty("charset", charset);
			startRow = Integer.parseInt(properties.getProperty("startRow", "" + startRow));
			firstRowAsTitle = Boolean.parseBoolean(properties.getProperty("firstRowAsTitle", "" + firstRowAsTitle).toLowerCase());
			delimiter = properties.getProperty("delimiter", "" + delimiter).charAt(0);
			smartQuote = properties.getProperty("smartQuote", "" + smartQuote).charAt(0);
			// parseMode = PARSE_MODE.valueOf(properties.getProperty("parseMode", parseMode.name()).toUpperCase());
			filePattern = properties.getProperty("filePattern", filePattern);
			subFolder = Boolean.parseBoolean(properties.getProperty("subFolder", "" + subFolder).toLowerCase());
		}

		String name = dirOrFile.isDirectory() ? dirOrFile.getName() : dirOrFile.getParentFile().getName();
		workDataCatalog = new TxtDataCatalog(name, definition, dirOrFile, filePattern, subFolder, charset, delimiter, smartQuote, startRow, firstRowAsTitle);
		return workDataCatalog;
	}

	public void close() throws DataException {
		List<DataTable> all = workDataCatalog.getTables();
		for (DataTable txtDataTable : all) {
			txtDataTable.close();
		}
		
		workDataCatalog.close();
		workDataCatalog = null;
	}
}
