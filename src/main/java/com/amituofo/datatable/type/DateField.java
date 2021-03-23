package com.amituofo.datatable.type;

import java.util.Date;

import com.amituofo.datatable.impl.basic.AbsDataField;

public class DateField extends AbsDataField {
	
	public DateField() {
	}
	
	public DateField(Object defaultValue) {
		super(defaultValue);
	}

	@Override
	public Date getValue() {
		return (Date) value;
	}

	@Override
	public void setValue(Object o) {
		// Assert!
		this.value = (Date) o;
	}
}
