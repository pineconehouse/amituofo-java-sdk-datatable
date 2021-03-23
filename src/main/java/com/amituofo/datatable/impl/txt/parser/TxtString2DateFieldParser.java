package com.amituofo.datatable.impl.txt.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.type.DateField;

public class TxtString2DateFieldParser extends AbsTxtString2XDataFieldParser<DateField, Date> {
	public TxtString2DateFieldParser(char quote, boolean trimValue) {
		super(null, quote, trimValue);
	}

	public TxtString2DateFieldParser(DateFormat format, char quote, boolean trimValue) {
		super(format, quote, trimValue);
	}

	@Override
	protected Date convertString(String value) throws DataException {
		if (value == null) {
			return null;
		}
		
		value = value.trim();
		if (value.length() == 0) {
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
//		TxtString2DateFieldParser newObj = new TxtString2DateFieldParser((DateFormat) format, quote, trimValue);
//		return newObj;
//	}
}
