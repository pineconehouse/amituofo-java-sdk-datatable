package com.amituofo.datatable.impl.basic.parser;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.impl.basic.AbsDataFieldParser;
import com.amituofo.datatable.type.StringField;

public class Object2StringFieldParser extends AbsDataFieldParser<StringField, Object, String> {
	public Object2StringFieldParser() {
		super(null);
	}

	@Override
	protected String convertValue(Object value) throws DataException {
		return value.toString();
	}

//	@Override
//	public DataFieldParser clone() {
//		Object2StringFieldParser newObj = new Object2StringFieldParser();
//		return newObj;
//	}
}
