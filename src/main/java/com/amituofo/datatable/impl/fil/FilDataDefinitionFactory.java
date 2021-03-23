package com.amituofo.datatable.impl.fil;

import java.io.File;
import java.text.Format;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;

import com.amituofo.datatable.DataFieldType;
import com.amituofo.datatable.DefinitionException;
import com.amituofo.datatable.impl.basic.StdDataFieldDefinition;
import com.amituofo.datatable.impl.basic.StdDefinitionStore;
import com.amituofo.datatable.type.StringFieldType;

public class FilDataDefinitionFactory extends StdDefinitionStore<FilDataTableDefinition> {
	public static final FilDataDefinitionFactory config = new FilDataDefinitionFactory();

	public static FilDataTableDefinition parse(String defineFile) throws DefinitionException {
		return parse(new File(defineFile));
	}

	public static FilDataTableDefinition parse(File defineFile) throws DefinitionException {
		Document doc = loadDocument(defineFile);
		return config.parse(doc, doc.getRootElement());
	}

	public static FilDataTableDefinition create(String name, String charset, int startRow, boolean firstRowAsTitle, int[] columnsLength) {
		FilDataTableDefinition tabledef = new FilDataTableDefinition(name, charset, startRow, firstRowAsTitle);
		if (columnsLength != null) {
			int startIndex = 0, lastLength = 0;
			for (int i = 0; i < columnsLength.length; i++) {
				startIndex += lastLength;

				FilDataFieldDefinition fieldDefinition = new FilDataFieldDefinition(null, new StringFieldType(), startIndex, columnsLength[i], null, null);
				try {
					tabledef.addColumn(fieldDefinition);
				} catch (DefinitionException e) {
					e.printStackTrace();
				}

				lastLength = columnsLength[i];
			}
		}
		return tabledef;
	}

//	@Override
//	protected TxtDataTableDefinition extract(Object title, List<Object> contents, FilDataTableDefinition definition) throws DefinitionException, DataException {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	protected FilDataTableDefinition parse(Document doc, Element root) throws DefinitionException {
		Attribute attrib = null;
		String value = null;

		Element recordDef = getTableDefinitionElements(doc);

		String name = "";
//		int mappingIndex = -1;
		// char fillChar = ' ';
		String charset = "UTF-8";
		int startRow = 1;
		boolean firstRowAsTitle = false;

		value = recordDef.getAttributeValue("name");
		if (value != null && !"".equals(value)) {
			name = value;
		} else {
			throw new DefinitionException("Table name is empty!");
		}

		value = recordDef.getAttributeValue("charset");
		if (value != null && !"".equals(value)) {
			charset = value;
		} else {
			// throw new DefinitionException("Charset is empty!");
		}

		value = recordDef.getAttributeValue("startRow");
		if (value != null && !"".equals(value)) {
			startRow = Integer.valueOf(value);
		} else {
			startRow = 1;
		}

		attrib = recordDef.getAttribute("firstRowAsTitle");
		if (attrib != null) {
			try {
				firstRowAsTitle = attrib.getBooleanValue();
			} catch (DataConversionException e1) {
				throw new DefinitionException("Property firstRowAsTitle is invalid!");
			}
		} else {
			throw new DefinitionException("Property firstRowAsTitle is invalid!");
		}

		// value = recordDef.getAttributeValue("fillChar");
		// if (value != null && !"".equals(value)) {
		// fillChar = value.charAt(0);
		// } else {
		// }

		FilDataTableDefinition dataRecDef = new FilDataTableDefinition(name, charset, startRow, firstRowAsTitle);

		int startIndex = 0, lastLength = 0;
		int index = 0;
		@SuppressWarnings("unchecked")
		List<Element> es = recordDef.getChildren();
		for (Element e : es) {
			DataFieldType fieldType = null;

			boolean allowEmpty = true;
			String defaultValue = null;
			String outformatstr = null;
			String informatstr = null;
			// int length = 0;

			if (e.getName().toLowerCase().contains("column")) {
				index++;

				name = e.getAttributeValue("name");
				if (name == null || name.length() == 0) {
					name = "Column" + index;
				}
				
//				try {
//					mappingIndex = e.getAttribute("mappingIndex").getIntValue();
//				} catch (Throwable e1) {
//					mappingIndex = index - 1;
//				}

				// try {
				// attrib = e.getAttribute("length");
				// if (attrib == null) {
				// length = -1;
				// } else {
				// length = attrib.getIntValue();
				// }
				// } catch (DataConversionException e1) {
				// throw new DefinitionException(e1);
				// }

				try {
					allowEmpty = e.getAttribute("allowEmpty").getBooleanValue();
				} catch (DataConversionException e1) {
					throw new DefinitionException(e1);
				}

				try {
					outformatstr = e.getAttribute("outFormat").getValue();
				} catch (Throwable e1) {
					outformatstr = null;
				}

				try {
					informatstr = e.getAttribute("inFormat").getValue();
				} catch (Throwable e1) {
					informatstr = null;
				}

				try {
					defaultValue = e.getAttribute("defaultValue").getValue();
				} catch (Throwable e1) {
					defaultValue = null;
				}

				String valueTypeName = e.getAttribute("type").getValue();// .toLowerCase();
				fieldType = createFieldType(valueTypeName, allowEmpty, defaultValue, informatstr, outformatstr);
				Format[] fmts = createInOutFormat(fieldType, informatstr, outformatstr);

				// if (length < 0) {
				// length = fieldType.getTotalLength();
				// }

				startIndex += lastLength;

				// StdDataFieldDefinition fieldDef = new FilDataFieldDefinition(name, fieldType, startIndex, fieldType.getTotalLength(), fillChar, fmts[0], fmts[1]);
				StdDataFieldDefinition fieldDef = new FilDataFieldDefinition(name, fieldType, startIndex, fieldType.getTotalLength(), fmts[0], fmts[1]);
				dataRecDef.addColumn(fieldDef);

				lastLength = fieldType.getTotalLength();
			}
		}

		dataRecDef.setDefinitionFromXml(true);
		
		return dataRecDef;
	}

}
