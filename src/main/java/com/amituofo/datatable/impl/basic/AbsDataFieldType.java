package com.amituofo.datatable.impl.basic;

import com.amituofo.datatable.DataFieldType;

public abstract class AbsDataFieldType implements DataFieldType {
	protected String name;
	// private boolean isKey = false;
	protected int integerLength = 0;
	protected int decimalLength = 0;
	protected boolean allowEmpty = true;
	protected Object defaultValue = null;
	protected String description = null;

	public abstract DataFieldType clone();
	public abstract int getTotalLength();

	public AbsDataFieldType(String name, boolean allowEmpty, Object defaultValue) {
		this.name = name;
		this.allowEmpty = allowEmpty;
		this.defaultValue = defaultValue;
	}

	public AbsDataFieldType(String name, int integerLength, int decimalLength, boolean allowEmpty, Object defaultValue) {
		this.name = name;
		if (integerLength > 0) {
			this.integerLength = integerLength;
		}
		if (decimalLength > 0) {
			this.decimalLength = decimalLength;
		}
		this.allowEmpty = allowEmpty;
		this.defaultValue = defaultValue;
	}

	protected void copyProperies(AbsDataFieldType o) {
		o.name = name;
		// o.isKey = isKey;
		o.allowEmpty = allowEmpty;
		o.integerLength = integerLength;
		o.decimalLength = decimalLength;
		o.defaultValue = defaultValue;
		o.description = description;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean isAllowEmpty() {
		return allowEmpty;
	}

	@Override
	public void setAllowEmpty(boolean allowEmpty) {
		this.allowEmpty = allowEmpty;
	}

	@Override
	public boolean isNoLength() {
		return integerLength > 0;
	}

	@Override
	public void setNoLength(boolean noLength) {
		if (noLength) {
			this.integerLength = 0;
		}
	}

	@Override
	public int getIntegerLength() {
		return integerLength;
	}

	@Override
	public void setIntegerLength(int integerLength) {
		this.integerLength = integerLength;
	}

	@Override
	public int getDecimalLength() {
		return decimalLength;
	}

	@Override
	public void setDecimalLength(int decimalLength) {
		this.decimalLength = decimalLength;
	}

	@Override
	public Object getDefaultValue() {
		return defaultValue;
	}

	@Override
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}
}
