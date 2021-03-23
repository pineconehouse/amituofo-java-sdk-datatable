package com.amituofo.datatable.impl.fil.parser;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.impl.basic.AbsDataRecordParser;

public class FilDataRecordParser extends AbsDataRecordParser {
	private String[] row = new String[1];

	public FilDataRecordParser() {
	}

	// @Override
	// public DataRecordParser<String, String> clone() {
	// FilDataRecordParser newObj = new FilDataRecordParser();
	// return newObj;
	// }

	@Override
	protected Object[] parseValues(Object record) throws DataException {
		//return split((String) record, definition.getColumnCount());
		row[0]=(String) record;
		return row;
	}

	// public DataRecord parseRecord(Object record) throws DataException {
	// String[] values = split((String) record, definition.getColumnCount());
	//
	// DataRecord dr = definition.newDataRecordInstance(false);
	// DataFieldDefinition[] fields = definition.getColumns();
	//
	// int maxLen = values.length > fields.length ? fields.length : values.length;
	//
	// for (int i = 0; i < maxLen; i++) {
	// dr.set(i, fields[i].parse(values[i]));
	// }
	//
	// return dr;
	// }
	//
	// public DataRecord parseFields(Object[] values) throws DataException {
	// DataRecord dr = definition.newDataRecordInstance(false);
	// DataFieldDefinition[] fields = definition.getColumns();
	//
	// int maxLen = values.length > fields.length ? fields.length : values.length;
	//
	// for (int i = 0; i < maxLen; i++) {
	// dr.set(i, fields[i].parse(values[i]));
	// }
	//
	// return dr;
	// }

//	public static String[] split(String row, int columnCount) throws DataException {
//		String[] strs = new String[columnCount];
//		for (int i = 0; i < strs.length; i++) {
//			strs[i] = row;
//		}
//		return strs;
//	}

	// public static String[] split(String row, int[] columnStartIndex) throws DataException {
	// List<String> strs = new ArrayList<String>();
	//
	// int length = row.length();
	// if (length > 0) {
	// int max = columnStartIndex.length - 1;
	// for (int i = 0; i < max; i++) {
	// strs.add(row.substring(columnStartIndex[i], columnStartIndex[i + 1]));
	// }
	//
	// strs.add(row.substring(columnStartIndex[max]));
	// }
	//
	// String[] result = new String[strs.size()];
	// strs.toArray(result);
	// return result;
	// }

}
