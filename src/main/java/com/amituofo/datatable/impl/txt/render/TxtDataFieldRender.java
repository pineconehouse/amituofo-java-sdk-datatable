package com.amituofo.datatable.impl.txt.render;

import java.text.Format;

import com.amituofo.datatable.DataField;
import com.amituofo.datatable.impl.basic.AbsDataFieldRender;

public class TxtDataFieldRender extends AbsDataFieldRender {
	protected char quote = '"';
	protected boolean forceQuote = false;
	protected char delimiter = ',';

	public TxtDataFieldRender(char delimiter, char quote, boolean forceQuote) {
		this(null, delimiter, quote, forceQuote);
	}

	public TxtDataFieldRender(Format format, char delimiter, char quote, boolean forceQuote) {
		super(format);
		this.quote = quote;
		this.delimiter = delimiter;
		this.forceQuote = forceQuote;
	}

	@Override
	public String rending(DataField field) {
		if (field != null && field.getValue() != null) {
			String tmpValue = null;
			if (format != null) {
				tmpValue = format.format(field.getValue());
			} else {
				tmpValue = field.getValue().toString();
			}

			if (quote != ' ') {
				if (forceQuote) {
					tmpValue = quote + tmpValue + quote;
				} else if (tmpValue.indexOf(delimiter) != -1) {
					tmpValue = quote + tmpValue + quote;
				}
			}

			return tmpValue;
		} else {
			return "";
		}
	}

	// @Override
	// public DataFieldRender clone() {
	// TxtDataFieldRender newObj = new TxtDataFieldRender(format, quote, delimiter, forceQuote);
	// return newObj;
	// }

	public char getQuote() {
		return quote;
	}

	public void setQuote(char quote) {
		this.quote = quote;
	}

	public boolean isForceQuote() {
		return forceQuote;
	}

	public void setForceQuote(boolean forceQuote) {
		this.forceQuote = forceQuote;
	}

	public char getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(char delimiter) {
		this.delimiter = delimiter;
	}

}
