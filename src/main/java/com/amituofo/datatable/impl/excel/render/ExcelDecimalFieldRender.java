package com.amituofo.datatable.impl.excel.render;

import java.text.NumberFormat;

import com.amituofo.datatable.DataField;
import com.amituofo.datatable.type.DecimalField;

public class ExcelDecimalFieldRender extends ExcelDataFieldRender {
	public ExcelDecimalFieldRender(NumberFormat format, char delimiter, char quote, boolean forceQuote) {
		super(format, delimiter, quote, forceQuote);
	}

	@Override
	public String rending(DataField value) {
		DecimalField dv = (DecimalField) value;
		if (dv != null && dv.getValue() != null) {
			String tmpValue = null;
			if (format != null) {
				tmpValue = ((NumberFormat) format).format(dv.getValue());
			} else {
				tmpValue = dv.getValue().toPlainString();
			}

			if (quote != ' ') {
				if (forceQuote) {
					tmpValue = quote + tmpValue + quote;
				} else if (tmpValue.indexOf(delimiter) != -1) {
					tmpValue = quote + tmpValue + quote;
				}
			}

			return tmpValue;
		} else {
			return "";
		}
	}
}
