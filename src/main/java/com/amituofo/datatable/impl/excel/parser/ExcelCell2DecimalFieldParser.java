package com.amituofo.datatable.impl.excel.parser;

import java.math.BigDecimal;
import java.text.NumberFormat;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.type.DecimalField;

public class ExcelCell2DecimalFieldParser extends AbsExcelCell2XDataFieldParser<DecimalField, BigDecimal> {

	public ExcelCell2DecimalFieldParser(FormulaEvaluator evaluator, NumberFormat format, boolean trimValue) {
		super(evaluator, format, trimValue);
	}

	@Override
	protected BigDecimal convertValue(Cell value) throws DataException {
		return toBigDecimalValue((Cell) value);
	}

	private BigDecimal toBigDecimalValue(Cell cell) {
		if (cell == null) {
			return null;
		}

		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
			return BigDecimal.valueOf(cell.getNumericCellValue());
		case Cell.CELL_TYPE_STRING:
			String val = cell.getStringCellValue();
			if (val != null && val.trim().length() != 0) {
				return new BigDecimal(val);
			}
		case Cell.CELL_TYPE_BLANK:
			return null;// BigDecimal.ZERO;
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue() ? BigDecimal.ONE : BigDecimal.ZERO;
		case Cell.CELL_TYPE_FORMULA:
			return toBigDecimalValue(evaluator.evaluate(cell));
		}

		return null;
	}

	private BigDecimal toBigDecimalValue(CellValue cell) {
		if (cell == null) {
			return null;
		}

		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
			return new BigDecimal(cell.getNumberValue());
		case Cell.CELL_TYPE_STRING:
			return new BigDecimal(cell.getStringValue());
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanValue() ? BigDecimal.ONE : BigDecimal.ZERO;
		case Cell.CELL_TYPE_FORMULA:
		case Cell.CELL_TYPE_BLANK:
			return null;// BigDecimal.ZERO;
		}

		return null;
	}

	// @Override
	// public DataFieldParser clone() {
	// ExcelCell2DecimalFieldParser newObj = new ExcelCell2DecimalFieldParser(evaluator, (NumberFormat)format, trimValue);
	// return newObj;
	// }

}
