package com.amituofo.datatable.type;

import java.math.BigDecimal;

import com.amituofo.datatable.impl.basic.AbsDataField;

public class DecimalField extends AbsDataField {
	
	public DecimalField() {
	}
	
	public DecimalField(Object defaultValue) {
		super(defaultValue);
	}

	@Override
	public BigDecimal getValue() {
		return (BigDecimal) value;
	}

	@Override
	public void setValue(Object o) {
		// Assert!
		this.value = (BigDecimal) o;
	}
}
