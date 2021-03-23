package com.amituofo.datatable.impl.excel.render;

import java.text.Format;

import com.amituofo.datatable.impl.txt.render.TxtDataFieldRender;

public class ExcelDataFieldRender extends TxtDataFieldRender {
	public ExcelDataFieldRender(Format format, char delimiter, char quote, boolean forceQuote) {
		super(format, delimiter, quote, forceQuote);
	}
}
