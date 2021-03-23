package com.amituofo.datatable.type;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.amituofo.datatable.DataField;
import com.amituofo.datatable.DataFieldType;
import com.amituofo.datatable.InvalidDataException;
import com.amituofo.datatable.impl.basic.AbsDataFieldType;

public class DateFieldType extends AbsDataFieldType {
	public final static DateFormat YYYYMMDD_SPLIT_BY_MINUS = new SimpleDateFormat("yyyy-MM-dd");
	public final static DateFormat YYYYMMDD_SPLIT_BY_VIRGULE = new SimpleDateFormat("yyyy/MM/dd");
	public final static DateFormat MMDDYYYY_SPLIT_BY_MINUS = new SimpleDateFormat("MM-dd-yyyy");
	public final static DateFormat MMDDYYYY_SPLIT_BY_VIRGULE = new SimpleDateFormat("MM/dd/yyyy");
	public final static DateFormat YYYYMMDDHHMMSSS_SPLIT_BY_MINUS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public final static DateFormat YYYYMMDDHHMMSSS_SPLIT_BY_VIRGULE = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	public final static DateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");
	public final static DateFormat YYYYMM = new SimpleDateFormat("yyyyMM");

	public DateFieldType() {
		super("datetime", true, null);
	}

	public DateFieldType(boolean allowEmpty, Date defaultValue) {
		super("datetime", 0, 0, allowEmpty, defaultValue);
	}

	public DateFieldType(Object defaultValue) {
		super("datetime", true, defaultValue);
	}
	
	public DateFieldType(int length, boolean allowEmpty, Date defaultValue) {
		super("datetime", length, 0, allowEmpty, defaultValue);
	}

	public void validate(DataField field) throws InvalidDataException {
		if (field == null) {
			return;
		}

		Object value = field.getValue();

		if (value != null && !(value instanceof Date)) {
			throw new InvalidDataException("Only Date value is allowed.");
		}

		if (!allowEmpty && value == null) {
			throw new InvalidDataException("Empty value is not allowed.");
		}
	}

	public DataField newField() {
		return new DateField(defaultValue);
	}

	public DataFieldType clone() {
		DateFieldType newObj = new DateFieldType(integerLength, allowEmpty, (Date) defaultValue);
		copyProperies(newObj);
		return newObj;
	}

	@Override
	public int getTotalLength() {
		return integerLength;
	}

}
