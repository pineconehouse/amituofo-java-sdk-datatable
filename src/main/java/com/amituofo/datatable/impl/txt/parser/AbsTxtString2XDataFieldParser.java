package com.amituofo.datatable.impl.txt.parser;

import java.text.Format;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.DataField;
import com.amituofo.datatable.impl.basic.AbsDataFieldParser;

public abstract class AbsTxtString2XDataFieldParser<T extends DataField, TO_TYPE> extends AbsDataFieldParser<T, String, TO_TYPE> {
	protected char quote = '"';
	protected boolean trimValue = false;

	protected abstract TO_TYPE convertString(String value) throws DataException;

	public AbsTxtString2XDataFieldParser(Format format, char quote, boolean trimValue) {
		super(format);
		this.quote = quote;
		this.trimValue = trimValue;
	}

	@Override
	protected TO_TYPE convertValue(String value) throws DataException {
		return convertString(wipeQuote(value));
	}

	private String wipeQuote(String value) throws DataException {
		if (quote != ' ' && value.length() != 0) {
			if (value.charAt(0) == quote && value.charAt(value.length() - 1) == quote) {
				value = value.substring(1, value.length() - 1);
			}
		}

		if (trimValue) {
			value = value.trim();
		}
		
		// Need?
		if (value.length() == 0) {
			return null;
		}

		return value;
	}

	public char getQuote() {
		return quote;
	}

	public void setQuote(char quote) {
		this.quote = quote;
	}

	public boolean isTrimValue() {
		return trimValue;
	}

	public void setTrimValue(boolean trimValue) {
		this.trimValue = trimValue;
	}

}
