package com.amituofo.datatable.impl.basic.render;

import com.amituofo.datatable.DataField;
import com.amituofo.datatable.impl.basic.AbsDataFieldRender;

public class ObjectFieldRender extends AbsDataFieldRender {

	public ObjectFieldRender() {
		super(null);
	}

	public String rending(DataField value) {
		Object o = value.getValue();
		if (o != null) {
			return o.toString();
		} else {
			return null;
		}
	}

//	@Override
//	public DataFieldRender clone() {
//		ObjectFieldRender newObj = new ObjectFieldRender();
//		return newObj;
//	}
}
