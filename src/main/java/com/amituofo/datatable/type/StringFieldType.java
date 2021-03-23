package com.amituofo.datatable.type;

import com.amituofo.datatable.DataField;
import com.amituofo.datatable.InvalidDataException;
import com.amituofo.datatable.impl.basic.AbsDataFieldType;

public class StringFieldType extends AbsDataFieldType {

	public StringFieldType() {
		super("string", true, null);
	}

	public StringFieldType(int length, boolean allowEmpty, String defaultValue) {
		super("string", length, 0, allowEmpty, defaultValue);
	}
	
	public StringFieldType(String defaultValue) {
		super("string", true, defaultValue);
	}

	public StringFieldType(boolean allowEmpty, String defaultValue) {
		super("string", allowEmpty, defaultValue);
	}

	public void validate(DataField field) throws InvalidDataException {
		if (field == null) {
			return;
		}

		Object value = field.getValue();

		if (value != null && !(value instanceof String)) {
			throw new InvalidDataException("Only String value is allowed.");
		}

		String v = (String) value;
		if (!allowEmpty && (value == null || v.length() == 0)) {
			throw new InvalidDataException("Empty value is not allowed.");
		}

		if (integerLength > 0 && v != null) {
			int len = v.length();
			if (len > integerLength) {
				throw new InvalidDataException("Value length illegal. The length should be <= " + integerLength);
			}
		}
	}

	public DataField newField() {
		return new StringField(defaultValue);
	}

	public StringFieldType clone() {
		StringFieldType newObj = new StringFieldType();
		copyProperies(newObj);
		return newObj;
	}

	@Override
	public int getTotalLength() {
		return integerLength;
	}
}
