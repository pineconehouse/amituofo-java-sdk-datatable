package com.amituofo.datatable.impl.txt.render;

import com.amituofo.datatable.DataField;
import com.amituofo.datatable.DataFieldDefinition;
import com.amituofo.datatable.DataRecord;
import com.amituofo.datatable.impl.basic.AbsDataRecordRender;

public class TxtDataRecordRender extends AbsDataRecordRender {
	private char delimiter = ',';

	public TxtDataRecordRender(char delimiter) {
		this.delimiter = delimiter;
	}

	@Override
	public String rending(DataRecord record, int[] fieldIndexMapping) {
		if (record == null) {
			return "";
		}

		DataFieldDefinition[] fieldsDef = definition.getColumns();
		DataField[] values = record.getFields();
		StringBuffer content = new StringBuffer();

		int max = (fieldsDef.length) - 1;
		if (fieldIndexMapping == null) {
			for (int i = 0; i < max; i++) {
				if (i < values.length && values[i] != null) {
					content.append(fieldsDef[i].rending(values[i]));
				} else {
					// content.append("");
					// content.append(fieldsDef[i].getType().getDefaultValue());
					content.append(fieldsDef[i].rending(fieldsDef[i].getDefault()));
				}
				content.append(delimiter);
			}

			if (max < values.length && values[max] != null) {
				content.append(fieldsDef[max].rending(values[max]));
			} else {
				// content.append("");
				// content.append(fieldsDef[max].getType().getDefaultValue());
				content.append(fieldsDef[max].rending(fieldsDef[max].getDefault()));
			}
		} else {
			for (int i = 0; i < max; i++) {
				int index = fieldIndexMapping[i];
				if (index < values.length && index != -1 && values[index] != null) {
					content.append(fieldsDef[i].rending(values[index]));
				} else {
					// content.append("");
					// content.append(fieldsDef[i].getType().getDefaultValue());
					content.append(fieldsDef[i].rending(fieldsDef[i].getDefault()));
				}
				content.append(delimiter);
			}

			int index = fieldIndexMapping[max];
			if (index < values.length && index != -1 && values[index] != null) {
				content.append(fieldsDef[max].rending(values[index]));
			} else {
				// content.append("");
				// content.append(fieldsDef[max].getType().getDefaultValue());
				content.append(fieldsDef[max].rending(fieldsDef[max].getDefault()));
			}
		}

		return content.toString();
	}

	@Override
	public String rendingTitle() {
		StringBuffer content = new StringBuffer();

		String[] title = definition.getColumnNames();
		int max = title.length - 1;
		for (int i = 0; i < max; i++) {
			if (title[i] != null) {
				content.append(title[i]);
			} else {
				content.append("");
			}
			content.append(delimiter);
		}

		if (title[max] != null) {
			content.append(title[max]);
		} else {
			content.append("");
		}

		return content.toString();
	}

	// @Override
	// public DataRecordRender clone() {
	// TxtDataRecordRender newObj = new TxtDataRecordRender(delimiter);
	// return newObj;
	// }

	public char getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(char delimiter) {
		this.delimiter = delimiter;
	}

}
