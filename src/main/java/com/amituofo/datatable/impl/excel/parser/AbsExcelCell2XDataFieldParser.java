package com.amituofo.datatable.impl.excel.parser;

import java.text.Format;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

import com.amituofo.datatable.DataField;
import com.amituofo.datatable.impl.basic.AbsDataFieldParser;

public abstract class AbsExcelCell2XDataFieldParser<T extends DataField, TO_TYPE> extends AbsDataFieldParser<T, Cell, TO_TYPE> {
	protected FormulaEvaluator evaluator = null;
	protected boolean trimValue = false;

	public AbsExcelCell2XDataFieldParser(FormulaEvaluator evaluator, Format format, boolean trimValue) {
		super(format);
		this.evaluator = evaluator;
		this.trimValue = trimValue;
	}

	public FormulaEvaluator getEvaluator() {
		return evaluator;
	}

	public void setEvaluator(FormulaEvaluator evaluator) {
		this.evaluator = evaluator;
	}

	public boolean isTrimValue() {
		return trimValue;
	}

	public void setTrimValue(boolean trimValue) {
		this.trimValue = trimValue;
	}

}
