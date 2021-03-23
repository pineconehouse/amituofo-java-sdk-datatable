package com.amituofo.datatable.impl.basic.parser;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.impl.basic.AbsDataFieldParser;
import com.amituofo.datatable.type.ObjectField;

public class Object2ObjectFieldParser extends AbsDataFieldParser<ObjectField, Object, Object> {
	public Object2ObjectFieldParser() {
		super(null);
	}

	@Override
	protected Object convertValue(Object value) throws DataException {
		return value;
	}

//	@Override
//	public DataFieldParser clone() {
//		Object2ObjectFieldParser newObj = new Object2ObjectFieldParser();
//		return newObj;
//	}
}
