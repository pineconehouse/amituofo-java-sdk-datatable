package com.amituofo.test.datatable;

import java.io.File;
import java.io.IOException;

import com.amituofo.common.kit.PrettyRecordPrinter;
import com.amituofo.datatable.DataException;
import com.amituofo.datatable.DefinitionException;

import junit.framework.TestCase;

public abstract class BasicTestCase extends TestCase {
	protected static String basicDir = "D:\\WORKSPACE3.7\\AMTF_Datafile3\\Test_Data\\";
	protected static File txtNoData = new File(basicDir + "blank.csv");
	protected static File txtNotExists = new File(basicDir + "txtNotExists.csv");
	protected long t1;
	protected int rowcnt = 1;
	protected PrettyRecordPrinter printer = new PrettyRecordPrinter();

	public abstract void test_okcase_PrintAllInFile() throws DataException, DefinitionException;

	public abstract void test_okcase_PrintAllInFile_ByXmlDef() throws DataException, DefinitionException;

	public abstract void test_okcase_PrintInvalid_ByXmlDef() throws DataException, DefinitionException;

	public abstract void test_okcase_SpeedOfReadBigData() throws DataException, DefinitionException;

	public abstract void test_okcase_PrintReadStartFromRow30_Notitle() throws DataException, DefinitionException;

	public abstract void test_okcase_ReadBlankFile() throws DataException, DefinitionException;

	public abstract void test_okcase_ReadNotExistsFile() throws DataException, DefinitionException;

	public abstract void test_okcase_PrintTop10OfTableDataInCatalog() throws DefinitionException, DataException;

	public abstract void test_okcase_WriteFormCurrentFileType2Txt() throws DataException, DefinitionException;

	public abstract void test_okcase_WriteFormCurrentFileTypeWithDefaultDef2Txt_ByWriteAllFunction() throws DataException, DefinitionException;

	public abstract void test_okcase_WriteFormCurrentFileTypeWithXmlDef2Txt_ByWriteAllFunction() throws DataException, DefinitionException;

	public abstract void test_okcase_WriteFormXmlDefTxt2CurrentFileType_ByWriteAllFunction() throws DataException, DefinitionException;

	public abstract void test_okcase_WriteToNewTabeAfterModification() throws DataException, DefinitionException;

	public abstract void test_okcase_PrintReadFirstRowTwice() throws DataException, DefinitionException;

	public abstract void test_okcase_truncate() throws DataException, DefinitionException, IOException;

	public abstract void test_okcase_RecordFilter() throws DataException, DefinitionException, IOException;

//	public abstract void test_okcase_ReadFromRowNo() throws DataException, DefinitionException, IOException;
	
	@Override
	protected void setUp() throws Exception {
		rowcnt = 1;
		t1 = System.currentTimeMillis();
	}

	@Override
	protected void tearDown() throws Exception {
		printer.printout();

		System.out.println("Totle row=" + rowcnt);

		long t2 = System.currentTimeMillis();
		long usetm = t2 - t1;
		System.out.println("TimeMillis=" + (usetm < 31 ? usetm : usetm - 31));
	}
	
	protected static File newFile(String name) {
		return new File(basicDir+name);
	}

}
