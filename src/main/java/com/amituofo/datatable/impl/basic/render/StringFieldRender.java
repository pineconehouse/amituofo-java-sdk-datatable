package com.amituofo.datatable.impl.basic.render;

import com.amituofo.datatable.DataField;
import com.amituofo.datatable.impl.basic.AbsDataFieldRender;
import com.amituofo.datatable.type.StringField;

public class StringFieldRender extends AbsDataFieldRender {

	public StringFieldRender() {
		super(null);
	}

	public String rending(DataField value) {
		String v = ((StringField) value).getValue();
		if (v != null) {
			return v;
		} else {
			return "";
		}
	}

//	@Override
//	public DataFieldRender clone() {
//		StringFieldRender newObj = new StringFieldRender();
//		return newObj;
//	}
}
