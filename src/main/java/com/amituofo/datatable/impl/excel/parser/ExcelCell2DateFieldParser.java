package com.amituofo.datatable.impl.excel.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.type.DateField;

public class ExcelCell2DateFieldParser extends AbsExcelCell2XDataFieldParser<DateField, Date> {

	public ExcelCell2DateFieldParser(FormulaEvaluator evaluator, DateFormat dateFormat, boolean trimValue) {
		super(evaluator, dateFormat, trimValue);
	}

	@Override
	protected Date convertValue(Cell value) throws DataException {
		return toDateValue((Cell) value);
	}

	private Date toDateValue(Cell cell) throws DataException {
		if (cell == null) {
			return null;
		}

		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC:
			return cell.getDateCellValue();
		case Cell.CELL_TYPE_STRING:
			if (format != null) {
				try {
					((DateFormat) format).parse(cell.getStringCellValue());
				} catch (ParseException e) {
					throw new DataException("Invalid date field parsed! " + cell.getStringCellValue());
				}
			}
		case Cell.CELL_TYPE_BLANK:
			return null;
		case Cell.CELL_TYPE_BOOLEAN:
		case Cell.CELL_TYPE_FORMULA:
			throw new DataException("Invalid date field parsed!");
		}

		return null;
	}

//	@Override
//	public DataFieldParser clone() {
//		ExcelCell2DateFieldParser newObj = new ExcelCell2DateFieldParser(evaluator, (DateFormat) format, trimValue);
//		return newObj;
//	}
}
