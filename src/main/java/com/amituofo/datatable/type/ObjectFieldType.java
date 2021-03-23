package com.amituofo.datatable.type;

import com.amituofo.datatable.DataField;
import com.amituofo.datatable.InvalidDataException;
import com.amituofo.datatable.impl.basic.AbsDataFieldType;

public class ObjectFieldType extends AbsDataFieldType {

	public ObjectFieldType() {
		super("object", true, null);
	}

	public ObjectFieldType(boolean allowEmpty, Object defaultValue) {
		super("object", allowEmpty, defaultValue);
	}

	public ObjectFieldType(Object defaultValue) {
		super("object", true, defaultValue);
	}

	public void validate(DataField field) throws InvalidDataException {
		if (field == null) {
			return;
		}

		Object value = field.getValue();

		if (!allowEmpty && value == null) {
			throw new InvalidDataException("Empty value is not allowed.");
		}
	}

	public DataField newField() {
		return new ObjectField(defaultValue);
	}

	public ObjectFieldType clone() {
		ObjectFieldType newObj = new ObjectFieldType();
		copyProperies(newObj);
		return newObj;
	}

	@Override
	public int getTotalLength() {
		return integerLength + decimalLength;
	}
}
