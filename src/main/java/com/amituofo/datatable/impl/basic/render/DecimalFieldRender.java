package com.amituofo.datatable.impl.basic.render;

import java.math.BigDecimal;
import java.text.NumberFormat;

import com.amituofo.datatable.DataField;
import com.amituofo.datatable.impl.basic.AbsDataFieldRender;
import com.amituofo.datatable.type.DecimalField;

public class DecimalFieldRender extends AbsDataFieldRender {

	public DecimalFieldRender(NumberFormat format) {
		super(format);
	}

	public String rending(DataField value) {
		BigDecimal v = ((DecimalField) value).getValue();
		if (v != null) {
			if (format == null) {
				return v.toPlainString();
			} else {
				return ((NumberFormat) format).format(v.doubleValue());
			}
		} else {
			return "";
		}
	}

//	@Override
//	public DataFieldRender clone() {
//		DecimalFieldRender newObj = new DecimalFieldRender((NumberFormat) format);
//		return newObj;
//	}
}
