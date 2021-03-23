package com.amituofo.datatable.utils;

import java.io.File;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.DataFile;
import com.amituofo.datatable.DataTable;
import com.amituofo.datatable.DefinitionException;
import com.amituofo.datatable.impl.excel.ExcelDataFile;
import com.amituofo.datatable.impl.excel.ExcelDataTableDefinition;
import com.amituofo.datatable.impl.fil.FilDataFile;
import com.amituofo.datatable.impl.fil.FilDataTableDefinition;
import com.amituofo.datatable.impl.memory.MemoryDataTable;
import com.amituofo.datatable.impl.memory.MemoryDataTableDefinition;
import com.amituofo.datatable.impl.txt.BsvDataFile;
import com.amituofo.datatable.impl.txt.CsvDataFile;
import com.amituofo.datatable.impl.txt.TsvDataFile;
import com.amituofo.datatable.impl.txt.TxtDataFile;
import com.amituofo.datatable.impl.txt.TxtDataTableDefinition;

public class DataTableBuilder {
	public static class Txt {
		public static DataFile<TxtDataTableDefinition> create(File fileOrDir, String charset, char delimiter, char smartQuote, String filePattern, int startRow,
				boolean firstRowAsTitle) {
			return new TxtDataFile(fileOrDir, charset, delimiter, smartQuote, filePattern, startRow, firstRowAsTitle);
		}

		public static DataFile<TxtDataTableDefinition> create(TxtDataTableDefinition definition, File fileOrDir, String filePattern, int startRow) {
			return new TxtDataFile(definition, fileOrDir, filePattern, startRow);
		}

		public static DataFile<TxtDataTableDefinition> create(File file, String charset, char delimiter, char smartQuote, int startRow, boolean firstRowAsTitle)
				throws DefinitionException {
			return new TxtDataFile(file, charset, delimiter, smartQuote, startRow, firstRowAsTitle);
		}
	}

	public static class Csv {
		public static DataFile<TxtDataTableDefinition> create(File fileOrDir, String charset, int startRow, boolean firstRowAsTitle) {
			return new CsvDataFile(fileOrDir, charset, firstRowAsTitle, startRow);
		}

		public static DataFile<TxtDataTableDefinition> create(File fileOrDir, String charset, boolean firstRowAsTitle) {
			return new CsvDataFile(fileOrDir, charset, firstRowAsTitle);
		}

		public static DataFile<TxtDataTableDefinition> create(TxtDataTableDefinition definition, File fileOrDir, int startRow) throws DefinitionException {
			return new CsvDataFile(definition, fileOrDir, startRow);
		}

		public static DataFile<TxtDataTableDefinition> create(TxtDataTableDefinition definition, File fileOrDir) throws DefinitionException {
			return new CsvDataFile(definition, fileOrDir);
		}

		public static DataFile<TxtDataTableDefinition> create(TxtDataTableDefinition definition, File fileOrDir, String filePattern, int startRow) throws DefinitionException {
			return new CsvDataFile(definition, fileOrDir, filePattern, startRow);
		}
	}

	public static class Tsv {
		public static DataFile<TxtDataTableDefinition> create(File fileOrDir, String charset, int startRow, boolean firstRowAsTitle) {
			return new TsvDataFile(fileOrDir, charset, firstRowAsTitle, startRow);
		}

		public static DataFile<TxtDataTableDefinition> create(File fileOrDir, String charset, boolean firstRowAsTitle) {
			return new TsvDataFile(fileOrDir, charset, firstRowAsTitle);
		}

		public static DataFile<TxtDataTableDefinition> create(TxtDataTableDefinition definition, File fileOrDir, int startRow) throws DefinitionException {
			return new TsvDataFile(definition, fileOrDir, startRow);
		}

		public static DataFile<TxtDataTableDefinition> create(TxtDataTableDefinition definition, File fileOrDir) throws DefinitionException {
			return new TsvDataFile(definition, fileOrDir);
		}

		public static DataFile<TxtDataTableDefinition> create(TxtDataTableDefinition definition, File fileOrDir, String filePattern, int startRow) throws DefinitionException {
			return new TsvDataFile(definition, fileOrDir, filePattern, startRow);
		}
	}

	public static class Bsv {
		public static DataFile<TxtDataTableDefinition> create(File fileOrDir, String charset, int startRow, boolean firstRowAsTitle) {
			return new BsvDataFile(fileOrDir, charset, firstRowAsTitle, startRow);
		}

		public static DataFile<TxtDataTableDefinition> create(File fileOrDir, String charset, boolean firstRowAsTitle) {
			return new BsvDataFile(fileOrDir, charset, firstRowAsTitle);
		}

		public static DataFile<TxtDataTableDefinition> create(TxtDataTableDefinition definition, File fileOrDir, int startRow) throws DefinitionException {
			return new BsvDataFile(definition, fileOrDir, startRow);
		}

		public static DataFile<TxtDataTableDefinition> create(TxtDataTableDefinition definition, File fileOrDir) throws DefinitionException {
			return new BsvDataFile(definition, fileOrDir);
		}

		public static DataFile<TxtDataTableDefinition> create(TxtDataTableDefinition definition, File fileOrDir, String filePattern, int startRow) throws DefinitionException {
			return new BsvDataFile(definition, fileOrDir, filePattern, startRow);
		}
	}

	public static class Fil {
		public static DataFile<FilDataTableDefinition> create(File fileOrDir, String charset, boolean firstRowAsTitle, int[] eachColumnLength) {
			return new FilDataFile(fileOrDir, charset, firstRowAsTitle, eachColumnLength);
		}

		public static DataFile<FilDataTableDefinition> create(File fileOrDir, String charset, int startRow, boolean firstRowAsTitle, int[] eachColumnLength) {
			return new FilDataFile(fileOrDir, charset, startRow, firstRowAsTitle, eachColumnLength);
		}

		public static DataFile<FilDataTableDefinition> create(FilDataTableDefinition definition, File fileOrDir) throws DefinitionException {
			return new FilDataFile(definition, fileOrDir);
		}
	}

	public static class Excel {
		public static DataFile<ExcelDataTableDefinition> create(File file, int startRow, boolean firstRowAsTitle) {
			return new ExcelDataFile(file, startRow, firstRowAsTitle);
		}

		public static DataFile<ExcelDataTableDefinition> create(File file, int startRow, int startColumn, boolean firstRowAsTitle) {
			return new ExcelDataFile(file, startRow, startColumn, firstRowAsTitle);
		}

		public static DataFile<ExcelDataTableDefinition> create(ExcelDataTableDefinition definition, File file, int startRow, boolean firstRowAsTitle) {
			return new ExcelDataFile(definition, file, startRow, firstRowAsTitle);
		}

		public static DataFile<ExcelDataTableDefinition> create(ExcelDataTableDefinition definition, File file, int startRow, int startColumn, boolean firstRowAsTitle) {
			return new ExcelDataFile(definition, file, startRow, startColumn, firstRowAsTitle);
		}
	}

//	public static class JdbcMySql {
//		public static JdbcDataAccessor create(String ip, String user, String password, String defaultCatalog) {
//			return new MySqlDataAccessor(ip, "3306", user, password, defaultCatalog);
//		}
//
//		public static JdbcDataAccessor create(String ip, String port, String user, String password, String defaultCatalog) {
//			return new MySqlDataAccessor(ip, port, user, password, defaultCatalog);
//		}
//	}
//
//	public static class JdbcSqlServer {
//		public static JdbcDataAccessor create(String ip, String user, String password, String defaultCatalog) {
//			return new SqlServerDataAccessor(ip, "3306", user, password, defaultCatalog);
//		}
//
//		public static JdbcDataAccessor create(String ip, String port, String user, String password, String defaultCatalog) {
//			return new SqlServerDataAccessor(ip, port, user, password, defaultCatalog);
//		}
//	}
//
//	public static class Jdbc {
//		public static JdbcDataAccessor create(String url, String user, String password, String defaultCatalog, String driverClassName) {
//			return new JdbcDataAccessor(url, user, password, defaultCatalog, driverClassName);
//		}
//		
//		public static JdbcDataAccessor create(String url, Properties properties) {
//			return new JdbcDataAccessor(url, properties);
//		}
//	}

	public static class Memory {
		public static DataTable create(String name) throws DataException, DefinitionException {
			return new MemoryDataTable(name);
		}

		public static DataTable create(MemoryDataTableDefinition definition) throws DataException, DefinitionException {
			return new MemoryDataTable(definition);
		}

		public static DataTable create(DataTable asDataTable) throws DataException, DefinitionException {
			return new MemoryDataTable(asDataTable);
		}
	}

}
