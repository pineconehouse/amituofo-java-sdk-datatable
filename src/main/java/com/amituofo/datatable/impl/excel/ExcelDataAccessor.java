package com.amituofo.datatable.impl.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.amituofo.common.util.FileUtils;
import com.amituofo.datatable.DataAccessor;
import com.amituofo.datatable.DataException;
import com.amituofo.datatable.DefinitionException;

public class ExcelDataAccessor implements DataAccessor<ExcelDataTableDefinition> {
	private File file = null;
	private File copyForReadWrite = null;

	private ExcelDataCatalog workDataCatalog = null;

	// private Workbook workbook = null;
	// private boolean firstRowAsTitle = false;
	// private int startReadRow = 0;
	// private int startReadColumn = 0;
	// private int maxReadRow = 1048576;// 65535;
	// private int maxReadColumn = 16384;// 256;
	// private int startWriteRow = 0;
	// private int startWriteColumn = 0;

	public ExcelDataAccessor() {
	}

	@Override
	public ExcelDataCatalog open(String url) throws DataException, DefinitionException {
		return open(null, url, null);
	}

	@Override
	public ExcelDataCatalog open(String url, Properties properties) throws DataException, DefinitionException {
		return open(null, url, properties);
	}

	@Override
	public ExcelDataCatalog open(ExcelDataTableDefinition definition, String url) throws DataException, DefinitionException {
		return open(definition, url, null);
	}

	@Override
	public ExcelDataCatalog open(ExcelDataTableDefinition definition, String url, Properties properties) throws DataException, DefinitionException {
		if (url == null || url.length() == 0) {
			throw new DataException("Invalid url " + url);
		}

		Workbook workbook = null;
		boolean firstRowAsTitle = false;
		int startReadRow = 0;
		int startReadColumn = 0;
		// int maxReadRow = 1048576;// 65535;
		// int maxReadColumn = 16384;// 256;
		int startWriteRow = 0;
		int startWriteColumn = 0;

		int indexFileend = url.indexOf('?');
		if (indexFileend == -1) {
			file = new File(url);
		} else {
			String paramPart = url.substring(indexFileend + 1);
			// 8="file:///"
			String filepath = url.substring(8, indexFileend);// .toLowerCase().replace("file:///", "");
			file = new File(filepath);
			String[] params = paramPart.split("&");
			for (String param : params) {
				String[] namevalue = param.split("=");
				if (namevalue.length != 2) {
					throw new DataException("Invalid parameter " + param);
				}

				if (namevalue[0].equalsIgnoreCase("firstRowAsTitle")) {
					firstRowAsTitle = Boolean.parseBoolean(namevalue[1]);
				} else if (namevalue[0].equalsIgnoreCase("startReadRow")) {
					startReadRow = Integer.parseInt(namevalue[1]);
				} else if (namevalue[0].equalsIgnoreCase("startReadColumn")) {
					startReadColumn = Integer.parseInt(namevalue[1]);
					// } else if (namevalue[0].equalsIgnoreCase("maxReadRow")) {
					// maxReadRow = Integer.parseInt(namevalue[1]);
					// } else if (namevalue[0].equalsIgnoreCase("maxReadColumn")) {
					// maxReadColumn = Integer.parseInt(namevalue[1]);
				} else if (namevalue[0].equalsIgnoreCase("startWriteRow")) {
					startWriteRow = Integer.parseInt(namevalue[1]);
				} else if (namevalue[0].equalsIgnoreCase("startWriteColumn")) {
					startWriteColumn = Integer.parseInt(namevalue[1]);
				}
			}
		}

		if (properties != null) {
			firstRowAsTitle = Boolean.parseBoolean(properties.getProperty("firstRowAsTitle", "" + firstRowAsTitle).toLowerCase());
			startReadRow = Integer.parseInt(properties.getProperty("startReadRow", "" + startReadRow));
			startReadColumn = Integer.parseInt(properties.getProperty("startReadColumn", "" + startReadColumn));
			// maxReadRow = Integer.parseInt(properties.getProperty("maxReadRow", "" + maxReadRow));
			// maxReadColumn = Integer.parseInt(properties.getProperty("maxReadColumn", "" + maxReadColumn));
			startWriteRow = Integer.parseInt(properties.getProperty("startWriteRow", "" + startWriteRow));
			startWriteColumn = Integer.parseInt(properties.getProperty("startWriteColumn", "" + startWriteColumn));
		}

		if (file.exists() && file.length() != 0) {
			try {
				// copyForReadWrite = File.createTempFile("~" + file.getName(), ".tmp");
				copyForReadWrite = File.createTempFile("~EX", ".tmp");
				FileUtils.copyFile(file, copyForReadWrite);
				workbook = WorkbookFactory.create(copyForReadWrite);
			} catch (Exception e) {
				e.printStackTrace();
				throw new DataException(e);
			}
		} else {
			String name = file.getName().toLowerCase();
			if (name.contains(".xlsx")) {
				workbook = new SXSSFWorkbook();
			} else if (name.contains(".xls")) {
				workbook = new HSSFWorkbook();
			} else {
				workbook = new SXSSFWorkbook();
			}
		}

		workDataCatalog = new ExcelDataCatalog(file.getName(), definition, workbook, firstRowAsTitle, startReadRow, startReadColumn, startWriteRow, startWriteColumn);
		return workDataCatalog;
	}

	@Override
	public void close() throws DataException {
		if (file != null && workDataCatalog != null) {
			try {
				OutputStream out = new FileOutputStream(file);
				workDataCatalog.getWorkbook().write(out);
				out.close();
			} catch (Throwable e) {
				e.printStackTrace();
				throw new DataException(e);
			}
		}
		
		workDataCatalog.close();
		workDataCatalog = null;

		if (copyForReadWrite != null && copyForReadWrite.exists()) {
			if (!copyForReadWrite.delete()) {
				try {
					// Delete the file
					new FileOutputStream(copyForReadWrite).close();
				} catch (Throwable e) {
					e.printStackTrace();
					throw new DataException(e);
				} finally {
					copyForReadWrite.deleteOnExit();
				}
			}
		}

		file = null;
		copyForReadWrite = null;
	}
}
