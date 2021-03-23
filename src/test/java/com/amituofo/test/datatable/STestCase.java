package com.amituofo.test.datatable;

import java.io.File;
import java.util.List;

import com.amituofo.datatable.DataCatalog;
import com.amituofo.datatable.DataException;
import com.amituofo.datatable.DataTable;
import com.amituofo.datatable.DefinitionException;
import com.amituofo.datatable.impl.txt.CsvDataFile;
import com.amituofo.datatable.impl.txt.TxtDataFile;
import com.amituofo.datatable.utils.DefaultMapping;

import junit.framework.TestCase;

public class STestCase extends TestCase {
	public void test_PrintTop10OfTableDataInCatalog() throws DefinitionException, DataException {
		File all = new File("F:\\TEMP\\all.csv");
		all.delete();
		CsvDataFile allcsv = new CsvDataFile(all, "utf-8", true);
		DataCatalog alldc = allcsv.openCatalog();
		DataTable alldt = alldc.getTable(0);

		File dir = new File("F:\\TEMP\\analysis_cn001_result");
		TxtDataFile csv = new TxtDataFile(dir, "utf-8", ' ', '\"', "*.csv", 1, false);
		DataCatalog dc = csv.openCatalog();

		List<DataTable> sourcedts = dc.getTables();

		int i = 0;
		int cnt = 0;
		alldt.getDefinition().resetColumn(sourcedts.get(0).getDefinition());
		alldt.writeTitle();
		for (DataTable sourcedt : sourcedts) {
			cnt += sourcedt.getRecordCount();
			System.out.println(++i + sourcedt.getName());
			alldt.appendAll(sourcedt, false, DefaultMapping.COLUMN_INDEX_MAPPING);
		}

		System.out.println(cnt);

		dc.close();
		alldc.close();
	}

//	public void test_Import_analysis_cn001_result() throws DefinitionException, DataException {
//		JdbcDataAccessor sqlsvr = new MySqlDataAccessor("10.167.39.215", "root", "root", "test");
//		DataCatalog dc = sqlsvr.open();
//		DataTable dt = dc.getTable("analysis_cn001_result");
//		dt.truncate();
//
//		File file = new File("D:\\WORKSPACE3.7\\AMTF_Datafile3\\Test_Data2\\analysis_cn001_result.txt");
//		TxtDataFile csv = new TxtDataFile(file, "utf-8", ' ', '\"', 1, false);
//		// CsvDataFile csv = new CsvDataFile(file, "utf-8", false);
//		DataCatalog csvdc = csv.openCatalog();
//		DataTable csvdt = csvdc.getTable(0);
//
//		dt.appendAll(csvdt, false, DefaultMapping.COLUMN_INDEX_MAPPING);
//
//		dc.close();
//		csvdc.close();
//	}
//	
//	public void test_Import_analysis_cn001_result_allinone_no0() throws DefinitionException, DataException {
//		JdbcDataAccessor sqlsvr = new MySqlDataAccessor("10.167.39.215", "root", "root", "test");
//		DataCatalog dc = sqlsvr.open();
//		DataTable dt = dc.getTable("analysis_cn001_result_allinone_no0");
//		dt.truncate();
//
//		File file = new File("D:\\WORKSPACE3.7\\AMTF_Datafile3\\Test_Data2\\analysis_cn001_result_allinone.txt");
//		BsvDataFile csv = new BsvDataFile(file, "utf-8", false);
//		// CsvDataFile csv = new CsvDataFile(file, "utf-8", false);
//		DataCatalog csvdc = csv.openCatalog();
//		DataTable csvdt = csvdc.getTable(0);
//
////		dt.appendAll(csvdt, false, DefaultMapping.COLUMN_INDEX_MAPPING);
//		DataRecord row = csvdt.readFirstRecord();
//		while(row!=null) {
//			
//			row.set(1,"中国联通");
//			
//			String title = row.getValueAsString(4);
//			int i = title.indexOf('-');
//			row.set(3,title.substring(0, i));
//			row.set(4, title.substring(i+1));
//			
//			dt.appendRecord(row, null);
//			
//			row = csvdt.readNextRecord();
//		}
//
//		dc.close();
//		csvdc.close();
//	}
//	
//	
//	public void test_Import_analysis_cn002_result() throws DefinitionException, DataException {
//		JdbcDataAccessor sqlsvr = new MySqlDataAccessor("10.167.39.215", "root", "root", "test");
//		DataCatalog dc = sqlsvr.open();
//		DataTable dt = dc.getTable("analysis_cn002_result");
//		dt.truncate();
//
//		File file = new File("D:\\WORKSPACE3.7\\AMTF_Datafile3\\Test_Data2\\analysis_cn002_result.txt");
//		BsvDataFile csv = new BsvDataFile(file, "utf-8", false);
//		DataCatalog csvdc = csv.openCatalog();
//		DataTable csvdt = csvdc.getTable(0);
//		csvdt.resetColumn(dt.getDefinition());
////		csvdt.getDefinition().getColumn(0).s
//
//		dt.appendAll(csvdt, false, DefaultMapping.COLUMN_INDEX_MAPPING);
//
//		dc.close();
//		csvdc.close();
//	}
//
//
//	public void test_AllCsv2DB() throws DefinitionException, DataException {
//		JdbcDataAccessor sqlsvr = new MySqlDataAccessor("10.167.39.215", "root", "root", "test");
//		DataCatalog dc = sqlsvr.open();
//		DataTable dt = dc.getTable("analysis_cn001_sales");
//		dt.truncate();
//
//		File file = new File("D:\\WORKSPACE3.7\\AMTF_Datafile3\\Test_Data2\\Job_01_ChinaUnicom_031Q_161Q");
//		CsvDataFile csv = new CsvDataFile(file, "utf-8", false);
//		DataCatalog csvdc = csv.openCatalog();
//
//		List<DataTable> tbls = csvdc.getTables();
//		for (DataTable csvdt : tbls) {
//			csvdt.getDefinition().insertColumn(0, new TxtDataFieldDefinition("COMPANY", -1, new StringFieldType(), ',', '"', false, true, null, null));
//			csvdt.getDefinition().insertColumn(2, new TxtDataFieldDefinition("TITLE", -1, new StringFieldType(false, "(左軸：単位元)"), ',', '"', false, true, null, null));
//
//			DataRecord rec = csvdt.readFirstRecord();
//			while (rec != null) {
//				rec.set(0, csvdt.getName().replace(".csv", ""));
//				String unit = "";
////				if ("中国联通".equals(rec.getValueAsString(0))) {
////					unit = "(左軸：単位百万元)";
////					
////					for (int i = 3; i < rec.getFieldCount(); i++) {
////						rec.set(i, new BigDecimal(rec.getValueAsDecimal(i).doubleValue()/1000000));
////					}
////				}
//				rec.set(2, rec.getValueAsString(0)+"-"+rec.getValueAsString(1)+unit);
//				
//				if (rec.getFieldCount()!=dt.getDefinition().getColumnCount()) {
//					System.out.println(csvdt.getName());
//				}
//				dt.appendRecord(rec, null);
////				System.out.println(rec);
//				rec = csvdt.readNextRecord();
//			}
//			// dt.appendAll(csvdt, false, DefaultMapping.COLUMN_INDEX_MAPPING);
//		}
//
//		dc.close();
//		csvdc.close();
//	}
//	
//	public void test_OutAllCsv() throws DefinitionException, DataException {
//		JdbcDataAccessor sqlsvr = new MySqlDataAccessor("10.167.39.215", "root", "root", "test");
//		DataCatalog dc = sqlsvr.open();
//		DataTable dt = dc.getTable("analysis_cn001_result_allinone_no0nonegative");
//		dt.truncate();
//
//		File file = new File("D:\\WORKSPACE3.7\\AMTF_Datafile3\\Test_Data2\\00_China_Unicom-00_all_sales_no0");
//		BsvDataFile csv = new BsvDataFile(file, "utf-8", false);
//		DataCatalog csvdc = csv.openCatalog();
//
//		int total = 0;
//		List<DataTable> tbls = csvdc.getTables();
//		for (DataTable csvdt : tbls) {
//			total +=csvdt.getRecordCount();
//			System.out.println(csvdt.getName() + "("+csvdt.getRecordCount()+")");
////			DataRecord rec = csvdt.readFirstRecord();
////			while (rec != null) {
//////				rec.set(0, csvdt.getName().replace(".csv", ""));
//////				String unit = "";
//////				rec.set(2, rec.getValueAsString(0)+"-"+rec.getValueAsString(1)+unit);
//////				
//////				if (rec.getFieldCount()!=dt.getDefinition().getColumnCount()) {
//////					System.out.println(csvdt.getName());
//////				}
//////				dt.appendRecord(rec, null);
////				System.out.println(rec);
////				rec = csvdt.readNextRecord();
////			}
//			// dt.appendAll(csvdt, false, DefaultMapping.COLUMN_INDEX_MAPPING);
//		}
//
//		System.out.println(total);
//		dc.close();
//		csvdc.close();
//	}
}
