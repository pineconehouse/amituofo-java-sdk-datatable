package com.amituofo.datatable.impl.fil.render;

import java.text.Format;
import java.util.HashMap;
import java.util.Map;

import com.amituofo.datatable.DataField;
import com.amituofo.datatable.impl.basic.AbsDataFieldRender;

public class FilDataFieldRender extends AbsDataFieldRender {
	private String fillChar = " ";
	private int length = 0;
	private final Map<Integer, String> fills = new HashMap<Integer, String>();

	public FilDataFieldRender(int length) {
		super(null);
	}

	public FilDataFieldRender(int length, Format format) {
		super(format);
		this.length = length;
	}

	public FilDataFieldRender(int length, char fillChar, Format format) {
		super(format);
		this.length = length;
		this.fillChar = String.valueOf(fillChar);
	}

	@Override
	public String rending(DataField value) {
		if (value != null && value.getValue() != null) {
			String tmpValue = null;
			if (format != null) {
				tmpValue = format.format(value.getValue());
			} else {
				tmpValue = value.getValue().toString();
			}

			int spaceCount = length - tmpValue.length();

			if (spaceCount > 0) {
				return tmpValue + getFillString(spaceCount);
			} else {
				return tmpValue.substring(0, length);
			}
		} else {
			// DataFieldType tp = value.getDefinition().getType();
			// return getFillString(tp.getTotalLength());
			return getFillString(length);
		}
	}

	public String getFillString(int spaceCount) {
		String padString = fills.get(spaceCount);
		if (padString == null) {
			padString = "";
			for (int i = 0; i < spaceCount; i++) {
				padString += fillChar;
			}
			fills.put(spaceCount, padString);
		}

		return padString;
	}

	// @Override
	// public DataFieldRender clone() {
	// FilDataFieldRender newObj = new FilDataFieldRender(length, format);
	// newObj.fills.putAll(this.fills);
	// return newObj;
	// }

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

}
