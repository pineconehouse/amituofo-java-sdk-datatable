package com.amituofo.datatable.impl.basic;

import java.text.Format;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.DataField;
import com.amituofo.datatable.DataFieldDefinition;
import com.amituofo.datatable.DataFieldParser;

public abstract class AbsDataFieldParser<T extends DataField, FROM_TYPE, TO_TYPE> implements DataFieldParser {
	protected Format format = null;

	protected abstract TO_TYPE convertValue(FROM_TYPE value) throws DataException;

//	public abstract DataFieldParser clone();

	public AbsDataFieldParser(Format format) {
		this.format = format;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T parse(DataFieldDefinition definition, Object value) throws DataException {
		T field = (T) definition.newDataFieldInstance();
		if (value == null) {
			// To be default value
			// field.setValue(null);
		} else {
			TO_TYPE newVal = convertValue((FROM_TYPE) value);
			if (newVal != null) {
				field.setValue(newVal);
			}
		}

		return field;
	}

	@Override
	public Format getFormat() {
		return format;
	}

	@Override
	public void setFormat(Format format) {
		this.format = format;
	}

}
