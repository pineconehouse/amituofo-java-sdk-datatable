package com.amituofo.datatable.impl.excel;

import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;

import org.apache.poi.ss.usermodel.FormulaEvaluator;

import com.amituofo.datatable.DataField;
import com.amituofo.datatable.DataFieldRender;
import com.amituofo.datatable.DataFieldType;
import com.amituofo.datatable.impl.basic.StdDataFieldDefinition;
import com.amituofo.datatable.impl.excel.parser.AbsExcelCell2XDataFieldParser;
import com.amituofo.datatable.impl.excel.parser.ExcelCell2DateFieldParser;
import com.amituofo.datatable.impl.excel.parser.ExcelCell2DecimalFieldParser;
import com.amituofo.datatable.impl.excel.parser.ExcelCell2StringFieldParser;
import com.amituofo.datatable.impl.excel.render.ExcelDataFieldRender;
import com.amituofo.datatable.impl.excel.render.ExcelDecimalFieldRender;
import com.amituofo.datatable.type.DateFieldType;
import com.amituofo.datatable.type.DecimalFieldType;
import com.amituofo.datatable.type.StringFieldType;

public class ExcelDataFieldDefinition extends StdDataFieldDefinition {

	public ExcelDataFieldDefinition(String name, int mappingIndex, DataFieldType type, boolean trimValue, Format informat, Format outformat, FormulaEvaluator evaluator) {
		super(name, mappingIndex, type, createParser(type, evaluator, informat, trimValue), createRender(type, outformat));
	}

	public ExcelDataFieldDefinition(String name, DataFieldType type) {
		this(name, -1, type, false, null, null, null);
	}

	private static DataFieldRender createRender(DataFieldType type, Format outformat) {
		if (type instanceof DecimalFieldType) {
			return new ExcelDecimalFieldRender((NumberFormat) outformat, ',', '\"', false);
		} else {
			return new ExcelDataFieldRender(outformat, ',', '\"', false);
		}
	}

	// @Override
	// public ExcelDataFieldDefinition clone() {
	// ExcelDataFieldDefinition newObj = new ExcelDataFieldDefinition(name, null, false, null, null, null);
	// copyTo(newObj);
	// return newObj;
	// }

	private static AbsExcelCell2XDataFieldParser<?, ?> createParser(DataFieldType type, FormulaEvaluator evaluator, Format informat, boolean trimValue) {
		AbsExcelCell2XDataFieldParser<? extends DataField, ?> fieldParser = null;
		if (type instanceof StringFieldType) {
			fieldParser = new ExcelCell2StringFieldParser(evaluator, informat, trimValue);
		} else if (type instanceof DecimalFieldType) {
			fieldParser = new ExcelCell2DecimalFieldParser(evaluator, (NumberFormat) informat, trimValue);
		} else if (type instanceof DateFieldType) {
			fieldParser = new ExcelCell2DateFieldParser(evaluator, (DateFormat) informat, trimValue);
			// } else if (type instanceof ObjectFieldType) {
			// fieldParser = new ExcelStringFieldParser(quote, trimValue);
		} else {
			fieldParser = new ExcelCell2StringFieldParser(evaluator, informat, trimValue);
		}

		return fieldParser;
	}

	@Override
	public void setType(DataFieldType type) {
		AbsExcelCell2XDataFieldParser<?, ?> p = (AbsExcelCell2XDataFieldParser<?, ?>) this.parser;

		this.type = type;
		this.parser = createParser(type, p.getEvaluator(), p.getFormat(), p.isTrimValue());
	}

	public boolean isTrimValue() {
		return ((AbsExcelCell2XDataFieldParser<?, ?>) parser).isTrimValue();
	}

	public void setTrimValue(boolean trimValue) {
		((AbsExcelCell2XDataFieldParser<?, ?>) parser).setTrimValue(trimValue);
	}

	public Format getInformat() {
		return ((AbsExcelCell2XDataFieldParser<?, ?>) parser).getFormat();
	}

	public void setInformat(Format informat) {
		((AbsExcelCell2XDataFieldParser<?, ?>) parser).setFormat(informat);
	}

	public Format getOutformat() {
		return ((AbsExcelCell2XDataFieldParser<?, ?>) render).getFormat();
	}

	public void setOutformat(Format outformat) {
		((AbsExcelCell2XDataFieldParser<?, ?>) render).setFormat(outformat);
	}

	public FormulaEvaluator getEvaluator() {
		return ((AbsExcelCell2XDataFieldParser<?, ?>) parser).getEvaluator();
	}

	public void setEvaluator(FormulaEvaluator evaluator) {
		((AbsExcelCell2XDataFieldParser<?, ?>) parser).setEvaluator(evaluator);
	}

}
