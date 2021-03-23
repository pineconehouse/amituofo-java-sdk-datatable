package com.amituofo.datatable.impl.excel.parser;

import java.math.BigDecimal;
import java.text.Format;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.type.StringField;

public class ExcelCell2StringFieldParser extends AbsExcelCell2XDataFieldParser<StringField, String> {
//	private MathContext mc = new MathContext(6);
	
	public ExcelCell2StringFieldParser(FormulaEvaluator evaluator, Format format, boolean trimValue) {
		super(evaluator, format, trimValue);
	}

	@Override
	protected String convertValue(Cell value) throws DataException {
		return toStringValue((Cell) value);
	}

	private String toStringValue(Cell cell) {
		if (cell == null) {
			return null;
		}

		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(cell)) { // 判断是日期类型
				if (format != null) {
					return format.format(cell.getDateCellValue());
				} else {
					return String.valueOf(cell.getDateCellValue());
				}
			} else {
				if (format != null) {
					return format.format(cell.getNumericCellValue());
				} else {
//					return new BigDecimal(cell.getNumericCellValue(), mc).toPlainString();
					return new BigDecimal(cell.getNumericCellValue()).toPlainString();
//					return String.valueOf(cell.getNumericCellValue());
				}
			}
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		case Cell.CELL_TYPE_BLANK:
			return null;
		case Cell.CELL_TYPE_BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		case Cell.CELL_TYPE_FORMULA:
			return toStringValue(evaluator.evaluate(cell));
		}

		return cell.toString();
	}

	private String toStringValue(CellValue cellValue) {
		if (cellValue == null) {
			return null;
		}

		switch (cellValue.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
			return new BigDecimal(cellValue.getNumberValue()).toPlainString();
		case Cell.CELL_TYPE_STRING:
			return cellValue.getStringValue();
		case Cell.CELL_TYPE_BOOLEAN:
			return String.valueOf(cellValue.getBooleanValue());
		case Cell.CELL_TYPE_FORMULA:
		case Cell.CELL_TYPE_BLANK:
			return null;
		}

		return cellValue.toString();
	}

//	@Override
//	public DataFieldParser clone() {
//		ExcelCell2StringFieldParser newObj = new ExcelCell2StringFieldParser(evaluator, format, trimValue);
//		return newObj;
//	}

}
