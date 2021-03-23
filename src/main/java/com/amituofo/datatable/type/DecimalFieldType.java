package com.amituofo.datatable.type;

import java.math.BigDecimal;

import com.amituofo.datatable.DataField;
import com.amituofo.datatable.InvalidDataException;
import com.amituofo.datatable.impl.basic.AbsDataFieldType;

public class DecimalFieldType extends AbsDataFieldType {
	private BigDecimal MAX = null;

	public DecimalFieldType() {
		super("decimal", true, null);
	}

	public DecimalFieldType(boolean allowEmpty, BigDecimal defaultValue) {
		super("decimal", allowEmpty, defaultValue);
	}

	public DecimalFieldType(Object defaultValue) {
		super("decimal", true, defaultValue);
	}

	public DecimalFieldType(int integerLength, int decimalLength, boolean allowEmpty, BigDecimal defaultValue) {
		super("decimal", integerLength, decimalLength, allowEmpty, defaultValue);
		double maxInt = 0, maxDec = 0;
		for (int i = 0; i < integerLength; i++) {
			maxInt *= 10;
			maxInt += 9;
		}
		for (int i = 0; i < decimalLength; i++) {
			maxDec /= 10;
			maxDec += 0.9;
		}

		MAX = new BigDecimal(maxInt + maxDec);
	}

	public void validate(DataField field) throws InvalidDataException {
		if (field == null) {
			return;
		}

		Object value = field.getValue();

		if (value != null && !(value instanceof BigDecimal)) {
			throw new InvalidDataException("Only BigDecimal value is allowed.");
		}

		if (!allowEmpty && value == null) {
			throw new InvalidDataException("Empty value is not allowed.");
		}

		if (MAX != null && ((BigDecimal) value).compareTo(MAX) > 0) {
			throw new InvalidDataException("Value illegal. The value should be <= " + MAX.toPlainString());
		}
	}

	public DataField newField() {
		return new DecimalField(defaultValue);
	}

	public DecimalFieldType clone() {
		DecimalFieldType newObj = new DecimalFieldType();
		copyProperies(newObj);
		return newObj;
	}

	public int getTotalLength() {
		if (decimalLength > 0) {
			return integerLength + decimalLength + 1;
		} else {
			return integerLength;
		}
	}

}
