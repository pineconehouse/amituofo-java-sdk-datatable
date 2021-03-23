package com.amituofo.datatable.impl.fil;

import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;

import com.amituofo.datatable.DataField;
import com.amituofo.datatable.DataFieldType;
import com.amituofo.datatable.impl.basic.StdDataFieldDefinition;
import com.amituofo.datatable.impl.fil.parser.AbsFilString2XDataFieldParser;
import com.amituofo.datatable.impl.fil.parser.FilString2DateFieldParser;
import com.amituofo.datatable.impl.fil.parser.FilString2DecimalFieldParser;
import com.amituofo.datatable.impl.fil.parser.FilString2StringFieldParser;
import com.amituofo.datatable.impl.fil.render.FilDataFieldRender;
import com.amituofo.datatable.type.DateFieldType;
import com.amituofo.datatable.type.DecimalFieldType;
import com.amituofo.datatable.type.StringFieldType;

public class FilDataFieldDefinition extends StdDataFieldDefinition {
	
	public FilDataFieldDefinition(String name, DataFieldType type, int columnStartIndex, int length, Format informat, Format outformat) {
		super(name, 0, type, createParser(type, columnStartIndex, length, informat), new FilDataFieldRender(length, outformat));
	}

	public FilDataFieldDefinition(String name, DataFieldType type, int columnStartIndex, int length, char fillChar, Format informat, Format outformat) {
		super(name, 0, type, createParser(type, columnStartIndex, length, informat), new FilDataFieldRender(length, fillChar, outformat));
	}

	// @Override
	// public FilDataFieldDefinition clone() {
	// FilDataFieldDefinition newObj = new FilDataFieldDefinition(name, null, 0, 0, null, null);
	// copyTo(newObj);
	// return newObj;
	// }

	private static AbsFilString2XDataFieldParser<?, ?> createParser(DataFieldType type, int columnStartIndex, int length, Format informat) {
		AbsFilString2XDataFieldParser<? extends DataField, ?> fieldParser = null;
		if (type instanceof StringFieldType) {
			fieldParser = new FilString2StringFieldParser(columnStartIndex, length);
		} else if (type instanceof DecimalFieldType) {
			fieldParser = new FilString2DecimalFieldParser(columnStartIndex, length, (NumberFormat) informat);
		} else if (type instanceof DateFieldType) {
			fieldParser = new FilString2DateFieldParser(columnStartIndex, length, (DateFormat) informat);
			// } else if (type instanceof ObjectFieldType) {
			// fieldParser = new FilStringFieldParser( trimValue);
		} else {
			fieldParser = new FilString2StringFieldParser(columnStartIndex, length);
		}

		return fieldParser;
	}

	@Override
	public void setType(DataFieldType type) {
		AbsFilString2XDataFieldParser<?,?> p = (AbsFilString2XDataFieldParser<?,?>)this.parser;
		
		this.type = type;
		this.parser = createParser(type, p.getColumnStartIndex(), p.getLength(), p.getFormat());
	}
	
	public int getColumnStartIndex() {
		return ((AbsFilString2XDataFieldParser<?,?>) parser).getColumnStartIndex();
	}

	public void setColumnStartIndex(int columnStartIndex) {
		((AbsFilString2XDataFieldParser<?,?>) parser).setColumnStartIndex(columnStartIndex);
	}

	public int getLength() {
		return ((AbsFilString2XDataFieldParser<?,?>) parser).getLength();
	}

	public void setLength(int length) {
		((AbsFilString2XDataFieldParser<?,?>) parser).setLength(length);
		((FilDataFieldRender) render).setLength(length);
	}

	public Format getOutformat() {
		return ((FilDataFieldRender) render).getFormat();
	}

	public void setOutformat(Format outformat) {
		((FilDataFieldRender) render).setFormat(outformat);
	}

	public Format getInformat() {
		return ((AbsFilString2XDataFieldParser<?,?>) parser).getFormat();
	}

	public void setInformat(Format informat) {
		((AbsFilString2XDataFieldParser<?,?>) parser).setFormat(informat);
	}

}
