package com.amituofo.datatable.impl.excel;

import java.io.IOException;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.DataTable;
import com.amituofo.datatable.DataTableDefinition;
import com.amituofo.datatable.DefinitionException;
import com.amituofo.datatable.impl.basic.AbsDataCatalog;

public class ExcelDataCatalog extends AbsDataCatalog {
	private Workbook workbook = null;
	// private boolean firstRowAsTitle;
	// private int startReadRow = 0;
	// private int startReadColumn = 0;
	// private int maxReadRow = 1048576;// 65535;
	// private int maxReadColumn = 16384;// 256;
	// private int startWriteRow = 0;
	// private int startWriteColumn = 0;
	private FormulaEvaluator evaluator;

	public ExcelDataCatalog(String name, ExcelDataTableDefinition definition, Workbook workbook, boolean firstRowAsTitle, int startReadRow, int startReadColumn, int startWriteRow,
			int startWriteColumn) throws DataException, DefinitionException {
		super(name);

		if (startReadRow <= 1) {
			startReadRow = 0;
		} else {
			startReadRow -= 1;
		}
		if (startReadColumn <= 1) {
			startReadColumn = 0;
		} else {
			startReadColumn -= 1;
		}
		this.workbook = workbook;
		// this.firstRowAsTitle = firstRowAsTitle;
		// this.startReadRow = startReadRow;
		// this.startReadColumn = startReadColumn;
		// this.startWriteRow = startWriteRow;
		// this.startWriteColumn = startWriteColumn;

		this.evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		int size = workbook.getNumberOfSheets();
		for (int i = 0; i < size; i++) {
			Sheet sheet = workbook.getSheetAt(i);
			if (sheet == null) {
				throw new DataException("Could not get sheet at index " + i);
			}

			ExcelDataTableDefinition tabledef = null;
			if (definition == null) {
				tabledef = ExcelDataDefinitionFactory.create(sheet.getSheetName(), firstRowAsTitle, startReadRow, startReadColumn, startWriteRow, startWriteColumn, evaluator);
			} else {
				tabledef = definition;
				((ExcelDataTableDefinition) tabledef).setEvaluator(evaluator);
			}
			ExcelDataTable dt = new ExcelDataTable(sheet.getSheetName(), tabledef, sheet, evaluator);
			setDataTable(sheet.getSheetName(), dt);
		}
	}

	public Workbook getWorkbook() {
		return workbook;
	}

	@Override
	public DataTable createTable(String name, DataTableDefinition definition) throws DataException, DefinitionException {
		((ExcelDataTableDefinition) definition).setEvaluator(evaluator);

		Sheet sheet = workbook.createSheet(name);
		DataTable workSheet = this.getTable(name);
		if (workSheet == null) {
			workSheet = new ExcelDataTable(name, (ExcelDataTableDefinition)definition, sheet, evaluator);
			setDataTable(name, workSheet);
		}

		return workSheet;
	}

	@Override
	public DataTable dropTable(String name) throws DataException {
		DataTable dt = super.getTable(name);
		if (dt != null) {
			dt.close();
			int index = workbook.getSheetIndex(name);
			if (index > -1) {
				workbook.removeSheetAt(index);

				return super.dropTable(name);
			}
		}
		return null;
	}

	@Override
	public void close() {
		super.close();
		try {
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
