package com.amituofo.datatable.impl.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

import com.amituofo.datatable.DataFieldDefinition;
import com.amituofo.datatable.DataFieldParser;
import com.amituofo.datatable.DataFieldRender;
import com.amituofo.datatable.DataFieldType;
import com.amituofo.datatable.DataTableDefinition;
import com.amituofo.datatable.DefinitionException;
import com.amituofo.datatable.impl.basic.StdDataTableDefinition;
import com.amituofo.datatable.impl.basic.render.StringFieldRender;
import com.amituofo.datatable.impl.excel.parser.AbsExcelCell2XDataFieldParser;
import com.amituofo.datatable.impl.excel.parser.ExcelDataRecordParser;
import com.amituofo.datatable.type.StringFieldType;

public class ExcelDataTableDefinition extends StdDataTableDefinition {
	private int firstReadingRow = 0;
	// private int firstReadingColumn = 0;
	// private int maxReadingRow = 1048576;// 65535;
	// private int maxReadingColumn = 16384;// 256;
	private int firstWritenRow = 0;
	private int firstWritenColumn = 0;

	private boolean firstRowAsTitle;

	private FormulaEvaluator evaluator;

	public ExcelDataTableDefinition(String name, boolean firstRowAsTitle, int firstReadingRow, int firstReadingColumn, int firstWritenRow, int firstWritenColumn,
			FormulaEvaluator evaluator) {
		super(name, new ExcelDataRecordParser(firstReadingColumn), null);
		this.firstReadingRow = firstReadingRow;
		// this.firstReadingColumn = firstReadingColumn;
		this.firstWritenRow = firstWritenRow;
		this.firstWritenColumn = firstWritenColumn;
		this.firstRowAsTitle = firstRowAsTitle;
		this.evaluator = evaluator;
	}

	// @Override
	// public ExcelDataTableDefinition clone() {
	// ExcelDataTableDefinition cloneDef = new ExcelDataTableDefinition(name, firstRowAsTitle, firstReadingRow, 0, firstWritenRow, firstWritenColumn, getEvaluator());
	// copyTo(cloneDef);
	// return cloneDef;
	// }

	@Override
	public DataFieldDefinition createField(String name, DataFieldType fieldType) {
		ExcelDataFieldDefinition field = new ExcelDataFieldDefinition(name, fieldType);
		return field;
	}
	
	public boolean isFirstRowAsTitle() {
		return firstRowAsTitle;
	}

	public void setFirstRowAsTitle(boolean firstRowAsTitle) {
		this.firstRowAsTitle = firstRowAsTitle;
	}

	public int getFirstReadingRow() {
		return firstReadingRow;
	}

	public void setFirstReadingRow(int firstReadingRow) {
		this.firstReadingRow = firstReadingRow;
	}

	public int getFirstReadingColumn() {
		return ((ExcelDataRecordParser) parser).getFirstColumn();
	}

	public void setFirstReadingColumn(int firstReadingColumn) {
		((ExcelDataRecordParser) parser).setFirstColumn(firstReadingColumn);
	}

	public int getFirstWritenRow() {
		return firstWritenRow;
	}

	public void setFirstWritenRow(int firstWritenRow) {
		this.firstWritenRow = firstWritenRow;
	}

	public int getFirstWritenColumn() {
		return firstWritenColumn;
	}

	public void setFirstWritenColumn(int firstWritenColumn) {
		this.firstWritenColumn = firstWritenColumn;
	}

	public FormulaEvaluator getEvaluator() {
		return evaluator;
	}

	public void setEvaluator(FormulaEvaluator evaluator) {
		this.evaluator = evaluator;
		DataFieldDefinition[] fields = this.getColumns();
		for (DataFieldDefinition dataFieldDefinition : fields) {
			AbsExcelCell2XDataFieldParser<?,?> ps = (AbsExcelCell2XDataFieldParser<?,?>) dataFieldDefinition.getParser();
			ps.setEvaluator(evaluator);
		}
	}

	@Override
	public void resetColumn(DataTableDefinition fromDefinition) throws DefinitionException {
		this.removeAll();
		for (int i = 0; i < fromDefinition.getColumnCount(); i++) {
			DataFieldDefinition fieldDef = fromDefinition.getColumn(i);

			DataFieldType type = fieldDef.getType().clone();
			DataFieldRender render = new StringFieldRender();
			DataFieldParser parser = fieldDef.getParser();

			ExcelDataFieldDefinition fd = new ExcelDataFieldDefinition(fieldDef.getName(), i, type, false, parser.getFormat(), render.getFormat(), evaluator);
			this.addColumn(fd);
		}
	}

	@Override
	public void extractColumnAsStringType(Object row, boolean isTitleRow) throws DefinitionException {
		if (row != null) {
			Row excelrow = (Row) row;
			if (this.getColumnCount() == 0) {
				int i = getFirstReadingColumn();
				Cell cell = excelrow.getCell(i);
				while (cell != null) {
					ExcelDataFieldDefinition fd = new ExcelDataFieldDefinition(isTitleRow ? cell.toString() : null, i, new StringFieldType(), false, null, null, getEvaluator());
					this.addColumn(fd);
					i++;
					cell = excelrow.getCell(i);
				}
			} else if (isTitleRow) {
				for (int i = 0; i < this.getColumnCount(); i++) {
					DataFieldDefinition fielddef = this.getColumn(i);
					((ExcelDataFieldDefinition) fielddef).setEvaluator(getEvaluator());
					// ((AbsExcelDataFieldParser) fielddef.getParser()).setEvaluator(evaluator);
					if (fielddef.getName().length() == 0) {
						Cell cell = excelrow.getCell(i + getFirstReadingColumn());
						if (cell != null) {
							fielddef.setName(cell.getStringCellValue());
						}
					}
				}
			}
		}
	}

}
