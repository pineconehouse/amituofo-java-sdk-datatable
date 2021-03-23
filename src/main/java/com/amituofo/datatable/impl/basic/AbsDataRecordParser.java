package com.amituofo.datatable.impl.basic;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.DataFieldDefinition;
import com.amituofo.datatable.DataRecord;
import com.amituofo.datatable.DataRecordParser;
import com.amituofo.datatable.DataTableDefinition;

public abstract class AbsDataRecordParser implements DataRecordParser {
	protected DataTableDefinition definition;

	// public AbsDataRecordParser(DataTableDefinition definition) {
	// this.definition = definition;
	// }
	
	// public abstract DataRecordParser<RECORD_TYPE, FIELD_TYPE> clone();

	protected abstract Object[] parseValues(Object record) throws DataException;

	@Override
	public DataTableDefinition getDefinition() {
		return definition;
	}

	@Override
	public void setDefinition(DataTableDefinition definition) {
		this.definition = definition;
	}

	@Override
	public DataRecord parseRecord(Object record) throws DataException {
		Object[] values = parseValues(record);

		final DataRecord dr = definition.newDataRecordInstance(false);
		DataFieldDefinition[] fields = definition.getColumns();

		for (int i = 0; i < fields.length; i++) {
			int mappingIndex = fields[i].getMappingIndex();
			if (mappingIndex > -1 && mappingIndex < values.length) {
				dr.set(i, fields[i].parse(values[mappingIndex]));
			} else {
				dr.set(i, fields[i].newDataFieldInstance());
			}
		}

		return dr;
	}

//	public DataRecord parseFields(Object[] fieldValues) throws DataException {
//		DataRecord dr = definition.newDataRecordInstance();
//		DataFieldDefinition[] fields = definition.getColumns();
//
//		for (int i = 0; i < fields.length; i++) {
//			int mappingIndex = fields[i].getMappingIndex();
//			if (mappingIndex >= -1 && mappingIndex < fieldValues.length) {
//				dr.set(i, fields[i].parse(fieldValues[mappingIndex]));
//			}
//		}
//
//		return dr;
//	}

}
