package com.amituofo.datatable.impl.basic;

import java.io.File;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import com.amituofo.datatable.DataFieldType;
import com.amituofo.datatable.DataTableDefinition;
import com.amituofo.datatable.DefinitionException;
import com.amituofo.datatable.type.DateFieldType;
import com.amituofo.datatable.type.DecimalFieldType;
import com.amituofo.datatable.type.ObjectFieldType;
import com.amituofo.datatable.type.StringFieldType;

public abstract class StdDefinitionStore<T extends DataTableDefinition> {
	public static final String DOC_TABLE_DEFINITION = "table-definition";
	// private final Map<String, StdDefinition> configMap = new HashMap<String, StdDefinition>();

	// public abstract DefinitionStore<T> loadConfig() throws DefinitionException;
	// public abstract DefinitionStore<T> loadConfig(File defineFile) throws DefinitionException;

	// public static StdDefinitionStore loadConfig(StdDefinitionStore instance) throws StdDefinitionException {
	// File[] files = instance.getDefaultConfigFiles();
	// if (files != null && files.length != 0) {
	// for (File file : files) {
	// loadConfig(instance, file.getPath());
	// }
	// } else {
	// throw new StdDefinitionException("Default config file not set");
	// }
	//
	// return instance;
	// }

	// protected abstract File[] getDefaultConfigFiles();

	// public static DataTableDefinition loadConfig(DataTableDefinition instance, String defineFile) throws DefinitionException {
	// try {
	// Document doc = (Document) loadDocument(defineFile);
	//
	// instance.parse(doc, doc.getRootElement());
	// } catch (Exception e) {
	// // e.printStackTrace();
	// throw new DefinitionException(e);
	// }
	//
	// return instance;
	// }

	protected abstract T parse(Document doc, Element root) throws DefinitionException;

//	protected abstract TxtDataTableDefinition extract(Object title, List<Object> contents, T definition) throws DefinitionException, DataException;

	public static Document loadDocument(File defineFile) throws DefinitionException {
		Document doc = null;
		SAXBuilder builder = new SAXBuilder(false);
		builder.setEntityResolver(new EntityResolver() {
			public InputSource resolveEntity(String publicId, String systemId) {
				return new InputSource(new StringReader(""));
			}
		});
		try {
			doc = (Document) builder.build(defineFile);
		} catch (Exception e) {
			// e.printStackTrace();
			throw new DefinitionException(e);
		}

		return doc;
	}

	protected int[] tractMaxMin(String type) {
		int s = type.indexOf('(');
		int e = type.indexOf(')');
		if (s != -1 && e != -1) {
			String[] MaxMin = type.substring(s + 1, e).split(",");
			int[] maxmin = new int[MaxMin.length];
			// maxmin[0] = maxmin[1] = 0;

			for (int i = 0; i < MaxMin.length; i++) {
				maxmin[i] = Integer.parseInt(MaxMin[i]);
			}

			return maxmin;
		}

		return new int[0];
	}

	protected String[] tractStringParams(String type) {
		int s = type.indexOf('(');
		int e = type.indexOf(')');
		if (s != -1 && e != -1) {
			String[] MaxMin = type.substring(s + 1, e).split(",");
			String[] maxmin = new String[MaxMin.length];
			for (int i = 0; i < MaxMin.length; i++) {
				maxmin[i] = MaxMin[i].trim();
			}

			return maxmin;
		}

		return new String[0];
	}
	
	protected Element getTableDefinitionElements(Document doc) throws DefinitionException {
		Element recordDef = doc.getRootElement().getChild(DOC_TABLE_DEFINITION);
		if (recordDef == null) {
			recordDef = doc.getRootElement().getChild("TableDefinition");
			if (recordDef == null) {
				throw new DefinitionException("Table definition not found!");
			}
		}

		return recordDef;
	}

	protected DataFieldType createFieldType(String valueTypeName, boolean allowEmpty, String defaultValue, String informatstr, String outformatstr) throws DefinitionException {
		Format informat = null;
		Format outformat = null;
		DataFieldType fieldType;

		valueTypeName = valueTypeName.toLowerCase();
		if (valueTypeName.startsWith("string")) {
			int[] maxmin = tractMaxMin(valueTypeName);
			if (maxmin.length >= 1) {
				fieldType = new StringFieldType(maxmin[0], allowEmpty, defaultValue);
			} else {
				fieldType = new StringFieldType(allowEmpty, defaultValue);
			}
		} else if (valueTypeName.startsWith("decimal")) {
			if (informatstr != null) {
				informat = new DecimalFormat(informatstr);
			}
			if (outformatstr != null) {
				outformat = new DecimalFormat(outformatstr);
			}

			BigDecimal dv = (defaultValue == null ? null : new BigDecimal(defaultValue));
			int[] maxmin = tractMaxMin(valueTypeName);
			if (maxmin.length >= 2) {
				fieldType = new DecimalFieldType(maxmin[0], maxmin[1], allowEmpty, dv);
			} else if (maxmin.length == 0) {
				fieldType = new DecimalFieldType(allowEmpty, dv);
			} else {
				fieldType = new DecimalFieldType(maxmin[0], 0, allowEmpty, dv);
			}
		} else if (valueTypeName.startsWith("datetime")) {
			if (informatstr != null) {
				informat = new SimpleDateFormat(informatstr);
			}
			outformat = informat;
			if (outformatstr != null) {
				outformat = new SimpleDateFormat(outformatstr);
			}
			try {
				Date dv = defaultValue == null ? null : ((DateFormat) informat).parse(defaultValue);

				int[] maxmin = tractMaxMin(valueTypeName);
				if (maxmin.length >= 1) {
					fieldType = new DateFieldType(maxmin[0], allowEmpty, dv);
				} else {
					fieldType = new DateFieldType(allowEmpty, dv);
				}
			} catch (ParseException e1) {
				throw new DefinitionException("Date format invalid! " + defaultValue);
			}
		} else if (valueTypeName.startsWith("object")) {
			fieldType = new ObjectFieldType(allowEmpty, defaultValue);
		} else {
			fieldType = new StringFieldType(allowEmpty, defaultValue);
		}

		return fieldType;
	}

	protected Format[] createInOutFormat(DataFieldType fieldType, String informatstr, String outformatstr) {
		Format informat = null;
		Format outformat = null;
		if (fieldType instanceof DecimalFieldType) {
			if (informatstr != null) {
				informat = new DecimalFormat(informatstr);
			}
			if (outformatstr != null) {
				outformat = new DecimalFormat(outformatstr);
			}
		} else if (fieldType instanceof DateFieldType) {
			if (informatstr != null) {
				informat = new SimpleDateFormat(informatstr);
			}
			outformat = informat;
			if (outformatstr != null) {
				outformat = new SimpleDateFormat(outformatstr);
			}
		}

		return new Format[] { informat, outformat };
	}

	// public T getConfiguration(String name) {
	// return (T) configMap.get(name);
	// }
	//
	// // public T getConfigInfo(String name) {
	// // Collection c = configMap.values();
	// // for
	// // }
	//
	// // public void setConfigInfo(String name, ConfigInfo info) {
	// // configMap.put(name, info);
	// // }
	//
	// public void addConfiguration(StdDefinition info) {
	// configMap.put(info.getName(), info);
	// }
	//
	// public void removeConfiguration(String name) {
	// configMap.remove(name);
	// }
	//
	// public void clearConfiguration() {
	// configMap.clear();
	// }
}
