package com.amituofo.datatable.type;

import com.amituofo.datatable.impl.basic.AbsDataField;

public class ObjectField extends AbsDataField {
	
	public ObjectField() {
	}
	
	public ObjectField(Object defaultValue) {
		super(defaultValue);
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public void setValue(Object o) {
		// Assert!
		this.value = o;
	}
}
