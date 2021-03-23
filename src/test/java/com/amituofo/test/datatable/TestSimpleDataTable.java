package com.amituofo.test.datatable;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.amituofo.common.kit.PrettyRecordPrinter;
import com.amituofo.datatable.DataAccessor;
import com.amituofo.datatable.DataCatalog;
import com.amituofo.datatable.DataException;
import com.amituofo.datatable.DataRecord;
import com.amituofo.datatable.DataRecordFilter;
import com.amituofo.datatable.DataTable;
import com.amituofo.datatable.DataTableDefinition;
import com.amituofo.datatable.DefinitionException;
import com.amituofo.datatable.InvalidDataException;
import com.amituofo.datatable.impl.fil.FilDataFile;
import com.amituofo.datatable.impl.memory.MemoryDataTable;
import com.amituofo.datatable.impl.memory.MemoryDataTableDefinition;
import com.amituofo.datatable.impl.txt.CsvDataFile;
import com.amituofo.datatable.impl.txt.TsvDataFile;
import com.amituofo.datatable.impl.txt.TxtDataAccessor;
import com.amituofo.datatable.impl.txt.TxtDataDefinitionFactory;
import com.amituofo.datatable.utils.Moulding;

public class TestSimpleDataTable extends BasicTestCase {
	public static File txtTsvGCM = newFile("GlobalCustomerMaster.tsv");
	public static File txtGCM = newFile("GlobalCustomerMaster.csv");
	public static File txtGCMPart = newFile("GCMPart.csv");

	public static File txtGCM50W = newFile("GlobalCustomerMaster50W.csv");
	public static File txtNoData = newFile("blank.csv");
	public static File txtNotExists = newFile("txtNotExists.csv");
	public static File txtRevenue = newFile("Revenue.csv");
	public static File txtRevenueCopy = newFile("RevenueCopy.csv");
	public static File txtTsvRevenue = newFile("Revenue.tsv");
	public static File csvDefRevenue = newFile("DataFileDef_(CSVRevenue).xml");
	public static File csvDefGCM = newFile("DataFileDef_(CSVGCM).xml");
	public static File txtRevenue20W = newFile("Revenue20W.csv");
	public static File txtFilGCM = newFile("GlobalCustomerMaster.fil");

	public void test_okcase_PrintAllInFile() throws DataException, DefinitionException {
		CsvDataFile csv = new CsvDataFile(txtGCMPart, "utf-8", true);
		DataCatalog dc = csv.openCatalog();
		DataTable dt = dc.getTable(0);

		DataTable stbl = new MemoryDataTable(new MemoryDataTableDefinition(dt.getDefinition()));
		for (int i = 0; i < 1; i++) {
			stbl.appendAll(dt, true, null);
		}

		DataRecord row = stbl.readFirstRecord();
		DataTableDefinition def = stbl.getDefinition();
		PrettyRecordPrinter printer = new PrettyRecordPrinter();
		printer.getLayout().enableTopBorder('/', '=', '\\');
		printer.appendRecord(def.getColumnNames());
		printer.appendCuttingLine('-');
		while (row != null) {
			printer.appendRecord(row.toStringValues());
			row = stbl.readNextRecord();
			rowcnt++;
		}

		printer.appendBlank();
		printer.printout();

		printer = new PrettyRecordPrinter();
		printer.getLayout().enableTopBorder('/', '=', '\\');
		DataTable newStbl = Moulding.transpose(stbl, false);
		row = newStbl.readFirstRecord();
		def = newStbl.getDefinition();
		printer.appendRecord(def.getColumnNames());
		printer.appendCuttingLine('-');
		while (row != null) {
			printer.appendRecord(row.toStringValues());
			row = newStbl.readNextRecord();
		}
		printer.printout();

		csv.closeCatalog();
	}

	public void test_okcase_PrintAllInFile_ByXmlDef() throws DataException, DefinitionException {
		CsvDataFile csv = new CsvDataFile(TxtDataDefinitionFactory.parse(csvDefGCM), txtGCM);
		DataCatalog dc = csv.openCatalog();
		DataTable dt = dc.getTable(0);

		DataRecord row = dt.readFirstRecord();
		DataTableDefinition def = dt.getDefinition();
		printer.appendRecord(def.getColumnNames());
		printer.appendCuttingLine('-');
		while (row != null) {
			// System.out.println((def.rending(row)));
			printer.appendRecord(row.toStringValues());
			row = dt.readNextRecord();
			rowcnt++;
		}
		csv.closeCatalog();
	}

	public void test_okcase_PrintInvalid_ByXmlDef() throws DataException, DefinitionException {
		DataTable dt = null;
		CsvDataFile csv = new CsvDataFile(TxtDataDefinitionFactory.parse(csvDefRevenue), txtRevenue);
		DataCatalog dc = csv.openCatalog();

		// txtmfa = new TxtDataAccessor();
		// DataCatalog dc = txtmfa.open("file:///" + txtRevenue.getPath() + "?charset=utf-8&firstRowAsTitle=true&delimiter=,&smartQuote=\"");

		dt = dc.getTable(0);
		DataTableDefinition def = dt.getDefinition();

		DataRecord row = null;
		try {
			row = dt.readFirstRecord();
		} catch (DataException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		printer.appendRecord(def.getColumnNames());
		printer.appendCuttingLine('-');
		while (row != null) {
			try {
				def.validate(row);
			} catch (InvalidDataException e1) {
				System.out.println(dt.currentReadingLine() + " line:" + e1.getMessage());
			}
			printer.appendRecord(row.toStringValues());
			// System.out.println(row.toString());
			row = null;
			try {
				row = dt.readNextRecord();
			} catch (DataException e) {
				e.printStackTrace();
			}
			rowcnt++;
		}

		System.out.println();
	}

	public void test_okcase_SpeedOfReadBigData2() throws DataException, DefinitionException {
		DataAccessor txtmfa = new TxtDataAccessor();
		DataCatalog dc = txtmfa.open(TxtDataDefinitionFactory.parse(csvDefRevenue), "file:///" + txtRevenue20W.getPath()
				+ "?charset=utf-8&firstRowAsTitle=true&delimiter=,&smartQuote=\"");
		DataTable dt = dc.getTable(0);
		DataTableDefinition def = dt.getDefinition();

		DataRecord row = dt.readFirstRecord();
		while (row != null) {
			try {
				def.validate(row);
			} catch (InvalidDataException e1) {
				System.out.println(dt.currentReadingLine() + " line:" + e1.getMessage());
			}
			row = dt.readNextRecord();
			rowcnt++;
		}
		txtmfa.close();
	}

	public void test_okcase_SpeedOfReadBigData() throws DataException, DefinitionException {
		// DataAccessor txtmfa = new TxtDataAccessor();
		// DataCatalog dc = txtmfa.open("file:///" + txtGCM50W.getPath() + "?charset=utf-8&firstRowAsTitle=true&delimiter=,&smartQuote=\"");
		CsvDataFile csv = new CsvDataFile(txtGCMPart, "utf-8", true);
		DataCatalog dc = csv.openCatalog();
		DataTable dt = dc.getTable(0);

		long t1 = System.currentTimeMillis();
		DataTable stbl = new MemoryDataTable(new MemoryDataTableDefinition(dt.getDefinition()));
		for (int i = 0; i < 10000; i++) {
			stbl.appendAll(dt, true, null);
		}
		long t2 = System.currentTimeMillis();
		long usetm = t2 - t1;
		System.out.println("TimeMillis(appendAll)=" + (usetm < 31 ? usetm : usetm - 31));

		t1 = System.currentTimeMillis();
		DataRecord row = stbl.readFirstRecord();
		while (row != null) {
			row = stbl.readNextRecord();
			rowcnt++;
		}
		t2 = System.currentTimeMillis();
		usetm = t2 - t1;
		System.out.println("TimeMillis(readAll)=" + (usetm < 31 ? usetm : usetm - 31));

		dc.close();
	}

	public void test_okcase_PrintReadStartFromRow30_Notitle() throws DataException, DefinitionException {
		CsvDataFile csv = new CsvDataFile(txtGCM, "utf-8", false, 30);

		// DataAccessor txtmfa = new TxtDataAccessor();
		// DataCatalog dc = txtmfa.open("file:///" + txtGCM.getPath() +
		// "?charset=utf-8&startRow=30&firstRowAsTitle=false&delimiter=,&smartQuote=\"&filePattern=*.csv&subFolder=false");
		DataCatalog dc = csv.openCatalog();
		DataTable dt = dc.getTable(0);
		DataTableDefinition def = dt.getDefinition();

		DataRecord row = dt.readFirstRecord();
		printer.appendRecord(def.getColumnNames());
		printer.appendCuttingLine('-');
		while (row != null) {
			printer.appendRecord(row.toStringValues());
			row = dt.readNextRecord();
			rowcnt++;
		}
		dc.close();
	}

	public void test_okcase_ReadBlankFile() throws DataException, DefinitionException {
		// DataAccessor txtmfa = new TxtDataAccessor();
		// DataCatalog dc = txtmfa.open("file:///" + txtNoData.getPath() + "?charset=utf-8&firstRowAsTitle=true&delimiter=,&smartQuote=\"");
		CsvDataFile csv = new CsvDataFile(txtNoData, "utf-8", true);
		DataCatalog dc = csv.openCatalog();

		DataTable dt = dc.getTable(0);
		DataTableDefinition def = dt.getDefinition();

		DataRecord row = dt.readFirstRecord();
		if (row != null) {
			rowcnt++;
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
		}
		dc.close();
	}

	public void test_okcase_ReadNotExistsFile() throws DataException, DefinitionException {
		txtNotExists.delete();

		// DataAccessor txtmfa = new TxtDataAccessor();
		// DataCatalog dc = txtmfa.open("file:///" + txtNotExists.getPath() + "?charset=utf-8&firstRowAsTitle=true&delimiter=,&smartQuote=\"");

		CsvDataFile csv = new CsvDataFile(txtNotExists, "utf-8", true);
		DataCatalog dc = csv.openCatalog();

		DataTable dt = dc.getTable(0);
		DataTableDefinition def = dt.getDefinition();

		DataRecord row = dt.readFirstRecord();
		if (row != null) {
			rowcnt++;
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
		}
		dc.close();
	}

	public void test_okcase_PrintTop10OfTableDataInCatalog() throws DefinitionException, DataException {
		File dir = newFile("CatalogTest\\");

		// DataAccessor sourceda = new TxtDataAccessor();
		// DataCatalog sourcedc = sourceda.open("file:///" + dir.getPath()
		// + "?charset=utf-8&startRow=1&firstRowAsTitle=true&delimiter=,&smartQuote=\"&filePattern=*.csv&subFolder=false");

		CsvDataFile csv = new CsvDataFile(dir, "utf-8", true);
		DataCatalog dc = csv.openCatalog();

		List<DataTable> sourcedts = dc.getTables();

		PrettyRecordPrinter printerSum = new PrettyRecordPrinter();
		printerSum.appendCuttingLine('-');
		for (DataTable sourcedt : sourcedts) {
			printerSum.appendRecord("Read from [" + sourcedt.getName() + "]");
		}
		printerSum.appendCuttingLine('-');
		printerSum.appendBlank();
		printerSum.printout();

		for (DataTable sourcedt : sourcedts) {
			DataTableDefinition def = sourcedt.getDefinition();

			PrettyRecordPrinter printer = new PrettyRecordPrinter();
			printer.getLayout().enableTopBorder('/', '=', '\\');

			printer.appendTitle("Read from [" + sourcedt.getName() + "]");
			List<DataRecord> rows = sourcedt.readFirstRecords(5);
			if (rows != null) {
				printer.appendRecord(def.getColumnNames());
				printer.appendCuttingLine('-');
				for (DataRecord row : rows) {
					printer.appendRecord(row.toStringValues());
				}
			}

			rows = sourcedt.readNextRecords(5);
			if (rows != null) {
				for (DataRecord row : rows) {
					printer.appendRecord(row.toStringValues());
				}
			}

			printer.appendCuttingLine('-');
			printer.appendBlank();
			printer.printout();
		}

		dc.dropTable("Copy of Region_Master.csv");

		dc.close();
	}

	public void test_okcase_WriteFormCurrentFileType2Txt() throws DataException, DefinitionException {
		txtTsvGCM.delete();

		CsvDataFile csv = new CsvDataFile(txtGCM, "utf-8", true);
		TsvDataFile tsv = new TsvDataFile(txtTsvGCM, "utf-8", true);
		DataTable tsvdt = tsv.openCatalog().getTable(0);

		DataTable dt = csv.openCatalog().getTable(0);
		DataTableDefinition def = dt.getDefinition();
		def.hiddenColumn("JAPAN_CUSTOMER_NAME_JP");

		tsvdt.resetColumn(dt.getDefinition());

		DataRecord row = dt.readFirstRecord();

		tsvdt.writeTitle();
		while (row != null) {
			tsvdt.appendRecord(row, null);
			row = dt.readNextRecord();
			rowcnt++;
		}
		csv.closeCatalog();
		tsv.closeCatalog();
	}

	public void test_okcase_WriteFormCurrentFileTypeWithDefaultDef2Txt_ByWriteAllFunction() throws DataException, DefinitionException {
		txtTsvGCM.delete();

		TsvDataFile tsv = new TsvDataFile(txtTsvGCM, "utf-8", true);
		CsvDataFile csv = new CsvDataFile(txtGCM, "utf-8", true);
		DataTable tsvdt = tsv.openCatalog().getTable(0);
		DataTable csvdt = csv.openCatalog().getTable(0);

		tsvdt.resetColumn(csvdt.getDefinition());

		tsvdt.appendAll(csvdt, true, null);

		tsv.closeCatalog();
		csv.closeCatalog();
	}

	public void test_okcase_WriteFormCurrentFileTypeWithDefaultDef2Fil_ByWriteAllFunction() throws DataException, DefinitionException {
		txtFilGCM.delete();

		CsvDataFile csv = new CsvDataFile(txtGCM, "utf-8", true);
		// CsvDataFile csv = new CsvDataFile(TxtDataDefinitionFactory.parse(csvDefGCM), txtGCM);
		FilDataFile fil = new FilDataFile(txtFilGCM, "utf-8", true, null);
		DataTable csvdt = csv.openCatalog().getTable(0);
		DataTable fildt = fil.openCatalog().getTable(0);

		// fildt.resetColumnLike(csvdt.getDefinition());

		fildt.appendAll(csvdt, true, null);

		fil.closeCatalog();
		csv.closeCatalog();
	}

	@Override
	public void test_okcase_WriteFormXmlDefTxt2CurrentFileType_ByWriteAllFunction() throws DataException, DefinitionException {
		// TODO Auto-generated method stub

	}

	public void test_okcase_WriteFormCurrentFileTypeWithXmlDef2Txt_ByWriteAllFunction() throws DataException, DefinitionException {

		txtTsvRevenue.delete();

		TsvDataFile tsv = new TsvDataFile(txtTsvRevenue, "utf-8", true);
		CsvDataFile csv = new CsvDataFile(TxtDataDefinitionFactory.parse(csvDefRevenue), txtRevenue);
		DataTable tsvdt = tsv.openCatalog().getTable(0);
		DataTable csvdt = csv.openCatalog().getTable(0);

		tsvdt.resetColumn(csvdt.getDefinition());

		tsvdt.appendAll(csvdt, true, null);

		tsv.closeCatalog();
		csv.closeCatalog();
	}

	public void test_okcase_WriteToNewTabeAfterModification() throws DataException, DefinitionException {

		CsvDataFile csv = new CsvDataFile(TxtDataDefinitionFactory.parse(csvDefRevenue), new File(basicDir));
		DataCatalog csvdc = csv.openCatalog();
		DataTable csvdt = csvdc.getTable(txtRevenue.getName());

		csvdc.dropTable(txtRevenueCopy.getName());
		DataTable csvCopydt = csvdc.createTable(txtRevenueCopy.getName(), TxtDataDefinitionFactory.parse(csvDefRevenue));

		csvCopydt.appendAll(csvdt, true, null);

		csv.closeCatalog();
	}

	public void test_okcase_PrintReadFirstRowTwice() throws DataException, DefinitionException {
		// DataAccessor txtmfa = new TxtDataAccessor();
		// DataCatalog dc = txtmfa.open("file:///" + txtGCM.getPath() + "?charset=utf-8&firstRowAsTitle=true&delimiter=,&smartQuote=\"");
		CsvDataFile csv = new CsvDataFile(txtGCM, "utf-8", true);
		DataCatalog dc = csv.openCatalog();

		DataTable dt = dc.getTable(0);
		DataTableDefinition def = dt.getDefinition();

		DataRecord row = dt.readFirstRecord();
		printer.appendRecord(def.getColumnNames());
		printer.appendCuttingLine('-');
		printer.appendRecord(row.toStringValues());
		row = dt.readFirstRecord();
		printer.appendRecord(row.toStringValues());

		dc.close();
	}

	public void test_okcase_PrintInsertColumn() throws DataException, DefinitionException {
		// DataAccessor txtmfa = new TxtDataAccessor();
		// DataCatalog dc = txtmfa.open("file:///" + txtGCM.getPath() + "?charset=utf-8&firstRowAsTitle=true&delimiter=,&smartQuote=\"");
		CsvDataFile csv = new CsvDataFile(txtGCM, "utf-8", true);
		DataCatalog dc = csv.openCatalog();

		DataTable dt = dc.getTable(0);
		DataTableDefinition def = dt.getDefinition();
		// def.addColumn(new TxtDataFieldDefinition("newC", -1, new StringFieldType(), ',', '"', false, true, null, null));
		// def.insertColumn(2, new TxtDataFieldDefinition("newCC", -1, new StringFieldType(), ',', '"', false, true, null, null));
		def.hiddenColumn("FGCSJOC");
		def.hiddenColumn("SBGSJOC");
		def.hiddenColumn("DUNS_CODE");
		def.hiddenColumn("ASIA_CODE");
		def.hiddenColumn("JAPAN_CUSTOMER_NAME_JP");

		DataRecord row = dt.readFirstRecord();
		row.set(4, "value");
		printer.appendRecord(def.getColumnNames());
		printer.appendCuttingLine('-');
		printer.appendRecord(row.toStringValues());
		row = dt.readFirstRecord();
		printer.appendRecord(row.toStringValues());
		row = dt.readNextRecord();
		printer.appendRecord(row.toStringValues());

		dc.close();
	}

	@Override
	public void test_okcase_truncate() throws DataException, DefinitionException, IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void test_okcase_RecordFilter() throws DataException, DefinitionException, IOException {
		CsvDataFile csv = new CsvDataFile(txtGCMPart, "utf-8", true);
		DataCatalog dc = csv.openCatalog();
		DataTable dt = dc.getTable(0);

		// --------------------------------------------------------------------------------------
		DataTable stbl = new MemoryDataTable(new MemoryDataTableDefinition(dt.getDefinition()));
		stbl.appendAll(dt, true, null);

		// --------------------------------------------------------------------------------------
		DataTableDefinition def = stbl.getDefinition();
		PrettyRecordPrinter printer = new PrettyRecordPrinter();
		printer.appendTitle("Orginal Table:");
		printer.getLayout().enableTopBorder('/', '=', '\\');
		printer.appendRecord(def.getColumnNames());
		printer.appendCuttingLine('-');
		DataRecord row = stbl.readFirstRecord();
		while (row != null) {
			printer.appendRecord(row.toStringValues());
			row = stbl.readNextRecord();
			rowcnt++;
		}

		printer.appendCuttingLine('-');
		printer.appendBlank();
		printer.printout();

		// --------------------------------------------------------------------------------------
		stbl.setFilter(new DataRecordFilter() {
			@Override
			public boolean isMatched(DataRecord row) {
				String category = (String) row.getValue(3);
				return (row.getValue(2) != null && row.getValue(1) != null && category.startsWith("9"));
			}
		});
		def = stbl.getDefinition();
		printer = new PrettyRecordPrinter();
		printer.appendTitle("Filted1:");
		printer.getLayout().enableTopBorder('/', '=', '\\');
		printer.appendRecord(def.getColumnNames());
		printer.appendCuttingLine('-');
		row = stbl.readFirstRecord();
		while (row != null) {
			printer.appendRecord(row.toStringValues());
			row = stbl.readNextRecord();
			rowcnt++;
		}

		printer.appendCuttingLine('-');
		printer.printout();

		// -----------------------------------------------------------------------------------------------
		stbl.setFilter(new DataRecordFilter() {
			@Override
			public boolean isMatched(DataRecord row) {
				String category = (String) row.getValue(3);
				return (row.getValue(2) != null && row.getValue(1) != null && category.startsWith("9"));
			}
		});
		def = stbl.getDefinition();
		printer = new PrettyRecordPrinter();
		printer.appendTitle("Filted2:");
		printer.getLayout().enableTopBorder('/', '=', '\\');
		printer.appendRecord(def.getColumnNames());
		printer.appendCuttingLine('-');
		List<DataRecord> rows = stbl.readFirstRecords(6);
		while (rows != null) {
			for (DataRecord rec : rows) {
				printer.appendRecord(rec.toStringValues());
			}
			rowcnt += rows.size();
			rows = stbl.readNextRecords(6);
		}

		printer.appendCuttingLine('-');
		printer.printout();
		// -----------------------------------------------------------------------------------------------

		csv.closeCatalog();
	}
}
