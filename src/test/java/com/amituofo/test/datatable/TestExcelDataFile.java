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
import com.amituofo.datatable.impl.excel.ExcelDataDefinitionFactory;
import com.amituofo.datatable.impl.excel.ExcelDataFile;
import com.amituofo.datatable.impl.excel.ExcelDataTableDefinition;
import com.amituofo.datatable.impl.txt.CsvDataFile;
import com.amituofo.datatable.impl.txt.TxtDataDefinitionFactory;
import com.amituofo.datatable.impl.txt.TxtDataTableDefinition;
import com.amituofo.datatable.utils.Moulding;

public class TestExcelDataFile extends BasicTestCase {
	public static File xlsxGCM = newFile("GlobalCustomerMaster.xlsx");
	public static File excelDefRevenue = newFile("DataFileDef_(XlsRevenue).xml");
	public static File xlsxNoData = newFile("blank.xlsx");
	public static File xlsxNotExists = newFile("txtNotExists.xlsx");
	public static File csvFromExcel = newFile("CsvFromExcel.csv");
	public static File excelFromCsv = newFile("ExcelFromCsv.xlsx");
	public static File xlsx100W = newFile("Revenue20W.xlsx");

	public static File excelDefZhangzu = new File(basicDir + "DataFileDef_(XlsZhangzu).xml");

	@Override
	public void test_okcase_PrintAllInFile() throws DataException, DefinitionException {
		// ExcelDataFile xlsx = new ExcelDataFile(xlsxGCM, 0, true);
		ExcelDataFile xlsx = new ExcelDataFile(newFile("化学繊維布.xls"), 4, 3, true);

		DataCatalog dc = xlsx.openCatalog();
		// DataTable dt = dc.getTable("globalcustomermaster");
		DataTable dt = dc.getTable("季度数据");
		DataTableDefinition def = dt.getDefinition();
		// def.removeColumn("JAPAN_CUSTOMER_NAME_JP");
		DataRecord row = dt.readFirstRecord();
		printer.appendRecord(def.getColumnNames());
		printer.appendCuttingLine('-');
		while (row != null) {
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

			// if (rowcnt > 50)
			// break;
		}

		xlsx.closeCatalog();
	}

	@Override
	public void test_okcase_PrintAllInFile_ByXmlDef() throws DataException, DefinitionException {
		ExcelDataTableDefinition csvdef = ExcelDataDefinitionFactory.parse(excelDefRevenue);
		ExcelDataFile xlsx = new ExcelDataFile(csvdef, xlsxGCM, 0, true);
		DataCatalog dc = xlsx.openCatalog();
		DataTable dt = dc.getTable("Revenue");
		DataTableDefinition def = dt.getDefinition();

		DataRecord row = dt.readFirstRecord();
		printer.appendRecord(def.getColumnNames());
		printer.appendCuttingLine('-');
		while (row != null) {
			printer.appendRecord(row.toStringValues());
			row = dt.readNextRecord();
			rowcnt++;

			if (rowcnt > 50)
				break;
		}

		xlsx.closeCatalog();
	}

	@Override
	public void test_okcase_PrintInvalid_ByXmlDef() throws DataException, DefinitionException {
		ExcelDataTableDefinition csvdef = ExcelDataDefinitionFactory.parse(excelDefRevenue);
		ExcelDataFile xlsx = new ExcelDataFile(csvdef, xlsxGCM, 0, true);
		DataCatalog dc = xlsx.openCatalog();
		DataTable dt = dc.getTable("Revenue");
		DataTableDefinition def = dt.getDefinition();

		DataRecord row = dt.readFirstRecord();
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

			if (rowcnt > 50)
				break;
		}

		xlsx.closeCatalog();
	}

	@Override
	public void test_okcase_SpeedOfReadBigData() throws DataException, DefinitionException {
		ExcelDataFile xlsx = new ExcelDataFile(xlsx100W, 0, true);
		DataCatalog dc = xlsx.openCatalog();
		DataTable dt = dc.getTable(0);

		DataRecord row = dt.readFirstRecord();
		while (row != null) {
			printer.appendRecord(row.toStringValues());
			row = dt.readNextRecord();
			rowcnt++;
		}

		xlsx.closeCatalog();
	}

	@Override
	public void test_okcase_PrintReadStartFromRow30_Notitle() throws DataException, DefinitionException {
		ExcelDataFile xlsx = new ExcelDataFile(xlsxGCM, 30, false);
		DataCatalog dc = xlsx.openCatalog();
		DataTable dt = dc.getTable("GlobalCustomerMaster");
		DataTableDefinition def = dt.getDefinition();

		DataRecord row = dt.readFirstRecord();
		printer.appendRecord(def.getColumnNames());
		printer.appendCuttingLine('-');
		while (row != null) {
			printer.appendRecord(row.toStringValues());
			row = dt.readNextRecord();
			rowcnt++;

			if (rowcnt > 50)
				break;
		}

		xlsx.closeCatalog();
	}

	@Override
	public void test_okcase_ReadBlankFile() throws DataException, DefinitionException {
		ExcelDataFile xlsx = new ExcelDataFile(xlsxNoData, 0, true);
		DataCatalog dc = xlsx.openCatalog();
		DataTable dt = dc.getTable("sheet1");
		DataTableDefinition def = dt.getDefinition();

		DataRecord row = dt.readFirstRecord();
		if (row != null) {
			printer.appendRecord(def.getColumnNames());
			printer.appendCuttingLine('-');
			while (row != null) {
				printer.appendRecord(row.toStringValues());
				row = dt.readNextRecord();
				rowcnt++;

				if (rowcnt > 50)
					break;
			}
		}

		xlsx.closeCatalog();
	}

	@Override
	public void test_okcase_ReadNotExistsFile() throws DataException, DefinitionException {
		ExcelDataFile xlsx = new ExcelDataFile(xlsxNotExists, 0, true);
		DataCatalog dc = xlsx.openCatalog();
		DataTable dt = dc.getTable("sheet1");

		if (dt != null) {
			this.assertEquals(1, 0);
		}

		xlsx.closeCatalog();
	}

	@Override
	public void test_okcase_PrintTop10OfTableDataInCatalog() throws DefinitionException, DataException {
		ExcelDataFile xlsx = new ExcelDataFile(xlsxGCM, 0, true);
		DataCatalog dc = xlsx.openCatalog();

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

		xlsx.closeCatalog();
	}

	@Override
	public void test_okcase_WriteFormCurrentFileType2Txt() throws DataException, DefinitionException {
		csvFromExcel.delete();

		ExcelDataFile xlsx = new ExcelDataFile(xlsxGCM, 0, true);
		DataTable xlsxdt = xlsx.openCatalog().getTable("GlobalCustomerMaster");

		CsvDataFile csv = new CsvDataFile(csvFromExcel, "utf-8", true);
		DataTable csvdt = csv.openCatalog().getTable(0);
		csvdt.resetColumn(xlsxdt.getDefinition());

		DataRecord row = xlsxdt.readFirstRecord();
		csvdt.writeTitle();
		while (row != null) {
			csvdt.appendRecord(row, null);
			row = xlsxdt.readNextRecord();
			rowcnt++;
		}

		xlsx.closeCatalog();
		csv.closeCatalog();
	}

	@Override
	public void test_okcase_WriteFormCurrentFileTypeWithDefaultDef2Txt_ByWriteAllFunction() throws DataException, DefinitionException {
		csvFromExcel.delete();

		ExcelDataFile xlsx = new ExcelDataFile(xlsxGCM, 0, true);
		DataCatalog xlsxdc = xlsx.openCatalog();
		DataTable xlsxdt = xlsxdc.getTable("Revenue");

		CsvDataFile csv = new CsvDataFile(csvFromExcel, "utf-8", true);
		DataTable csvdt = csv.openCatalog().getTable(0);
		csvdt.resetColumn(xlsxdt.getDefinition());

		csvdt.appendAll(xlsxdt, true, null);

		xlsx.closeCatalog();
		csv.closeCatalog();
	}

	@Override
	public void test_okcase_WriteFormCurrentFileTypeWithXmlDef2Txt_ByWriteAllFunction() throws DataException, DefinitionException {
		csvFromExcel.delete();

		ExcelDataTableDefinition csvdef = ExcelDataDefinitionFactory.parse(excelDefRevenue);
		ExcelDataFile xlsx = new ExcelDataFile(csvdef, xlsxGCM, 0, true);
		DataCatalog xlsxdc = xlsx.openCatalog();
		DataTable xlsxdt = xlsxdc.getTable("Revenue");

		CsvDataFile csv = new CsvDataFile(csvFromExcel, "utf-8", true);
		DataTable csvdt = csv.openCatalog().getTable(0);
		csvdt.resetColumn(xlsxdt.getDefinition());

		csvdt.appendAll(xlsxdt, true, null);

		xlsx.closeCatalog();
		csv.closeCatalog();
	}

	@Override
	public void test_okcase_WriteToNewTabeAfterModification() throws DataException, DefinitionException {
		ExcelDataTableDefinition csvdef = ExcelDataDefinitionFactory.parse(excelDefRevenue);
		ExcelDataFile xlsx = new ExcelDataFile(csvdef, xlsxGCM, 0, true);
		DataCatalog dc = xlsx.openCatalog();
		DataTable dt = dc.getTable("Revenue");
		DataTableDefinition def = dt.getDefinition();

		dc.dropTable("CopyFromRevenue");
		DataTable newdt = dc.createTable("CopyFromRevenue", csvdef);

		// newdt.writeAll(dt);

		DataRecord row = dt.readFirstRecord();
		newdt.writeTitle();
		while (row != null) {
			DataField fd = row.get(def.getColumnIndex("ITEM_REVENUE"));
			if (fd.getValue() != null) {
				fd.setValue(new BigDecimal(10000).add((BigDecimal) fd.getValue()));

				DataField fd1 = row.get(def.getColumnIndex("GROSS_MARGIN"));
				if (fd1.getValue() != null) {
					fd1.setValue(((BigDecimal) fd1.getValue()).add((BigDecimal) fd.getValue()));
				}
			}

			newdt.appendRecord(row, null);
			row = dt.readNextRecord();
			rowcnt++;
		}

		xlsx.closeCatalog();
	}

	@Override
	public void test_okcase_PrintReadFirstRowTwice() throws DataException, DefinitionException {
		ExcelDataFile xlsx = new ExcelDataFile(xlsxGCM, 0, true);
		DataCatalog dc = xlsx.openCatalog();
		DataTable dt = dc.getTable("GlobalCustomerMaster");
		DataTableDefinition def = dt.getDefinition();

		DataRecord row = dt.readFirstRecord();
		printer.appendRecord(def.getColumnNames());
		printer.appendCuttingLine('-');
		printer.appendRecord(row.toStringValues());
		row = dt.readFirstRecord();
		printer.appendRecord(row.toStringValues());
		row = dt.readFirstRecord();
		printer.appendRecord(row.toStringValues());

		xlsx.closeCatalog();
	}

	@Override
	public void test_okcase_WriteFormXmlDefTxt2CurrentFileType_ByWriteAllFunction() throws DataException, DefinitionException {
		excelFromCsv.delete();

		TxtDataTableDefinition csvdef = TxtDataDefinitionFactory.parse(TestTxtDataFile.csvDefRevenue);
		CsvDataFile csv = new CsvDataFile(csvdef, TestTxtDataFile.txtRevenue20W);
		DataTable csvdt = csv.openCatalog().getTable(0);

		ExcelDataFile xlsx = new ExcelDataFile(excelFromCsv, 0, true);
		DataCatalog xlsxdc = xlsx.openCatalog();
		DataTable xlsxdt = xlsxdc.createTable("revenue", ExcelDataDefinitionFactory.create("revenue", true, 0, 0, 0, 0, null));

		xlsxdt.resetColumn(csvdt.getDefinition());

		xlsxdt.appendAll(csvdt, true, null);

		// int i = 0;
		// while (i++ < 60000) {
		// xlsxdt.writeAll(csvdt);
		// }

		csv.closeCatalog();
		xlsx.closeCatalog();
	}

	public void test_readZhangzu() throws DataException, DefinitionException {
		File zhang = new File("F:\\DOWNLOADS\\zonezu_2011-05-01_2014-06-30.xlsx");
		File zhangcsv = new File("F:\\DOWNLOADS\\zonezu_2011-05-01_2014-06-30.csv");
		zhangcsv.delete();

		ExcelDataTableDefinition zhangzudef = ExcelDataDefinitionFactory.parse(excelDefZhangzu);
		ExcelDataFile xlsx = new ExcelDataFile(zhangzudef, zhang, 4, false);
		DataCatalog dc = xlsx.openCatalog();
		DataTable dt = dc.getTable("zonezu_2011-05-01_2014-06-30");

		dc.dropTable("zhangzu");
		DataTable newdt = dc.createTable("zhangzu", zhangzudef);

		CsvDataFile csv = new CsvDataFile(zhangcsv, "utf-8", true);
		DataTable csvdt = csv.openCatalog().getTable(0);
		csvdt.resetColumn(zhangzudef);

		DataRecord row = dt.readFirstRecord();

		// printer.appendRecord(row.getFieldNames());
		// printer.appendCuttingLine('-');

		newdt.writeTitle();
		csvdt.writeTitle();
		while (row != null) {
			// row.get("NO").getValue()
			// printer.appendRecord(row.toStringValues());

			newdt.appendRecord(row, null);
			csvdt.appendRecord(row, null);

			row = dt.readNextRecord();
			rowcnt++;

			// if (rowcnt > 50)
			// break;
		}

		xlsx.closeCatalog();
		csv.closeCatalog();

	}

	public void test_readExcels() throws DataException, DefinitionException {
		File file = null;
		List<File> files = FileUtils.findFiles("F:\\TEMP\\四半期別", "*.xls*", false);
		for (int i = 0; i < files.size(); i++) {
			try {
				file = files.get(i);

				ExcelDataFile excel = new ExcelDataFile(file, 3, true);
				DataTable tbl = excel.openCatalog().getTable("季度数据");

				System.out.println(file);

				String name = file.getName().split("\\.")[0];
				File csvFile = new File("F:\\TEMP\\四半期別\\" + name + ".csv");
				csvFile.delete();
				CsvDataFile csv = new CsvDataFile(csvFile, "utf-8", true);
				DataTable csvdt = csv.openCatalog().getTable(0);
				csvdt.resetColumn(tbl.getDefinition());
				csvdt.writeTitle();

				// csvdt.appendAll(tbl);

				DataRecord row = tbl.readFirstRecord();
				while (row != null) {
					String category = (String) row.getValue(0);
					if (category.contains("季度") && category.contains("年第")) {
						Object[] o = row.getValues();
						for (int j = 1; j < o.length; j++) {
							String num = (String) o[j];

							if (num != null && num.trim().length() > 0) {
								BigDecimal b = new BigDecimal((String) num);
								double f1 = b.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
								row.set(j, String.valueOf(f1));
							}
						}
						csvdt.appendRecord(row, null);
					}

					row = tbl.readNextRecord();
				}

				excel.closeCatalog();
				csv.closeCatalog();
			} catch (Exception e) {
				System.out.println("ERROR " + file);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void test_okcase_truncate() throws DataException, DefinitionException, IOException {
		File f = newFile("化学繊維布.xls");
		File copyForReadWrite = File.createTempFile("~EX", ".xls");
		FileUtils.copyFile(f, copyForReadWrite);

		ExcelDataFile xlsx = new ExcelDataFile(copyForReadWrite, 4, 3, true);

		DataCatalog dc = xlsx.openCatalog();
		DataTable dt = dc.getTable("季度数据");
		dt.truncate();

		xlsx.closeCatalog();
	}

	@Override
	public void test_okcase_RecordFilter() throws DataException, DefinitionException, IOException {
		ExcelDataFile xlsx = new ExcelDataFile(xlsxGCM, 0, true);
		DataCatalog dc = xlsx.openCatalog();
		DataTable dt = dc.getTable("GlobalCustomerMaster");

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
				return (row.getValue(3) != null && ((String) row.getValue(3)).trim().length() != 0 && ((String) row.getValue(6)).contains("富士通"));
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
				// String category = (String) row.getValue(2);
				// return (category.startsWith("4"));
				return (row.getValue(3) != null && ((String) row.getValue(3)).trim().length() != 0 && ((String) row.getValue(6)).contains("富士通"));
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
			rowcnt += rows.size();
			rows = dt.readNextRecords(3);
		}

		printer.appendCuttingLine('-');
		printer.printout();
		// -----------------------------------------------------------------------------------------------

		xlsx.closeCatalog();

	}

	public void test_okcase_transpose() throws DataException, DefinitionException {
		ExcelDataFile xlsx = new ExcelDataFile(newFile("化学繊維布.xls"), 4, 3, true);

		DataCatalog dc = xlsx.openCatalog();
		DataTable dt = dc.getTable("季度数据");

		dt.setFilter(new DataRecordFilter() {
			@Override
			public boolean isMatched(DataRecord record) {
				String category = (String) record.getValue(0);
				return (category.contains("季度") && category.contains("年第"));
			}

		});
		DataTableDefinition def = dt.getDefinition();
		DataRecord row = dt.readFirstRecord();
		printer.appendRecord(def.getColumnNames());
		printer.appendCuttingLine('-');
		while (row != null) {
			printer.appendRecord(row.toStringValues());
			row = dt.readNextRecord();
			rowcnt++;
		}

		printer.printout();

		printer = new PrettyRecordPrinter();
		printer.getLayout().enableTopBorder('/', '=', '\\');
		DataTable newStbl = Moulding.transpose(dt, true);
		row = newStbl.readFirstRecord();
		def = newStbl.getDefinition();
		printer.appendRecord(def.getColumnNames());
		printer.appendCuttingLine('-');
		while (row != null) {
			printer.appendRecord(row.toStringValues());
			row = newStbl.readNextRecord();
		}
		printer.printout();

		xlsx.closeCatalog();
	}

}
