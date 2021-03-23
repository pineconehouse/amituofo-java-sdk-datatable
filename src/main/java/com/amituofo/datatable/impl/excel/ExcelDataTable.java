package com.amituofo.datatable.impl.excel;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.DataField;
import com.amituofo.datatable.DataFieldDefinition;
import com.amituofo.datatable.DataRecord;
import com.amituofo.datatable.DataTable;
import com.amituofo.datatable.DefinitionException;
import com.amituofo.datatable.IndexMapping;
import com.amituofo.datatable.impl.basic.AbsDataTable;
import com.amituofo.datatable.type.DateField;
import com.amituofo.datatable.type.DecimalField;
import com.amituofo.datatable.type.StringField;

public class ExcelDataTable extends AbsDataTable<ExcelDataTableDefinition, Row, Cell> {
	private Sheet workSheet = null;

	private int titleRow = 0;
	private int startReadRow = 0;
	private int startReadColumn = 0;
	// private int maxReadRow = 1048576;// 65535;
	// private int maxReadColumn = 16384;// 256;
	private int startWriteRow = 0;
	private int startWriteColumn = 0;

	private boolean firstRowAsTitle;

	public ExcelDataTable(String name, ExcelDataTableDefinition definition, Sheet workSheet, FormulaEvaluator evaluator) throws DataException, DefinitionException {
		super(name, definition);
		this.workSheet = workSheet;
		this.definition = definition;

		// if (definition.getFieldCount() != 0) {
		// this.maxReadColumn = definition.getFieldCount();
		// }

		this.firstRowAsTitle = definition.isFirstRowAsTitle();

		this.startReadRow = definition.getFirstReadingRow();
		this.startReadColumn = definition.getFirstReadingColumn();
		// this.maxReadRow = maxReadRow;
		// this.maxReadColumn = maxReadColumn;
		this.startWriteRow = definition.getFirstWritenRow();
		this.startWriteColumn = definition.getFirstWritenColumn();

		if (this.firstRowAsTitle) {
			this.titleRow = startReadRow;
			this.startReadRow++;
		}

		definition.setEvaluator(evaluator);

		if (!definition.isDefinitionFromXml()) {
			extractColumnDefinition();
		}
	}

	/**
	 * @param startRow
	 *            1,2,3,....
	 * @param startColumn
	 *            1,2,3,....
	 * @param maxRow
	 *            1,2,3,....
	 * @param maxColumn
	 */
	// public void setReadingRange(int startRow, int startColumn) {
	// if (startRow <= 1) {
	// startRow = 0;
	// } else {
	// startRow -= 1;
	// }
	// if (startColumn <= 1) {
	// startColumn = 0;
	// } else {
	// startColumn -= 1;
	// }
	// // if (maxColumn > 16384 || maxColumn <= 0) {
	// // maxColumn = 16384 - 1;
	// // }
	// // if (maxRow > 1048576 || maxRow <= 0) {
	// // maxRow = 1048576 - 1;
	// // }
	// // if (maxColumn <= 0) {
	// // maxColumn = -1;
	// // }
	// // if (maxRow <= 0) {
	// // maxRow = 1;
	// // }
	//
	// this.startReadRow = startRow;
	// this.startReadColumn = startColumn;
	// // this.maxReadRow = maxRow + startReadRow;
	// // this.maxReadColumn = maxColumn;
	//
	// this.currentReadingLine = startReadRow;
	// }

	public void setWritenRange(int startRow, int startColumn) {
		if (startRow < 0) {
			startRow = 0;
		}
		if (startColumn < 0) {
			startColumn = 0;
		}

		this.startWriteRow = startRow;
		this.startWriteColumn = startColumn;

		this.currentWritenLine = startWriteRow;
	}

	// @Override
	// public void setDefinition(DataTableDefinition definition) {
	// super.setDefinition(definition);
	// // this.maxReadColumn = definition.getFieldCount();
	//
	// FormulaEvaluator evaluator = workSheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
	// ((ExcelDataRecordParser) definition.getParser()).setEvaluator(evaluator);
	// List<DataFieldDefinition> fields = definition.getFields();
	// for (DataFieldDefinition field : fields) {
	// ((AbsExcelCell2XDataFieldParser) field.getParser()).setEvaluator(evaluator);
	// }
	// }

	private void extractColumnDefinition() throws DataException, DefinitionException {
		Row row = null;
		if (firstRowAsTitle) {
			row = workSheet.getRow(titleRow);
		} else {
			row = workSheet.getRow(startReadRow);
		}

		definition.extractColumnDefinition(this, row, firstRowAsTitle);
	}

	@Override
	public DataRecord readFirstRecord() throws DataException {
		currentReadingLine = startReadRow;

		DataRecord record = readNextRecord();

		return record;
	}

	@Override
	public DataRecord readNextRecord() throws DataException {
		// Row row = this.workSheet.getRow(this.currentReadingLine++);
		// if (row != null) {
		// return definition.parse(row);
		// }

		do {
			Row row = workSheet.getRow(currentReadingLine);
			if (row != null) {
				DataRecord rec = definition.parse(row);
				currentReadingLine++;

				if (recordFilter.isMatched(rec)) {
					return rec;
				}
			} else {
				return null;
			}
		} while (true);
	}

	@Override
	public List<DataRecord> readFirstRecords(int readCount) throws DataException {
		currentReadingLine = startReadRow;

		List<DataRecord> records = readNextRecords(readCount);

		return records;
	}

	@Override
	public List<DataRecord> readNextRecords(int readCount) throws DataException {
		// Row row = null;
		// List<DataRecord> records = new ArrayList<DataRecord>(readCount);
		// for (int i = 0; i < readCount; i++) {
		// row = this.workSheet.getRow(this.currentReadingLine++);
		// if (row != null) {
		// records.add(definition.parse(row));
		// } else {
		// break;
		// }
		// }
		// return records;

		if (readCount <= 0) {
			return null;
		}

		int index = 0;
		List<DataRecord> content = new ArrayList<DataRecord>(readCount);

		do {
			Row row = workSheet.getRow(currentReadingLine);

			if (row != null) {
				DataRecord rec = definition.parse(row);
				currentReadingLine++;

				if (recordFilter.isMatched(rec)) {
					content.add(rec);
					index++;
				}
			} else {
				break;
			}
		} while (index < readCount);

		if (content.size() == 0) {
			return null;
		} else {
			return content;
		}
	}
	
	@Override
	public List<DataRecord> readRecords(int startRowNo, int readCount) throws DataException {
		if (readCount <= 0) {
			return null;
		}

		if (startRowNo <= 1) {
			return this.readFirstRecords(readCount);
		} else {
			currentReadingLine = startRowNo;
			
			return readNextRecords(readCount);
		}
	}


	@Override
	public void writeTitle() throws DataException {
		Row row = this.workSheet.getRow(this.currentWritenLine);
		if (row == null) {
			row = this.workSheet.createRow(this.currentWritenLine);
		}

		String[] titleName = this.definition.getColumnNames();
		for (int i = this.startWriteColumn; i < titleName.length; i++) {
			Cell cell = row.getCell(i);
			if (cell == null) {
				cell = row.createCell(i);
			}

			cell.setCellValue(titleName[i]);
		}

		this.currentWritenLine++;
	}

	@Override
	public void appendRecord(DataRecord record, int[] fieldIndexMapping) throws DataException {
		Row row = this.workSheet.getRow(this.currentWritenLine);
		if (row == null) {
			row = this.workSheet.createRow(this.currentWritenLine);
		}

		if (record == null) {
			this.currentWritenLine++;
			return;
		}

		DataFieldDefinition[] fieldsDef = definition.getColumns();
		DataField[] values = record.getFields();

		int max = this.startWriteColumn + fieldsDef.length;
		int j = 0;
		for (int i = this.startWriteColumn; i < max; i++, j++) {
			DataField value = null;

			if (fieldIndexMapping == null) {
				value = values[i];
				if (value == null) {
					value = fieldsDef[j].getDefault();
				}
			} else {
				int index = fieldIndexMapping[j];
				if (index != -1) {
					value = values[index];
					if (value == null) {
						value = fieldsDef[j].getDefault();
					}
				}
			}

			Cell cell = row.getCell(i);
			if (cell == null) {
				cell = row.createCell(i);
			}
			
			if (value != null) {
				if (value instanceof StringField) {
					cell.setCellValue(((StringField) value).getValue());
				} else if (value instanceof DecimalField) {
					BigDecimal v = ((DecimalField) value).getValue();
					if (v != null) {
						cell.setCellValue(v.doubleValue());
					}
				} else if (value instanceof DateField) {
					// cell.setCellValue(new Date());
					Date dt = (Date) ((DateField) value).getValue();
					if (dt != null) {
						cell.setCellValue(dt);
					}
				} else {
					cell.setCellValue(value.toString());
				}
			}
		}

		this.currentWritenLine++;
	}

	@Override
	public void appendRecords(List<DataRecord> lines, int[] fieldIndexMapping) throws DataException {
		for (DataRecord line : lines) {
			appendRecord(line, fieldIndexMapping);
		}
	}

	@Override
	public void appendAll(DataTable tableSet, boolean includeTitle, IndexMapping indexMapping) throws DataException {
		int[] fieldIndexMapping = null;

		if (indexMapping != null) {
			fieldIndexMapping = indexMapping.indexMapping(definition, tableSet.getDefinition());
		}

		List<DataRecord> lines = tableSet.readFirstRecords(500);
		if (lines != null && lines.size() > 0) {
			if (includeTitle && firstRowAsTitle) {
				writeTitle();
			}

			do {
				this.appendRecords(lines, fieldIndexMapping);
				lines = tableSet.readNextRecords(500);
			} while (lines != null && lines.size() > 0);
		}
	}

	// public void writeRecord(String[] line) throws DataException {
	// writeRecord(recordParser.parse(definition, line));
	// }
	//
	// public void writeRecord(Object[] line) throws DataException {
	// writeRecord(recordParser.parse(definition, line));
	// }

	@Override
	public void close() {
	}

	@Override
	public void truncate() throws DataException {
		int rownum = startReadRow;
		int colnum = startReadColumn;
		int colcnt = definition.getColumnCount();
		Row row = this.workSheet.getRow(rownum++);
		while (row != null) {
			for (int i = 0; i < colcnt; i++) {
				Cell cell = row.getCell(i + colnum);
				if (cell != null)
					row.removeCell(cell);
			}

			row = this.workSheet.getRow(rownum++);
		}
		// Iterator<Row> it = workSheet.rowIterator();
		// for (; it.hasNext();) {
		// Row row = (Row) it.next();
		// workSheet.removeRow(row);
		// }
	}

	@Override
	public long getRecordCount() throws DataException {
		if (recordFilter != DEFAULT_FILTER) {
			return super.getFilteredRecordCount();
		}

		int row = startReadRow;
		long count = 0;
		// int seekIndex = 50;
		// while (true) {
		// Row value = workSheet.getRow(seekIndex);
		//
		// if (value == null) {
		// seekIndex = seekIndex / 2;
		// } else if (value != null) {
		// seekIndex ++;
		// }
		// }

		Row value = workSheet.getRow(row);
		if (value != null) {
			while (value != null) {
				count++;
				row++;
				value = workSheet.getRow(row);
			}
		}

		if (firstRowAsTitle) {
			count--;
		}

		return count;
	}

	public void autoSizeColumn() {
		int toColumn = definition.getColumnCount() - 1;
		for (int i = 0; i < toColumn; i++) {
			this.workSheet.autoSizeColumn(i);
		}
	}

	@Override
	public String getName() {
		return workSheet.getSheetName();
	}

	@Override
	public void setName(String name) {
		int isht = workSheet.getWorkbook().getSheetIndex(workSheet);
		workSheet.getWorkbook().setSheetName(isht, name);
	}

}
