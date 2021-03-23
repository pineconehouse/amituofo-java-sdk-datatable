package com.amituofo.datatable.impl.basic.render;


public class StringFieldLeftPadRender extends AbsStringFieldPadRender {

	public StringFieldLeftPadRender(int length, String padValue) {
		super(length, padValue);
	}

	protected String rending(String value) {
		if (padValue.length() != 0 && length > 0 && value != null && value.length() > 0) {
			String padString = "";
			int spaceCount = length - value.length();
			for (int i = 0; i < spaceCount; i++) {
				padString += padValue;
			}

			return padString + value;
		}

		return value;
	}

//	@Override
//	public DataFieldRender clone() {
//		StringFieldLeftPadRender newObj = new StringFieldLeftPadRender(length, padValue);
//		return newObj;
//	}
}
