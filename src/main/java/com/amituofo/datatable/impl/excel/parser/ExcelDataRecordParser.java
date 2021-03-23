package com.amituofo.datatable.impl.excel.parser;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.impl.basic.AbsDataRecordParser;

public class ExcelDataRecordParser extends AbsDataRecordParser {
	private int firstColumn = 0;

	// private FormulaEvaluator evaluator;

	/**
	 * @param firstColumn
	 *            Start from 1
	 */
	public ExcelDataRecordParser(int firstColumn) {
		if (firstColumn > 0) {
			this.firstColumn = firstColumn;
		}
		// this.evaluator = evaluator;
	}

	@Override
	protected Object[] parseValues(Object record) throws DataException {
		int cnt = definition.getColumnCount();
		Cell[] cells = new Cell[cnt + firstColumn];

		for (int i = firstColumn; i < cells.length; i++) {
			cells[i] = ((Row) record).getCell(i);
		}

		return cells;
	}

	// public DataRecord parseRecord(Object record) throws DataException {
	// final DataRecord dr = definition.newDataRecordInstance(false);
	// DataFieldDefinition[] fields = definition.getColumns();
	//
	// for (int i = 0; i < fields.length; i++) {
	// Cell cell = ((Row) record).getCell(i + firstColumn);
	// dr.set(i, fields[i].parse(cell));
	// }
	//
	// return dr;
	// }
	//
	// public DataRecord parseFields(Object[] recordFields) throws DataException {
	// DataRecord dr = definition.newDataRecordInstance(false);
	// DataFieldDefinition[] fields = definition.getColumns();
	//
	// int maxLen = recordFields.length > fields.length ? fields.length : recordFields.length;
	//
	// for (int i = 0; i < maxLen; i++) {
	// dr.set(i, fields[i].parse(recordFields[i]));
	// }
	//
	// return dr;
	// }

	// @Override
	// public DataRecordParser<Row, Cell> clone() {
	// ExcelDataRecordParser newObj = new ExcelDataRecordParser(firstColumn);
	// return newObj;
	// }

	// public FormulaEvaluator getEvaluator() {
	// return evaluator;
	// }
	//
	// public void setEvaluator(FormulaEvaluator evaluator) {
	// this.evaluator = evaluator;
	// }

	public int getFirstColumn() {
		return firstColumn;
	}

	public void setFirstColumn(int firstColumn) {
		this.firstColumn = firstColumn;
	}

	// public static String toValue(Cell cell, FormulaEvaluator evaluator, DataFieldrending rending) {
	// switch (cell.getCellType()) {
	// case Cell.CELL_TYPE_NUMERIC:
	// if (HSSFDateUtil.isCellDaterendingted(cell)) { // 判断是日期类型
	// Date dt = cell.getDateCellValue();
	// return rending.rending(dt);
	// } else {
	// return new BigDecimal(cell.getNumericCellValue()).toPlainString();
	// }
	// case Cell.CELL_TYPE_STRING:
	// return cell.getStringCellValue();
	// case Cell.CELL_TYPE_BLANK:
	// return "";
	// case Cell.CELL_TYPE_BOOLEAN:
	// return String.valueOf(cell.getBooleanCellValue());
	// case Cell.CELL_TYPE_FORMULA:
	// CellValue cellValue = evaluator.evaluate(cell);
	// switch (cellValue.getCellType()) {
	// case Cell.CELL_TYPE_NUMERIC:
	// if (HSSFDateUtil.isCellDaterendingted(cell)) { // 判断是日期类型
	// Date dt = cell.getDateCellValue();
	// return rending.rending(dt);
	// } else {
	// return new BigDecimal(cell.getNumericCellValue()).toPlainString();
	// }
	// case Cell.CELL_TYPE_STRING:
	// return cell.getStringCellValue();
	// case Cell.CELL_TYPE_BOOLEAN:
	// return String.valueOf(cell.getBooleanCellValue());
	// case Cell.CELL_TYPE_BLANK:
	// return "";
	// }
	// }
	//
	// return cell.toString();
	// }

	// private int testColumnCount(Row row) {
	// int i = firstColumn;
	// Cell cell = row.getCell(i);
	// while (cell != null) {
	// cell = row.getCell(++i);
	// }
	//
	// return i - firstColumn;
	// }

}
