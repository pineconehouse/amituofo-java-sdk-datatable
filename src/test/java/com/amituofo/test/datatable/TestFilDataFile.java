package com.amituofo.test.datatable;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.amituofo.common.kit.PrettyRecordPrinter;
import com.amituofo.common.util.FileUtils;
import com.amituofo.datatable.DataCatalog;
import com.amituofo.datatable.DataException;
import com.amituofo.datatable.DataField;
import com.amituofo.datatable.DataRecord;
import com.amituofo.datatable.DataRecordFilter;
import com.amituofo.datatable.DataTable;
import com.amituofo.datatable.DataTableDefinition;
import com.amituofo.datatable.DefinitionException;
import com.amituofo.datatable.InvalidDataException;
import com.amituofo.datatable.impl.fil.FilDataDefinitionFactory;
import com.amituofo.datatable.impl.fil.FilDataFile;
import com.amituofo.datatable.impl.txt.CsvDataFile;

public class TestFilDataFile extends BasicTestCase {
	private static final int[] COLUMN_LENGTHS = new int[] { 16, 10, 38, 22 };
	public static File filProfitFile = newFile("Profit_Center.fil");
	public static File filProfitErrFile = newFile("Profit_Center_Err.fil");
	public static File filProfitFileCopy = newFile("Profit_CenterCopy.fil");
	public static File csvProfitFile = newFile("Profit_Center.csv");
	
	public static File filProfitDef = newFile("DataFileDef_(FILProfit_Center).xml");

	@Override
	public void test_okcase_PrintAllInFile() throws DataException, DefinitionException {
		FilDataFile fil = new FilDataFile(filProfitFile, "utf-8", true, COLUMN_LENGTHS);
		DataCatalog dc = fil.openCatalog();
		DataTable dt = dc.getTable(0);
		DataTableDefinition def = dt.getDefinition();

		DataRecord row = dt.readFirstRecord();
		printer.appendRecord(def.getColumnNames());
		printer.appendCuttingLine('-');
		while (row != null) {
//			System.out.println((def.rending(row)));
			printer.appendRecord(row.toStringValues());
			// row.get(2).setValue("123,456");
			// System.out.println(dt.currentReadingLine() + "\t" + row.toString());
			// System.out.println(row.toString());
			// System.out.println(txtmfa.currentReadLine() + "\t" + row.toString('\t'));
			// System.out.println(txtmfa.currentReadLine() + "\t" + Arrays.asList(row.getValues()));
			// System.out.println(txtmfa.currentReadLine() + "\t" + row.append(field));
			// System.out.println(txtmfa.currentReadLine() + "\t" + row.getFieldNames());
			row = dt.readNextRecord();
			rowcnt++;
		}
		fil.closeCatalog();
	}

	@Override
	public void test_okcase_PrintAllInFile_ByXmlDef() throws DefinitionException, DataException {
		FilDataFile fil = new FilDataFile(FilDataDefinitionFactory.parse(filProfitDef), filProfitFile);
		DataCatalog dc = fil.openCatalog();
		DataTable dt = dc.getTable(0);
		DataTableDefinition def = dt.getDefinition();

		DataRecord row = dt.readFirstRecord();
		printer.appendRecord(def.getColumnNames());
		printer.appendCuttingLine('-');
		while (row != null) {
//			System.out.println((def.rending(row)));
			printer.appendRecord(row.toStringValues());
			row = dt.readNextRecord();
			rowcnt++;
			if (rowcnt>100) {
				break;
			}
		}
		fil.closeCatalog();
	}

	@Override
	public void test_okcase_PrintInvalid_ByXmlDef() throws DefinitionException, DataException {
		FilDataFile fil;
		try {
			fil = new FilDataFile(FilDataDefinitionFactory.parse(filProfitDef), filProfitErrFile);
		} catch (DefinitionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		DataCatalog dc;
		try {
			dc = fil.openCatalog();
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		DataTable dt = dc.getTable(0);
		DataTableDefinition def = dt.getDefinition();

		DataRecord row;
		try {
			row = dt.readFirstRecord();
			printer.appendRecord(def.getColumnNames());
			printer.appendCuttingLine('-');
			while (row != null) {
				try {
					def.validate(row);
				} catch (InvalidDataException e1) {
					System.out.println(dt.currentReadingLine() + " line:" + e1.getMessage());
				}
				
				printer.appendRecord(row.toStringValues());
				row = dt.readNextRecord();
				rowcnt++;
			}
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			fil.closeCatalog();
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void test_okcase_SpeedOfReadBigData() throws DataException, DefinitionException {
		// TODO Auto-generated method stub

	}

	@Override
	public void test_okcase_PrintReadStartFromRow30_Notitle() throws DataException {
		// TODO Auto-generated method stub

	}

	@Override
	public void test_okcase_ReadBlankFile() throws DataException {
		// TODO Auto-generated method stub

	}

	@Override
	public void test_okcase_ReadNotExistsFile() throws DataException {
		// TODO Auto-generated method stub

	}

	@Override
	public void test_okcase_PrintTop10OfTableDataInCatalog() throws DefinitionException, DataException {
		// TODO Auto-generated method stub

	}

	@Override
	public void test_okcase_WriteFormCurrentFileType2Txt() throws DataException, DefinitionException {
		csvProfitFile.delete();

		FilDataFile fil = new FilDataFile(filProfitFile, "utf-8", true, COLUMN_LENGTHS);
		CsvDataFile csv = new CsvDataFile(csvProfitFile, "utf-8", true);
		DataTable fildt = fil.openCatalog().getTable(0);
		DataTable csvdt = csv.openCatalog().getTable(0);
		DataTableDefinition def = fildt.getDefinition();

		csvdt.resetColumn(def);

		DataRecord row = fildt.readFirstRecord();

		csvdt.writeTitle();
		while (row != null) {
			csvdt.appendRecord(row, null);
			row = fildt.readNextRecord();
			rowcnt++;
		}
		fil.closeCatalog();
		csv.closeCatalog();
	}

	@Override
	public void test_okcase_WriteFormCurrentFileTypeWithDefaultDef2Txt_ByWriteAllFunction() throws DataException, DefinitionException {
		csvProfitFile.delete();

		FilDataFile fil = new FilDataFile(filProfitFile, "utf-8", true, COLUMN_LENGTHS);
		CsvDataFile csv = new CsvDataFile(csvProfitFile, "utf-8", true);
		DataTable fildt = fil.openCatalog().getTable(0);
		DataTable csvdt = csv.openCatalog().getTable(0);

		csvdt.resetColumn(fildt.getDefinition());

		csvdt.appendAll(fildt, true, null);

		fil.closeCatalog();
		csv.closeCatalog();
	}

	@Override
	public void test_okcase_WriteFormCurrentFileTypeWithXmlDef2Txt_ByWriteAllFunction() throws DataException, DefinitionException {
		csvProfitFile.delete();

		FilDataFile fil = new FilDataFile(FilDataDefinitionFactory.parse(filProfitDef), filProfitFile);
		CsvDataFile csv = new CsvDataFile(csvProfitFile, "utf-8", true);
		DataTable fildt = fil.openCatalog().getTable(0);
		DataTable csvdt = csv.openCatalog().getTable(0);

		csvdt.resetColumn(fildt.getDefinition());

		csvdt.appendAll(fildt, true, null);

		fil.closeCatalog();
		csv.closeCatalog();
	}

	@Override
	public void test_okcase_WriteFormXmlDefTxt2CurrentFileType_ByWriteAllFunction() throws DataException, DefinitionException {
		// TODO Auto-generated method stub

	}

	@Override
	public void test_okcase_WriteToNewTabeAfterModification() throws DataException, DefinitionException {
		FilDataFile fil = new FilDataFile(FilDataDefinitionFactory.parse(filProfitDef), filProfitFile);
		DataCatalog c = fil.openCatalog();
		DataTable fildt = c.getTable(filProfitFile.getName().toUpperCase());
		DataTableDefinition def = fildt.getDefinition();
		DataTableDefinition cdef = def;
		DataTable newdt = c.createTable(filProfitFileCopy.getName(), cdef);
		newdt.truncate();

		DataRecord row = fildt.readFirstRecord();

		newdt.writeTitle();
		while (row != null) {
			DataField fd = row.get(def.getColumnIndex("PC_Profit_Center_Code"));
			fd.setValue(((BigDecimal) fd.getValue()).add(new BigDecimal(1000)));

			newdt.appendRecord(row, null);
			row = fildt.readNextRecord();
			rowcnt++;
		}
		fil.closeCatalog();
	}

	@Override
	public void test_okcase_PrintReadFirstRowTwice() throws DataException {
		// TODO Auto-generated method stub

	}

	@Override
	public void test_okcase_truncate() throws DataException, DefinitionException, IOException {
		// TODO Auto-generated method stub
		File f = filProfitFile;
		File copyForReadWrite = File.createTempFile("~EX", ".fil");
		FileUtils.copyFile(f, copyForReadWrite);
		
		FilDataFile fil = new FilDataFile(copyForReadWrite, "utf-8", true, COLUMN_LENGTHS);

		DataCatalog dc = fil.openCatalog();
		DataTable dt = dc.getTable(0);
		dt.truncate();
		
		fil.closeCatalog();
	}

	@Override
	public void test_okcase_RecordFilter() throws DataException, DefinitionException, IOException {
		FilDataFile fil = new FilDataFile(filProfitFile, "utf-8", true, COLUMN_LENGTHS);
		DataCatalog dc = fil.openCatalog();
		DataTable dt = dc.getTable(0);

		DataTableDefinition def = dt.getDefinition();
		PrettyRecordPrinter printer = new PrettyRecordPrinter();
		printer.appendTitle("Orginal Table:");
		printer.getLayout().enableTopBorder('/', '=', '\\');
		printer.appendRecord(def.getColumnNames());
		printer.appendCuttingLine('-');
		DataRecord row = dt.readFirstRecord();
		while (row != null) {
			printer.appendRecord(row.toStringValues());
			row = dt.readNextRecord();
			rowcnt++;
		}

		printer.appendCuttingLine('-');
		printer.appendBlank();
		printer.printout();
		// -----------------------------------------------------------------------------------------------
		dt.setFilter(new DataRecordFilter() {
			@Override
			public boolean isMatched(DataRecord row) {
				// String category = (String) row.getValue(2);
				// return (category.startsWith("4"));
				return (("FMS".equals(row.getValue(1))||"FSG".equals(row.getValue(1)))&& ((String)row.getValue(2)).contains("JOC"));
			}
		});
		def = dt.getDefinition();
		printer = new PrettyRecordPrinter();
		printer.appendTitle("Filted1:");
		printer.getLayout().enableTopBorder('/', '=', '\\');
		printer.appendRecord(def.getColumnNames());
		printer.appendCuttingLine('-');
		row = dt.readFirstRecord();
		while (row != null) {
			printer.appendRecord(row.toStringValues());
			row = dt.readNextRecord();
			rowcnt++;
		}

		printer.appendCuttingLine('-');
		printer.appendBlank();
		printer.printout();
		
		// -----------------------------------------------------------------------------------------------

		dt.setFilter(new DataRecordFilter() {
			@Override
			public boolean isMatched(DataRecord row) {
				return (("FMS".equals(row.getValue(1))||"FSG".equals(row.getValue(1)))&& ((String)row.getValue(2)).contains("JOC"));
			}
		});
		def = dt.getDefinition();
		printer = new PrettyRecordPrinter();
		printer.appendTitle("Filted2:");
		printer.getLayout().enableTopBorder('/', '=', '\\');
		printer.appendRecord(def.getColumnNames());
		printer.appendCuttingLine('-');
		List<DataRecord> rows = dt.readFirstRecords(3);
		while (rows != null) {
			for (DataRecord rec : rows) {
				printer.appendRecord(rec.toStringValues());
			}
			rowcnt +=rows.size();
			rows = dt.readNextRecords(2);
		}

		printer.appendCuttingLine('-');
		printer.printout();
		// -----------------------------------------------------------------------------------------------

		fil.closeCatalog();
	}
	
}
