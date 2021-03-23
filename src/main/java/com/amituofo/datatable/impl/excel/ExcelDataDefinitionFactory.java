package com.amituofo.datatable.impl.excel;

import java.io.File;
import java.text.Format;
import java.util.List;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;

import com.amituofo.datatable.DataFieldType;
import com.amituofo.datatable.DataTableDefinition;
import com.amituofo.datatable.DefinitionException;
import com.amituofo.datatable.impl.basic.StdDataFieldDefinition;
import com.amituofo.datatable.impl.basic.StdDefinitionStore;

public class ExcelDataDefinitionFactory extends StdDefinitionStore<ExcelDataTableDefinition> {
	public static final ExcelDataDefinitionFactory config = new ExcelDataDefinitionFactory();

	public static ExcelDataTableDefinition parse(String defineFile) throws DefinitionException {
		return parse(new File(defineFile));
	}

	public static ExcelDataTableDefinition parse(File defineFile) throws DefinitionException {
		Document doc = loadDocument(defineFile);
		return config.parse(doc, doc.getRootElement());
	}

	public static DataTableDefinition create(String name, boolean firstRowAsTitle) {
		return new ExcelDataTableDefinition(name, firstRowAsTitle, 0, 0, 0, 0, null);
	}

	public static ExcelDataTableDefinition create(String name, boolean firstRowAsTitle, int startReadRow, int startReadColumn, int startWriteRow, int startWriteColumn,
			FormulaEvaluator evaluator) {
		return new ExcelDataTableDefinition(name, firstRowAsTitle, startReadRow, startReadColumn, startWriteRow, startWriteColumn, evaluator);
	}

//	@Override
//	protected TxtDataTableDefinition extract(Object title, List<Object> contents, ExcelDataTableDefinition definition) throws DefinitionException, DataException {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	protected ExcelDataTableDefinition parse(Document doc, Element root) throws DefinitionException {
		Attribute attrib = null;
		String value = null;

		Element recordDef = getTableDefinitionElements(doc);

		String name = "";
		int startRow = 0, startColumn = 0;
		boolean firstRowAsTitle = false;

		value = recordDef.getAttributeValue("name");
		if (value != null && !"".equals(value)) {
			name = value;
		} else {
			throw new DefinitionException("Table name is empty!");
		}

		value = recordDef.getAttributeValue("startRow");
		if (value != null && !"".equals(value)) {
			startRow = Integer.valueOf(value);
		} else {
			startRow = 0;
		}

		value = recordDef.getAttributeValue("startColumn");
		if (value != null && !"".equals(value)) {
			startColumn = Integer.valueOf(value);
		} else {
			startColumn = 0;
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

		ExcelDataTableDefinition dataRecDef = new ExcelDataTableDefinition(name, firstRowAsTitle, startRow, startColumn, startRow, startColumn, null);

		int index = 0;
		@SuppressWarnings("unchecked")
		List<Element> es = recordDef.getChildren();
		for (Element e : es) {
			DataFieldType fieldType = null;

			int mappingIndex = -1;
			boolean allowEmpty = true;
			boolean trimValue = false;
			String defaultValue = null;
			String outformatstr = null;
			String informatstr = null;

			if (e.getName().toLowerCase().contains("column")) {
				index++;

				name = e.getAttributeValue("name");
				if (name == null || name.length() == 0) {
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

				String valueTypeName = e.getAttribute("type").getValue();// .toLowerCase();
				fieldType = createFieldType(valueTypeName, allowEmpty, defaultValue, informatstr, outformatstr);
				Format[] fmts = createInOutFormat(fieldType, informatstr, outformatstr);

				StdDataFieldDefinition fieldDef = new ExcelDataFieldDefinition(name, mappingIndex, fieldType, trimValue, fmts[0], fmts[1], null);
				dataRecDef.addColumn(fieldDef);
			}
		}
		
		dataRecDef.setDefinitionFromXml(true);

		return dataRecDef;
	}
}
