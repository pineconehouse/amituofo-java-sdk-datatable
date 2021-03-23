package com.amituofo.datatable.type;

import com.amituofo.datatable.impl.basic.AbsDataField;

public class StringField extends AbsDataField {
	public StringField() {
	}
	
	public StringField(Object defaultValue) {
		super(defaultValue);
	}

	@Override
	public String getValue() {
		return (String) value;
	}

	@Override
	public void setValue(Object o) {
		// Assert!
		this.value = (String) o;
	}
}
