package com.amituofo.datatable.impl.txt.parser;

import java.math.BigDecimal;
import java.text.NumberFormat;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.type.DecimalField;

public class TxtString2DecimalFieldParser extends AbsTxtString2XDataFieldParser<DecimalField, BigDecimal> {

	public TxtString2DecimalFieldParser(char quote, boolean trimValue) {
		super(null, quote, trimValue);
	}

	public TxtString2DecimalFieldParser(NumberFormat format, char quote, boolean trimValue) {
		super(format, quote, trimValue);
	}

	@Override
	protected BigDecimal convertString(String value) throws DataException {
		if (value == null) {
			return null;
		}
		
		value = value.trim();
		if (value.length() == 0) {
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
//		TxtString2DecimalFieldParser newObj = new TxtString2DecimalFieldParser((NumberFormat) format, quote, trimValue);
//		return newObj;
//	}
}
