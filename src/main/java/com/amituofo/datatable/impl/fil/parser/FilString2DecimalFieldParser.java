package com.amituofo.datatable.impl.fil.parser;

import java.math.BigDecimal;
import java.text.NumberFormat;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.type.DecimalField;

public class FilString2DecimalFieldParser extends AbsFilString2XDataFieldParser<DecimalField, BigDecimal> {

	public FilString2DecimalFieldParser(int columnStartIndex, int length) {
		super(columnStartIndex, length, null);
	}

	public FilString2DecimalFieldParser(int columnStartIndex, int length, NumberFormat format) {
		super(columnStartIndex, length, format);
	}

	@Override
	protected BigDecimal convertString(String value) throws DataException {
		if (value == null || value.length() == 0) {
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
//		FilString2DecimalFieldParser newObj = new FilString2DecimalFieldParser(columnStartIndex, length, (NumberFormat) format);
//		return newObj;
//	}
}
