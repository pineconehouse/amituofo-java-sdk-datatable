package com.amituofo.datatable.impl.fil.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.type.DateField;

public class FilString2DateFieldParser extends AbsFilString2XDataFieldParser<DateField, Date> {
	public FilString2DateFieldParser(int columnStartIndex, int length) {
		super(columnStartIndex, length, null);
	}

	public FilString2DateFieldParser(int columnStartIndex, int length, DateFormat format) {
		super(columnStartIndex, length, format);
	}

	@Override
	protected Date convertString(String value) throws DataException {
		if (value == null || value.length() == 0) {
			return null;
		}

		try {
			return ((DateFormat) format).parse(value);
		} catch (ParseException e) {
			throw new DataException("Exception when parse date value " + value, e);
		}
	}

//	@Override
//	public DataFieldParser clone() {
//		FilString2DateFieldParser newObj = new FilString2DateFieldParser(columnStartIndex, length, (DateFormat) format);
//		return newObj;
//	}
}
