package com.amituofo.datatable.impl.basic.parser;

import java.math.BigDecimal;
import java.text.NumberFormat;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.impl.basic.AbsDataFieldParser;
import com.amituofo.datatable.type.DecimalField;

public class String2DecimalFieldParser extends AbsDataFieldParser<DecimalField, String, BigDecimal> {

	public String2DecimalFieldParser() {
		super(null);
	}

	public String2DecimalFieldParser(NumberFormat format) {
		super(format);
	}

	@Override
	protected BigDecimal convertValue(String value) throws DataException {
		if (value == null || value.trim().length() == 0) {
			return null;
		}
		
		try {
			if (format == null) {
				return new BigDecimal(value);
			} else {
				return new BigDecimal(((NumberFormat) format).parse(value).doubleValue());
			}
		} catch (Throwable e) {
			throw new DataException("Exception when parse decimal value " + value, e);
		}
	}

//	@Override
//	public DataFieldParser clone() {
//		String2DecimalFieldParser newObj = new String2DecimalFieldParser((NumberFormat) format);
//		return newObj;
//	}
}
