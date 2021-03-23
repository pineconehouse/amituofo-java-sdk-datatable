package com.amituofo.datatable;

import java.text.Format;

public interface DataFieldRender {
	// public DataFieldRender clone();

	public String rending(DataField field);
	
//	public String rendingDefault();

	public Format getFormat();

	void setFormat(Format format);

	// public String rending(String value);

	// public String rending(Object value);
}
