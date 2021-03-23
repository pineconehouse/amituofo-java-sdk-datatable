package com.amituofo.datatable.impl.basic.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.impl.basic.AbsDataFieldParser;
import com.amituofo.datatable.type.DateField;

public class String2DateFieldParser extends AbsDataFieldParser<DateField, String, Date> {
	public String2DateFieldParser(DateFormat format) {
		super(format);
	}

	public String2DateFieldParser(String pattern) {
		super(new SimpleDateFormat(pattern));
	}

	@Override
	protected Date convertValue(String value) throws DataException {
		if (value == null || value.trim().length() == 0) {
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
//		String2DateFieldParser newObj = new String2DateFieldParser((DateFormat) format);
//		return newObj;
//	}
}
