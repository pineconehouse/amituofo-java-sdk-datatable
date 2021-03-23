package com.amituofo.datatable.impl.basic.render;


public class StringFieldRightPadRender extends AbsStringFieldPadRender {

	public StringFieldRightPadRender(int length, String padValue) {
		super(length, padValue);
	}

	protected String rending(String value) {
		if (padValue.length() != 0 && length > 0 && value != null && value.length() > 0) {
			String padString = "";
			int spaceCount = length - value.length();
			for (int i = 0; i < spaceCount; i++) {
				padString += padValue;
			}

			return value + padString;
		}

		return (String) value.toString();
	}

//	@Override
//	public DataFieldRender clone() {
//		StringFieldRightPadRender newObj = new StringFieldRightPadRender(length, padValue);
//		return newObj;
//	}
}
