package com.amituofo.datatable.impl.basic.render;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.amituofo.datatable.DataField;
import com.amituofo.datatable.impl.basic.AbsDataFieldRender;
import com.amituofo.datatable.type.DateField;

public class DateFieldRender extends AbsDataFieldRender {
	public DateFieldRender(String pattern) {
		super(new SimpleDateFormat(pattern));
	}

	public DateFieldRender(String pattern, DateFormatSymbols formatSymbols) {
		super(new SimpleDateFormat(pattern, formatSymbols));
	}

	public DateFieldRender(String pattern, Locale locale) {
		super(new SimpleDateFormat(pattern, locale));
	}

	public DateFieldRender(DateFormat dateFormat) {
		super(dateFormat);
	}

	public String rending(DataField value) {
		Date v = ((DateField) value).getValue();
		if (v != null) {
			return format.format(v);
		} else {
			return "";
		}
	}

//	@Override
//	public DataFieldRender clone() {
//		DateFieldRender newObj = new DateFieldRender((DateFormat) format);
//		return newObj;
//	}
}
