package com.amituofo.datatable.impl.fil.parser;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.type.StringField;

public class FilString2StringFieldParser extends AbsFilString2XDataFieldParser<StringField, String> {

	public FilString2StringFieldParser(int columnStartIndex, int length) {
		super(columnStartIndex, length, null);
	}

	@Override
	protected String convertString(String value) throws DataException {
		if (value == null || value.length() == 0) {
			return null;
		}
		
		return value;
	}

//	@Override
//	public DataFieldParser clone() {
//		FilString2StringFieldParser newObj = new FilString2StringFieldParser(columnStartIndex, length);
//		return newObj;
//	}
}
