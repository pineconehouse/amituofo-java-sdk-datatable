package com.amituofo.datatable.impl.fil.render;

import com.amituofo.datatable.DataField;
import com.amituofo.datatable.DataFieldDefinition;
import com.amituofo.datatable.DataRecord;
import com.amituofo.datatable.impl.basic.AbsDataRecordRender;
import com.amituofo.datatable.type.StringField;

public class FilDataRecordRender extends AbsDataRecordRender {
	public FilDataRecordRender() {
	}

	@Override
	public String rending(DataRecord record, int[] fieldIndexMapping) {
		if (record == null) {
			return "";
		}

		DataFieldDefinition[] fieldsDef = definition.getColumns();
		DataField[] values = record.getFields();
		StringBuffer content = new StringBuffer();

		if (fieldIndexMapping == null) {
			for (int i = 0; i < fieldsDef.length; i++) {
				if (i < values.length && values[i] != null) {
					content.append(fieldsDef[i].rending(values[i]));
				} else {
					content.append(fieldsDef[i].rending(fieldsDef[i].getDefault()));
				}
			}
		} else {
			for (int i = 0; i < fieldsDef.length; i++) {
				int index = fieldIndexMapping[i];
				if (index < values.length && values[index] != null) {
					content.append(fieldsDef[i].rending(values[index]));
				} else {
					content.append(fieldsDef[i].rending(fieldsDef[i].getDefault()));
				}
			}
		}

		return content.toString();
	}

	@Override
	public String rendingTitle() {
		DataFieldDefinition[] fieldsDef = definition.getColumns();
		String[] titles = definition.getColumnNames();
		StringBuffer content = new StringBuffer();

		for (int i = 0; i < titles.length; i++) {
			DataFieldDefinition def = fieldsDef[i];
			DataField title = new StringField(titles[i]);
			content.append(def.rending(title));
		}

		return content.toString();
	}

	// @Override
	// public DataRecordRender clone() {
	// FilDataRecordRender newObj = new FilDataRecordRender();
	// return newObj;
	// }
}
