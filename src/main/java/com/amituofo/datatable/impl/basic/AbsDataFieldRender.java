package com.amituofo.datatable.impl.basic;

import java.text.Format;

import com.amituofo.datatable.DataFieldRender;

public abstract class AbsDataFieldRender implements DataFieldRender {
	protected Format format = null;

	public AbsDataFieldRender(Format format) {
		this.format = format;
	}

//	public abstract DataFieldRender clone();
	
	@Override
	public Format getFormat() {
		return format;
	}

	@Override
	public void setFormat(Format format) {
		this.format = format;
	}

}
