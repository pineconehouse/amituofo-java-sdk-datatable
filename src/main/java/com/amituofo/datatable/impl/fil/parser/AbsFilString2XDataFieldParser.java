package com.amituofo.datatable.impl.fil.parser;

import java.text.Format;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.DataField;
import com.amituofo.datatable.impl.basic.AbsDataFieldParser;

public abstract class AbsFilString2XDataFieldParser<T extends DataField, TO_TYPE> extends AbsDataFieldParser<T, String, TO_TYPE> {
	protected int columnStartIndex = 0;
	protected int length = 1;

	protected abstract TO_TYPE convertString(String value) throws DataException;

	public AbsFilString2XDataFieldParser(int columnStartIndex, int length, Format format) {
		super(format);
		this.columnStartIndex = columnStartIndex;
		this.length = length;
	}

	@Override
	protected TO_TYPE convertValue(String value) throws DataException {
		value = value.substring(columnStartIndex, columnStartIndex + length).trim();

		return convertString(value);
	}

	public int getColumnStartIndex() {
		return columnStartIndex;
	}

	public void setColumnStartIndex(int columnStartIndex) {
		this.columnStartIndex = columnStartIndex;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
}
