package com.amituofo.datatable.impl.basic.parser;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.impl.basic.AbsDataFieldParser;
import com.amituofo.datatable.type.StringField;

public class String2StringFieldParser extends AbsDataFieldParser<StringField, String, String> {
	public String2StringFieldParser() {
		super(null);
	}

	@Override
	protected String convertValue(String value) throws DataException {
		return value;
	}

//	@Override
//	public DataFieldParser clone() {
//		String2StringFieldParser newObj = new String2StringFieldParser();
//		return newObj;
//	}
}
