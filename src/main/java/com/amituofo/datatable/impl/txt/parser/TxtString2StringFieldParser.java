package com.amituofo.datatable.impl.txt.parser;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.type.StringField;

public class TxtString2StringFieldParser extends AbsTxtString2XDataFieldParser<StringField, String> {

	public TxtString2StringFieldParser(char quote, boolean trimValue) {
		super(null, quote, trimValue);
	}

	@Override
	protected String convertString(String value) throws DataException {
		return value;
	}

//	@Override
//	public DataFieldParser clone() {
//		TxtString2StringFieldParser newObj = new TxtString2StringFieldParser(quote, trimValue);
//		return newObj;
//	}
}
