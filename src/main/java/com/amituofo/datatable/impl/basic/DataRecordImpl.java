package com.amituofo.datatable.impl.basic;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.DataField;
import com.amituofo.datatable.DataFieldDefinition;
import com.amituofo.datatable.DataRecord;

public class DataRecordImpl implements DataRecord {
	private static final long serialVersionUID = -784600834435839227L;

	protected DataField[] fields = null;
	protected DataFieldDefinition[] definitions;

	// protected List<String> errors = null;

	// public DataRecordImpl(int initSize) {
	// this.fields = new DataField[initSize];
	// }

	public DataRecordImpl(DataFieldDefinition[] definitions, boolean initFields) {
		this.definitions = definitions;
		this.fields = new DataField[definitions.length];
		if (initFields) {
			for (int i = 0; i < fields.length; i++) {
				fields[i] = definitions[i].newDataFieldInstance();
			}
		}
	}

	@Override
	public DataField get(int index) {
		return fields[index];
	}

	@Override
	public DataField get(String name) {
		int i = getFieldIndex(name);
		if (i != -1) {
			return fields[i];
		}

		return null;
	}

	@Override
	public DataField[] getFields() {
		return fields;
	}

	@Override
	public Object getValue(int index) {
		if (fields[index] != null) {
			return fields[index].getValue();
		}

		return null;
	}

	@Override
	public Object getValue(String name) {
		int i = getFieldIndex(name);
		if (i != -1) {
			if (fields[i] != null) {
				return fields[i].getValue();
			}
		}

		return null;
	}

	@Override
	public String getValueAsString(int index) {
		return (String) getValue(index);
	}

	@Override
	public String getValueAsString(String name) {
		return (String) getValue(name);
	}

	@Override
	public Number getValueAsDecimal(int index) {
		return (Number) getValue(index);
	}

	@Override
	public Number getValueAsDecimal(String name) {
		return (Number) getValue(name);
	}

	@Override
	public String toString() {
		return toString(", ");
	}

	@Override
	public String toString(String delimiter) {
		if (fields.length > 0) {
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < fields.length - 1; i++) {
				buf.append(fields[i].toString());
				buf.append(delimiter);
			}

			buf.append(fields[fields.length - 1].toString());

			return buf.toString();
		} else {
			return "";
		}
	}

	@Override
	public String[] toStringValues() {
		String[] values = new String[fields.length];
		for (int i = 0; i < fields.length; i++) {
			if (fields[i] != null) {
				values[i] = fields[i].toString();
			}
		}
		return values;
	}

	@Override
	public Object[] getValues() {
		Object[] values = new Object[fields.length];
		for (int i = 0; i < fields.length; i++) {
			if (fields[i] != null) {
				values[i] = fields[i].getValue();
			}
		}
		return values;
	}

	// public String[] renderValues(DataTableDefinition definition) {
	// String[] values = new String[fields.length];
	// for (int i = 0; i < fields.length; i++) {
	// if (fields[i] != null) {
	// values[i] = definition.getField(i).rending(fields[i]);
	// }
	// }
	// return values;
	// }

	// public String getFieldName(int index) {
	// return definition.getField(index).getName();
	// }

	// public String[] getFieldNames() {
	// String[] values = new String[definition.getFieldCount()];
	// for (int i = 0; i < values.length; i++) {
	// values[i] = getFieldName(i);
	// }
	// return values;
	// }

	@Override
	public int getFieldCount() {
		return fields.length;
	}

	@Override
	public int getFieldIndex(String name) {
		for (int i = 0; i < definitions.length; i++) {
			if (name.equalsIgnoreCase(definitions[i].getName())) {
				return i;
			}
		}

		return -1;
	}

	// public DataField parse(int index, Object value) throws DataException {
	// fields[index] = definition.getField(index).parse(value);
	// return fields[index];
	// }
	//
	// public DataField parse(String name, Object value) throws DataException {
	// int i = getFieldIndex(name);
	// if (i != -1) {
	// fields[i] = definition.getField(i).parse(value);
	// return fields[i];
	// }
	//
	// return null;
	// }

	// public DataRecord parse(Object[] values) throws DataException {
	// int len = fields.length - values.length;
	// if (len > 0) {
	// for (int i = 0; i < values.length; i++) {
	// fields[i] = definition.getField(i).parse(values[i]);
	// }
	// } else {
	// for (int i = 0; i < fields.length; i++) {
	// fields[i] = definition.getField(i).parse(values[i]);
	// }
	// }
	//
	// return this;
	// }

	@Override
	public DataField set(int index, DataField value) throws DataException {
		fields[index] = value;
		return fields[index];
	}

	@Override
	public DataField set(int index, Object value) throws DataException {
		// fields[index] = definition.getField(index).newDataFieldInstance();
		fields[index].setValue(value);

		// fields[index] = definition.getField(index).parse(value);
		return fields[index];
	}

	// public DataField set(String name, Object value) throws DataException {
	// int i = getFieldIndex(name);
	// if (i != -1) {
	// set(i, value);
	// fields[i].setValue(value);
	//
	// return fields[i];
	// }
	//
	// return null;
	// }

	@Override
	public DataRecord set(Object[] values) throws DataException {
		int maxLen = values.length > fields.length ? fields.length : values.length;

		for (int i = 0; i < maxLen; i++) {
			fields[i].setValue(values[i]);
		}

		return this;
	}

	// public boolean isValid() {
	// return errors == null || errors.size() == 0;
	// }
	//
	// public List<String> getErrors() {
	// return errors;
	// }
	//
	// public void appendError(String message) {
	// if (errors == null) {
	// errors = new ArrayList<String>();
	// }
	//
	// errors.add(message);
	// }
	//
	// public void clearErrors() {
	// errors = null;
	// }
	//
	// public DataTableDefinition getDefinition() {
	// return definition;
	// }

	// public List<String> validate(DataTableDefinition definition) {
	//
	// List<String> errors = new ArrayList<String>();
	//
	// DataFieldDefinition[] fieldDefs= definition.getFields();
	// for (int i = 0; i < fieldDefs.length; i++) {
	// fieldDefs[i].validate(fields[i]);
	// }
	//
	// return (errors.size() > 0) ? errors : null;

	// // TODO 这个版本错误会不发生
	// if (definition.getFieldCount() != fields.length) {
	// String msg = "Column count not match! Actual count:" + fields.length + ". Definition count:" + definition.getFieldCount();
	// // String msg = "エラー内容：項目数が定義と異なります。　データ:" + fieldValues.length + " 定義:" + columnDefinition.length;
	// appendError(msg);
	// }

	// DataField[] fields = this.getFields();
	// for (int i = 0; i < fields.length; i++) {
	// DataField field = fields[i];
	// if (field != null) {
	// field.validate();
	// if (field.isInvalid()) {
	// this.appendError("Column" + (i + 1) + " [" + field.getDefinition().getName() + "] " + field.getErrors());
	// // this.appendError("Column:" + (i + 1) + " " + field.getErrors());
	// }
	// }
	// }
	// }

	@Override
	public boolean equals(DataRecord obj) {
		DataField[] objfields = obj.getFields();
		if (objfields.length != this.fields.length) {
			return false;
		}

		for (int i = 0; i < objfields.length; i++) {
			DataField dataField = objfields[i];
			if (dataField != null && this.fields[i] == null) {
				return false;
			}

			if (dataField == null && this.fields[i] != null) {
				return false;
			}

			if (dataField != null && this.fields[i] != null && !dataField.equals(this.fields[i])) {
				return false;
			}
		}

		return true;
	}

}
