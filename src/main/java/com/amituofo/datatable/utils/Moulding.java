package com.amituofo.datatable.utils;

import java.util.List;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.DataFieldDefinition;
import com.amituofo.datatable.DataFieldType;
import com.amituofo.datatable.DataRecord;
import com.amituofo.datatable.DataRecordFilter;
import com.amituofo.datatable.DataTable;
import com.amituofo.datatable.DataTableDefinition;
import com.amituofo.datatable.DefinitionException;
import com.amituofo.datatable.impl.memory.MemoryDataFieldDefinition;
import com.amituofo.datatable.impl.memory.MemoryDataTable;
import com.amituofo.datatable.impl.memory.MemoryDataTableDefinition;
import com.amituofo.datatable.type.DecimalFieldType;
import com.amituofo.datatable.type.StringFieldType;

public class Moulding {
	public static DataTable transpose(DataTable sourceTbl, boolean firstColumnAsTitle) throws DataException, DefinitionException {

		Object[][] matrix = toMatrix(sourceTbl, true);
		Object[][] newmatrix = transpose(matrix);

		return toSimpleDataTable(newmatrix, firstColumnAsTitle, sourceTbl.getName());
	}

	public static Object[][] transpose(Object[][] matrix) throws DataException, DefinitionException {
		if (matrix.length == 0) {
			return null;
		}

		Object[][] newmatrix = new Object[matrix[0].length][matrix.length];

		int starti = 0;
		int startj = 0;
		for (int i = starti; i < matrix.length; i++) {
			for (int j = startj; j < matrix[i].length; j++) {
				newmatrix[j][i] = matrix[i][j];
			}
		}

		return newmatrix;
	}

	public static Object[][] toMatrix(DataTable sourceTbl, boolean withTitle) throws DataException {
		int rowIndex = 0;
		int rownum = (int)sourceTbl.getRecordCount();
		int colnum = sourceTbl.getDefinition().getColumnCount();

		if (withTitle) {
			rownum++;
		}

		Object[][] matrix = new Object[rownum][colnum];

		if (withTitle) {
			String[] titles = sourceTbl.getDefinition().getColumnNames();
			for (int colIndex = 0; colIndex < titles.length; colIndex++) {
				matrix[0][colIndex] = titles[colIndex];
			}
			rowIndex++;
		}

		DataRecord row = sourceTbl.readFirstRecord();
		while (row != null && rowIndex < matrix.length) {
			Object[] values = row.getValues();
			for (int colIndex = 0; colIndex < values.length; colIndex++) {
				matrix[rowIndex][colIndex] = values[colIndex];
			}

			rowIndex++;
			row = sourceTbl.readNextRecord();
		}

		return matrix;
	}

	public static String[][] toStringMatrix(DataTable sourceTbl, boolean withTitle) throws DataException {
		int rowIndex = 0;
		int rownum = (int)sourceTbl.getRecordCount();
		int colnum = sourceTbl.getDefinition().getColumnCount();

		if (withTitle) {
			rownum++;
		}

		String[][] matrix = new String[rownum][colnum];

		if (withTitle) {
			String[] titles = sourceTbl.getDefinition().getColumnNames();
			for (int colIndex = 0; colIndex < titles.length; colIndex++) {
				matrix[0][colIndex] = titles[colIndex];
			}
			rowIndex++;
		}

		DataRecord row = sourceTbl.readFirstRecord();
		while (row != null && rowIndex < matrix.length) {
			Object[] values = row.getValues();
			for (int colIndex = 0; colIndex < values.length; colIndex++) {
				matrix[rowIndex][colIndex] = values[colIndex] == null ? null : values[colIndex].toString();
			}

			rowIndex++;
			row = sourceTbl.readNextRecord();
		}

		return matrix;
	}

	public static DataRecord toDataRecord(DataTableDefinition definition, Object[] fieldValues) throws DataException {
		final DataRecord dr = definition.newDataRecordInstance(false);
		DataFieldDefinition[] fields = definition.getColumns();

		for (int i = 0; i < fields.length; i++) {
			DataFieldDefinition fd = fields[i];
			int mappingIndex = fd.getMappingIndex();
			if (mappingIndex > -1 && mappingIndex < fieldValues.length) {
				dr.set(i, fields[i].parse(fieldValues[mappingIndex]));
			}
		}

		return dr;
	}

	public static DataTable toSimpleDataTable(Object[][] matrix, boolean firstRowAsTitle, String tableName) throws DataException, DefinitionException {
		DataTable newTbl = null;
		MemoryDataTableDefinition tblDef = new MemoryDataTableDefinition(tableName);
		DataFieldType[] types = extractColumnType(matrix, firstRowAsTitle);
		for (int colIndex = 0; colIndex < matrix[0].length; colIndex++) {
			String colName = (firstRowAsTitle ? (String) matrix[0][colIndex] : "Column" + colIndex);
			tblDef.addColumn(new MemoryDataFieldDefinition(colName, colIndex, types[colIndex]));
		}

		newTbl = new MemoryDataTable(tblDef);

		for (int rowIndex = firstRowAsTitle ? 1 : 0; rowIndex < matrix.length; rowIndex++) {
			newTbl.appendRecord(toDataRecord(tblDef, matrix[rowIndex]), null);
		}

		return newTbl;
	}

	public static MemoryDataTable toSimpleDataTable(DataTable dt, int rowCount, boolean withoutFilter) throws DataException, DefinitionException {
		MemoryDataTableDefinition tblDef = new MemoryDataTableDefinition(dt.getDefinition());
		MemoryDataTable newTbl = new MemoryDataTable(tblDef);

		DataRecordFilter filter = null;
		if (withoutFilter) {
			filter = dt.getFilter();
			dt.setFilter(null);
		}

		List<DataRecord> rows = dt.readFirstRecords(rowCount);
		if (rows != null) {
			newTbl.appendRecords(rows, null);
		}

		if (withoutFilter) {
			dt.setFilter(filter);
		}

		return newTbl;
	}

	public static DataFieldType[] extractColumnType(Object[][] matrix, boolean firstRowAsTitle) {
		if (matrix.length == 0) {
			return new DataFieldType[0];
		}

		// flgMatrix
		// 000001 = 1 (是数字)
		// 000010 = 2 (有负号)
		// 000100 = 4 (有小数点)
		// 001000 = 8 (是null)
		// 010000 = 16 (不是数字)
		// 100000 = 32 (Date)
		int[] rowFlgMatrix = new int[matrix.length];

		int[] rowLengthMatrix = new int[matrix.length];

		final int DECIMAL_FIELD_TYPE = 1;
		final int STRING_FIELD_TYPE = 2;
		int[] colTypeMatrix = new int[matrix[0].length];

		final int firstRow = firstRowAsTitle ? 1 : 0;
		final int maxCol = matrix[0].length;
		final int maxRow = matrix.length;
		for (int col = 0; col < maxCol; col++) {
			for (int row = firstRow; row < maxRow; row++) {
				Object val = matrix[row][col];
				if (val == null) {
					rowLengthMatrix[row] = 0;
					rowFlgMatrix[row] |= 8;
					continue;
				}

				if (val instanceof Number) {
					rowFlgMatrix[row] |= 1;
					continue;
				}

				if (val instanceof String) {
					String strval = (String) val;
					if (strval.trim().length() == 0) {
						rowLengthMatrix[row] = 0;
						rowFlgMatrix[row] |= 8;
						continue;
					}

					val = strval.trim();
					rowLengthMatrix[row] = strval.length();

					try {
						Double.parseDouble(strval);
						rowFlgMatrix[row] |= 1;
					} catch (NumberFormatException e) {
						rowFlgMatrix[row] |= 16;
						break;
					}

					if (strval.startsWith("-")) {
						rowFlgMatrix[row] |= 2;
					}

					if (strval.contains(".")) {
						rowFlgMatrix[row] |= 4;
					}
				}
			}

			for (int i = 0; i < rowFlgMatrix.length; i++) {
				if (colTypeMatrix[col] == STRING_FIELD_TYPE) {
					break;
				}

				int flg = rowFlgMatrix[i];

				if ((flg & 16) == 16) {
					colTypeMatrix[col] = STRING_FIELD_TYPE;
					break;
				}

				if ((flg & 8) == 8) {
					continue;
				}

				if (((flg & 1) == 1) || ((flg & 2) == 2) || ((flg & 4) == 4)) {
					colTypeMatrix[col] = DECIMAL_FIELD_TYPE;
					continue;
				}
			}

			// 如果是数字但是长度都一致，则认为是code，安string来
//			if (colTypeMatrix[col] == DECIMAL_FIELD_TYPE) {
//				int totalLen = 0;
//				int notNullCnt = 0;
//				int firstLen = 0;
//				for (int i = 0; i < rowLengthMatrix.length; i++) {
//					if (rowLengthMatrix[i] != 0) {
//						if (firstLen == 0) {
//							firstLen = rowLengthMatrix[i];
//						}
//						totalLen += rowLengthMatrix[i];
//						notNullCnt++;
//					}
//				}
//
//				if (notNullCnt > 1) {
//					if (totalLen / notNullCnt == firstLen) {
//						colTypeMatrix[col] = STRING_FIELD_TYPE;
//					}
//				}
//			}

			for (int i = 0; i < rowFlgMatrix.length; i++) {
				rowFlgMatrix[i] = 0;
				rowLengthMatrix[i] = 0;
			}
		}

		for (int col = 0; col < maxCol; col++) {
			if (colTypeMatrix[col] == 0) {
				colTypeMatrix[col] = STRING_FIELD_TYPE;
			}
		}

		DataFieldType[] colType = new DataFieldType[maxCol];
		for (int col = 0; col < maxCol; col++) {
			if (colTypeMatrix[col] == STRING_FIELD_TYPE) {
				colType[col] = new StringFieldType();
			} else if (colTypeMatrix[col] == DECIMAL_FIELD_TYPE) {
				colType[col] = new DecimalFieldType();
			}
		}

		return colType;
	}
	

}
