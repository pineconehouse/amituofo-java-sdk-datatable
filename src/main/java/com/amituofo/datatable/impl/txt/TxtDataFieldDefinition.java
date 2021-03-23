package com.amituofo.datatable.impl.txt;

import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;

import com.amituofo.datatable.DataField;
import com.amituofo.datatable.DataFieldType;
import com.amituofo.datatable.impl.basic.StdDataFieldDefinition;
import com.amituofo.datatable.impl.txt.parser.AbsTxtString2XDataFieldParser;
import com.amituofo.datatable.impl.txt.parser.TxtString2DateFieldParser;
import com.amituofo.datatable.impl.txt.parser.TxtString2DecimalFieldParser;
import com.amituofo.datatable.impl.txt.parser.TxtString2StringFieldParser;
import com.amituofo.datatable.impl.txt.render.TxtDataFieldRender;
import com.amituofo.datatable.type.DateFieldType;
import com.amituofo.datatable.type.DecimalFieldType;
import com.amituofo.datatable.type.StringFieldType;

public class TxtDataFieldDefinition extends StdDataFieldDefinition {

	public TxtDataFieldDefinition(String name, int mappingIndex, DataFieldType type, char delimiter, char quote, boolean forceQuote, boolean trimValue, Format informat, Format outformat) {
		super(name, mappingIndex, type, createParser(type, quote, trimValue, informat), new TxtDataFieldRender(outformat, delimiter, quote, forceQuote));
	}
	
	public TxtDataFieldDefinition(String name, DataFieldType type) {
		this(name, -1, type, ',', '"', false, false, null, null);
	}

//	@Override
//	public TxtDataFieldDefinition clone() {
//		TxtDataFieldDefinition newObj = new TxtDataFieldDefinition(name, null, ' ', ' ', false, false, null, null);
//		copyTo(newObj);
//		return newObj;
//	}

	private static AbsTxtString2XDataFieldParser<?, ?> createParser(DataFieldType type, char quote, boolean trimValue, Format informat) {
		AbsTxtString2XDataFieldParser<? extends DataField, ?> fieldParser = null;
		if (type instanceof StringFieldType) {
			fieldParser = new TxtString2StringFieldParser(quote, trimValue);
		} else if (type instanceof DecimalFieldType) {
			fieldParser = new TxtString2DecimalFieldParser((NumberFormat) informat, quote, trimValue);
		} else if (type instanceof DateFieldType) {
			fieldParser = new TxtString2DateFieldParser((DateFormat) informat, quote, trimValue);
			// } else if (type instanceof ObjectFieldType) {
			// fieldParser = new TxtStringFieldParser(quote, trimValue);
		} else {
			fieldParser = new TxtString2StringFieldParser(quote, trimValue);
		}

		return fieldParser;
	}

	@Override
	public void setType(DataFieldType type) {
		AbsTxtString2XDataFieldParser<?,?> p = (AbsTxtString2XDataFieldParser<?,?>)this.parser;
		
		this.type = type;
		this.parser = createParser(type, p.getQuote(), p.isTrimValue(), p.getFormat());
	}

	public char getDelimiter() {
		return ((TxtDataFieldRender) render).getDelimiter();
	}

	public void setDelimiter(char delimiter) {
		((TxtDataFieldRender) render).setDelimiter(delimiter);
	}

	public char getQuote() {
		return ((TxtDataFieldRender) render).getQuote();
	}

	public void setQuote(char quote) {
		((TxtDataFieldRender) render).setQuote(quote);
		((AbsTxtString2XDataFieldParser<?,?>) parser).setQuote(quote);
	}

	public boolean isForceQuote() {
		return ((TxtDataFieldRender) render).isForceQuote();
	}

	public void setForceQuote(boolean forceQuote) {
		((TxtDataFieldRender) render).setForceQuote(forceQuote);
	}

	public boolean isTrimValue() {
		return ((AbsTxtString2XDataFieldParser<?,?>) parser).isTrimValue();
	}

	public void setTrimValue(boolean trimValue) {
		((AbsTxtString2XDataFieldParser<?,?>) parser).setTrimValue(trimValue);
	}

	public Format getOutformat() {
		return ((TxtDataFieldRender) render).getFormat();
	}

	public void setOutformat(Format outformat) {
		((TxtDataFieldRender) render).setFormat(outformat);
	}

	public Format getInformat() {
		return ((AbsTxtString2XDataFieldParser<?,?>) parser).getFormat();
	}

	public void setInformat(Format informat) {
		((AbsTxtString2XDataFieldParser<?,?>) parser).setFormat(informat);
	}

}
