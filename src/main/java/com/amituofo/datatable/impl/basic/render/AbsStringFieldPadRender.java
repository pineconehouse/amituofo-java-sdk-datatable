package com.amituofo.datatable.impl.basic.render;

import com.amituofo.datatable.DataField;
import com.amituofo.datatable.impl.basic.AbsDataFieldRender;
import com.amituofo.datatable.type.StringField;

public abstract class AbsStringFieldPadRender extends AbsDataFieldRender {
	protected int length = 0;
	protected String padValue = "";

	public AbsStringFieldPadRender(int length, String padValue) {
		super(null);
		this.length = length;
		this.padValue = padValue;
	}

	protected abstract String rending(String value);

	public String rending(DataField value) {
		String v = ((StringField) value).getValue();
		if (v != null) {
			return rending(v);
		} else {
			return "";
		}
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getPadValue() {
		return padValue;
	}

	public void setPadValue(String padValue) {
		this.padValue = padValue;
	}

}
