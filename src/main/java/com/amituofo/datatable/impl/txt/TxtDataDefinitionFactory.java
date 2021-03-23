package com.amituofo.datatable.impl.txt;

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

public class TxtDataDefinitionFactory extends StdDefinitionStore<TxtDataTableDefinition> {
	public static final TxtDataDefinitionFactory config = new TxtDataDefinitionFactory();

	// public static final DataRecordParser PARSER_CSV_RECORD = new TxtDataRecordParser(',', '"');
	// public static final DataRecordRender Render_CSV_RECORD = new TxtDataRecordRender(',');

	// public static final DataTableDefinition TABLE_UTF8_CSV = new StdDataTableDefinition("CSV", PARSER_CSV_RECORD, Render_CSV_RECORD);

	// public static final StdDataTableDefinition UTF8_TSV_NOTITLE = new StdDataTableDefinition("TSV", "UTF-8", false, false, '\t', '"', PARSE_MODE.SOURCE_PRIORITY);
	// public static final StdDataTableDefinition UTF8_CSV_WITH_TITLE = new StdDataTableDefinition("CSV", "UTF-8", true, true, ',', '"', PARSE_MODE.SOURCE_PRIORITY);
	// public static final StdDataTableDefinition UTF8_TSV_WITH_TITLE = new StdDataTableDefinition("TSV", "UTF-8", true, true, '\t', '"', PARSE_MODE.SOURCE_PRIORITY);

	public static TxtDataTableDefinition parse(String defineFile) throws DefinitionException {
		return parse(new File(defineFile));
	}

	public static TxtDataTableDefinition parse(File defineFile) throws DefinitionException {
		Document doc = loadDocument(defineFile);
		return config.parse(doc, doc.getRootElement());
	}

	public static TxtDataTableDefinition create(String name, String charset, char delimiter, char quote, int startRow, boolean firstRowAsTitle) {
		return new TxtDataTableDefinition(name, charset, delimiter, quote, startRow, firstRowAsTitle);
	}

	// public static final StdDataTableDefinition UTF8_TSV_NOTITLE = new StdDataTableDefinition("TSV", "UTF-8", false, false, '\t', '"', PARSE_MODE.SOURCE_PRIORITY);
	// public static TxtDataTableDefinition UTF8_CSV_WITH_TITLE(String name) {
	// return new TxtDataTableDefinition(name, "UTF-8", ',', '"', 1, true);
	// }
	// public static final StdDataTableDefinition UTF8_TSV_WITH_TITLE = new StdDataTableDefinition("TSV", "UTF-8", true, true, '\t', '"', PARSE_MODE.SOURCE_PRIORITY);

//	@Override
//	protected TxtDataTableDefinition extract(Object titleRow, List<Object> contents, TxtDataTableDefinition definition) throws DataException, DefinitionException {
//		String[] titles = null;
//		int[] dataFieldTypes = null;
//		final int DECIMAL_FIELD_TYPE = 1;
//		final int STRING_FIELD_TYPE = 2;
//
//		if (titleRow != null) {
//			titles = TxtDataRecordParser.split((String) titleRow, definition.getDelimiter(), definition.getQuote(), false, true);
//		}
//
//		if (contents != null && contents.size() > 0) {
//			String[] lastRowVals = null;
//			String[] currentRowVals = TxtDataRecordParser.split((String) contents.get(0), definition.getDelimiter(), definition.getQuote(), false, true);
//
//			dataFieldTypes = new int[titles == null ? currentRowVals.length : titles.length];
//			// int[] finalType = new int[dataFieldTypes.length];
//			// for (int i = 0; i < finalType.length; i++) {
//			// finalType[i] = 0;
//			// }
//
//			for (int i = 1; i < contents.size(); i++) {
//				Object objrow = contents.get(i);
//				currentRowVals = TxtDataRecordParser.split((String) objrow, definition.getDelimiter(), definition.getQuote(), false, true);
//
//				int max = dataFieldTypes.length < currentRowVals.length ? dataFieldTypes.length : currentRowVals.length;
//				for (int j = 0; j < max; j++) {
//					if (currentRowVals[j] != null && currentRowVals[j].length() != 0) {
//						if (dataFieldTypes[j] != STRING_FIELD_TYPE) {
//							if (lastRowVals != null && lastRowVals[j].length() != currentRowVals[j].length()) {
//								dataFieldTypes[j] = STRING_FIELD_TYPE;
//								continue;
//							}
//
//							try {
//								Double.parseDouble(currentRowVals[j]);
//								dataFieldTypes[j] = DECIMAL_FIELD_TYPE;
//								continue;
//							} catch (NumberFormatException e) {
//								dataFieldTypes[j] = STRING_FIELD_TYPE;
//								continue;
//							}
//						}
//					}
//				}
//
//				lastRowVals = currentRowVals;
//			}
//		}
//
//		if (titles != null) {
//			int endIndex = titles.length;
//			for (int i = titles.length - 1; i >= 0; i--) {
//				if (titles[i].trim().length() == 0) {
//					endIndex--;
//				}
//			}
//
//			definition.removeAll();
//			for (int i = 0; i < endIndex; i++) {
//				DataFieldType type = new StringFieldType();
//
//				try {
//					definition.addColumn(new TxtDataFieldDefinition(titles[i], i, type, definition.getDelimiter(), definition.getQuote(), false, false, null, null));
//				} catch (DefinitionException e) {
//					e.printStackTrace();
//				}
//			}
//
//		}
//
//		return definition;
//	}

	@Override
	protected TxtDataTableDefinition parse(Document doc, Element root) throws DefinitionException {
		Attribute attrib = null;
		String value = null;

		Element recordDef = getTableDefinitionElements(doc);

		String name = "";
		char delimiter = ',';
		char smartQuote = ' ';
		boolean forceQuote = false;
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

		value = recordDef.getAttributeValue("delimiter");
		if (value != null && !"".equals(value)) {
			delimiter = value.charAt(0);
		} else {
			throw new DefinitionException("Property delimiter is empty!");
		}

		value = recordDef.getAttributeValue("forceQuote");
		if (value != null && !"".equals(value)) {
			forceQuote = Boolean.valueOf(value);
		} else {
			forceQuote = false;
		}

		value = recordDef.getAttributeValue("smartQuote");
		if (value != null && !"".equals(value)) {
			smartQuote = value.charAt(0);
		} else {
			smartQuote = ' ';
		}

		// value = recordDef.getAttributeValue("parseMode");
		// if (value != null && !"".equals(value)) {
		// parseMode = PARSE_MODE.valueOf(value.toUpperCase());
		// } else {
		// // throw new DefinitionException("Property parseMode is empty!");
		// }

		TxtDataTableDefinition dataRecDef = new TxtDataTableDefinition(name, charset, delimiter, smartQuote, startRow, firstRowAsTitle);

		int index = 0;
		@SuppressWarnings("unchecked")
		List<Element> es = recordDef.getChildren();
		for (Element e : es) {
			DataFieldType fieldType = null;
			// DataFieldParser fieldParser = null;
			// DataFieldRender fieldRender = null;

			int mappingIndex = -1;
			boolean allowEmpty = true;
			// boolean wipeSmartQuote = true;
			boolean trimValue = false;
			String defaultValue = null;
			String outformatstr = null;
			String informatstr = null;

			if (e.getName().toLowerCase().contains("column")) {
				index++;

				name = e.getAttributeValue("name");
				if (name == null || name.length() == 0) {
					// if (parseMode == PARSE_MODE.SOURCE_PRIORITY) {
					// throw new DefinitionException("Column name must be define when parseMode is " + PARSE_MODE.SOURCE_PRIORITY.name());
					// }

					name = "Column" + index;
				}

				try {
					mappingIndex = e.getAttribute("mappingIndex").getIntValue();
				} catch (Throwable e1) {
					mappingIndex = index - 1;
				}

				try {
					allowEmpty = e.getAttribute("allowEmpty").getBooleanValue();
				} catch (DataConversionException e1) {
					throw new DefinitionException(e1);
				}

				try {
					trimValue = e.getAttribute("trimValue").getBooleanValue();
				} catch (Throwable e1) {
					trimValue = false;
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
				//
				// try {
				// Attribute a_wipeSmartQuote = e.getAttribute("wipeSmartQuote");
				// if (a_wipeSmartQuote != null && a_wipeSmartQuote.getBooleanValue() == false) {
				// fieldDef.setQuote(' ');
				// } else {
				// fieldDef.setQuote(dataRecDef.getSmartQuote());
				// }
				// } catch (Throwable e1) {
				// fieldDef.setQuote(dataRecDef.getSmartQuote());
				// }

				// Format informat = null;
				// Format outformat = null;
				// String valueTypeName = e.getAttribute("type").getValue();// .toLowerCase();
				// if (valueTypeName.startsWith("string")) {
				// int[] maxmin = tractMaxMin(valueTypeName);
				// if (maxmin.length >= 1) {
				// fieldType = new StringFieldType(maxmin[0], allowEmpty, defaultValue);
				// } else {
				// fieldType = new StringFieldType(allowEmpty, defaultValue);
				// }
				// } else if (valueTypeName.startsWith("decimal")) {
				// if (informatstr != null) {
				// informat = new DecimalFormat(informatstr);
				// }
				// if (outformatstr != null) {
				// outformat = new DecimalFormat(outformatstr);
				// }
				//
				// BigDecimal dv = (defaultValue == null ? null : new BigDecimal(defaultValue));
				// int[] maxmin = tractMaxMin(valueTypeName);
				// if (maxmin.length >= 2) {
				// fieldType = new DecimalFieldType(maxmin[0], maxmin[1], allowEmpty, dv);
				// } else {
				// fieldType = new DecimalFieldType(allowEmpty, dv);
				// }
				// } else if (valueTypeName.startsWith("datetime")) {
				// if (informatstr != null) {
				// informat = new SimpleDateFormat(informatstr);
				// }
				// outformat = informat;
				// if (outformatstr != null) {
				// outformat = new SimpleDateFormat(outformatstr);
				// }
				// try {
				// Date dv = defaultValue == null ? null : ((DateFormat) informat).parse(defaultValue);
				// fieldType = new DateFieldType(allowEmpty, dv);
				// } catch (ParseException e1) {
				// throw new DefinitionException("Date format invalid! " + defaultValue);
				// }
				// // } else if (valueTypeName.startsWith("object")) {
				// // fieldType = new ObjectFieldType();
				// // fieldParser = new xxxxxx(smartQuote, trimValue);
				// } else {
				// fieldType = new StringFieldType(allowEmpty, defaultValue);
				// }

				String valueTypeName = e.getAttribute("type").getValue();// .toLowerCase();
				fieldType = createFieldType(valueTypeName, allowEmpty, defaultValue, informatstr, outformatstr);
				Format[] fmts = createInOutFormat(fieldType, informatstr, outformatstr);

				StdDataFieldDefinition fieldDef = new TxtDataFieldDefinition(name, mappingIndex, fieldType, delimiter, smartQuote, forceQuote, trimValue, fmts[0], fmts[1]);
				dataRecDef.addColumn(fieldDef);
			}
		}
		
		dataRecDef.setDefinitionFromXml(true);

		return dataRecDef;
	}
}
