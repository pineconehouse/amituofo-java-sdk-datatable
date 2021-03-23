package com.amituofo.datatable.impl.basic;

import java.util.ArrayList;
import java.util.List;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.DataFieldDefinition;
import com.amituofo.datatable.DataFieldType;
import com.amituofo.datatable.DataRecord;
import com.amituofo.datatable.DataRecordParser;
import com.amituofo.datatable.DataRecordRender;
import com.amituofo.datatable.DataTable;
import com.amituofo.datatable.DataTableDefinition;
import com.amituofo.datatable.DefinitionException;
import com.amituofo.datatable.InvalidDataException;
import com.amituofo.datatable.impl.memory.MemoryDataTable;
import com.amituofo.datatable.utils.Moulding;

public abstract class StdDataTableDefinition extends StdDefinition implements DataTableDefinition {
	public static final String DEFAULT_COLNAME = "Column";

	protected DataFieldDefinition[] fieldDefinitions = new DataFieldDefinition[0];
	// protected DataFieldDefinition[] fieldDefinitionsWithoutHidden = new DataFieldDefinition[0];

	protected DataRecordParser parser = null;
	protected DataRecordRender render = null;
	// protected DataRecordFilter recordFilter = null;
	protected boolean definitionFromXml = false;

	// protected String function = "";

	// public abstract DataTableDefinition clone();

	public StdDataTableDefinition(String name, DataRecordParser parser, DataRecordRender render) {
		super(name);
		this.render = render;
		this.parser = parser;
		if (this.parser != null) {
			this.parser.setDefinition(this);
		}
		if (this.render != null) {
			this.render.setDefinition(this);
		}
	}

	public void extractColumnDefinition(DataTable dt, Object row, boolean isTitleRow) throws DefinitionException, DataException {
		extractColumnAsStringType(row, isTitleRow);

		extractColumnType(dt);
	}

	protected abstract void extractColumnAsStringType(Object row, boolean isTitleRow) throws DefinitionException, DataException;

	protected void extractColumnType(DataTable dt) throws DefinitionException, DataException {
		MemoryDataTable stb = Moulding.toSimpleDataTable(dt, 500, true);
		Object[][] matrix = (Object[][]) Moulding.toMatrix(stb, false);
		DataFieldType[] types = Moulding.extractColumnType(matrix, false);

		for (int col = 0; col < types.length; col++) {
			dt.getDefinition().getColumn(col).setType(types[col]);
		}
	}

	// public void copyTo(StdDataTableDefinition newObj) {
	// newObj.name = this.name;
	// newObj.parser = this.parser.clone();
	// newObj.render = this.render.clone();
	// for (DataFieldDefinition def : fieldDefinitions) {
	// newObj.fieldDefinitions.add((DataFieldDefinition) def.clone());
	// }
	// }

	// public DataTableDefinition clone() {
	// StdDataTableDefinition newObj = new StdDataTableDefinition(null, null, null);
	// copyTo(newObj);
	// return newObj;
	// }

	@Override
	public DataFieldDefinition getColumn(int index) {
		// int cape = (index + 1) - fieldDefinition.size();
		// if (cape > 0) {
		// for (int i = 0; i < cape; i++) {
		// fieldDefinition.add(new T("Column" + (fieldDefinition.size() + 1), this));
		// }
		// }
		// if (index >= fieldDefinitions.size()) {
		// return null;
		// }

		return fieldDefinitions[index];
	}

	@Override
	public DataFieldDefinition getColumn(String name) {
		for (DataFieldDefinition def : fieldDefinitions) {
			if (def.getName().equalsIgnoreCase(name)) {
				return def;
			}
		}

		return null;
	}

	@Override
	public int getColumnIndex(String name) {
		for (int i = 0; i < fieldDefinitions.length; i++) {
			if (fieldDefinitions[i].getName().equalsIgnoreCase(name)) {
				return i;
			}
		}

		return -1;
	}

	// public DataFieldDefinition addField() {
	// return (DataFieldDefinition) addStringField(DEFAULT_COLNAME + (fieldDefinition.size() + 1));
	// }

	// public List addFields(int count) {
	// List newFieldDef = new ArrayList();
	// for (int i = 0; i < count; i++) {
	// newFieldDef.add(addField());
	// }
	//
	// return newFieldDef;
	// }

	// public DataRecord newDataRecordInstance() {
	// try {
	// return (ExcelDataRecord) dataRecordClass.getConstructors()[0].newInstance(this);
	// } catch (Throwable e) {
	// // e.printStackTrace();
	// }
	//
	// return null;
	// }

	@Override
	public void addColumns(DataFieldDefinition[] all) throws DefinitionException {
		for (DataFieldDefinition dataFieldDefinition : all) {
			addColumn(dataFieldDefinition);
		}
	}

	@Override
	public void addColumn(DataFieldDefinition fieldDefinition) throws DefinitionException {
		if (fieldDefinition.getName() == null || fieldDefinition.getName().trim().length() == 0) {
			fieldDefinition.setName(DEFAULT_COLNAME + (fieldDefinitions.length + 1));
		}

		// fieldDefinition.setRelativeIndex(fieldDefinitions.length + 1);

		if (getColumn(fieldDefinition.getName()) != null) {
			throw new DefinitionException("Duplicate field name " + fieldDefinition.getName() + " is not allowed.");
		}

		if (fieldDefinition.getMappingIndex() < 0) { // ==-1
			fieldDefinition.setMappingIndex(fieldDefinitions.length);
		}
		
		DataFieldDefinition[] newFieldDefs = new DataFieldDefinition[fieldDefinitions.length + 1];
		for (int i = 0; i < fieldDefinitions.length; i++) {
			newFieldDefs[i] = fieldDefinitions[i];
		}

		newFieldDefs[fieldDefinitions.length] = fieldDefinition;

		fieldDefinitions = newFieldDefs;
	}

	@Override
	public void insertColumn(int index, DataFieldDefinition fieldDefinition) throws DefinitionException {
		if (fieldDefinition.getName() == null || fieldDefinition.getName().length() == 0) {
			fieldDefinition.setName(DEFAULT_COLNAME + (fieldDefinitions.length + 1));
		}

		if (getColumn(fieldDefinition.getName()) != null) {
			throw new DefinitionException("Duplicate field name " + fieldDefinition.getName() + " is not allowed.");
		}

		if (index > (fieldDefinitions.length)) {
			index = fieldDefinitions.length;
		}

		if (index < 0) {
			index = 0;
		}
		
		if (fieldDefinition.getMappingIndex() < 0) { // ==-1
			fieldDefinition.setMappingIndex(index);
		}

		DataFieldDefinition[] newFieldDefs = new DataFieldDefinition[fieldDefinitions.length + 1];

		int newI = 0, oldI = 0;
		for (; newI < index;) {
			newFieldDefs[newI] = fieldDefinitions[oldI];

			newI++;
			oldI++;
		}

		// fieldDefinition.setRelativeIndex(index);
		newFieldDefs[index] = fieldDefinition;
		newI = index + 1;

		for (; newI < newFieldDefs.length;) {
			// fieldDefinitions[oldI].setRelativeIndex(newI);
			newFieldDefs[newI] = fieldDefinitions[oldI];

			newI++;
			oldI++;
		}

		fieldDefinitions = newFieldDefs;
	}

	@Override
	public DataFieldDefinition hiddenColumn(int index) {

		if (index > (fieldDefinitions.length)) {
			index = fieldDefinitions.length;
		}

		if (index < 0) {
			index = 0;
		}

		fieldDefinitions[index].setHidden(true);

		return fieldDefinitions[index];

		// int newI = 0, oldI = 0;
		// DataFieldDefinition deleteItem = null;
		// DataFieldDefinition[] newFieldDefs = new DataFieldDefinition[fieldDefinitions.length - 1];
		// for (; newI < newFieldDefs.length;) {
		// if (oldI != index) {
		// newFieldDefs[newI] = fieldDefinitions[oldI];
		//
		// newI++;
		// oldI++;
		// } else {
		// deleteItem = fieldDefinitions[oldI];
		//
		// oldI++;
		// }
		// }
		//
		// fieldDefinitions = newFieldDefs;
		// return deleteItem;
	}

	@Override
	public DataFieldDefinition hiddenColumn(String name) {
		int index = this.getColumnIndex(name);
		if (index != -1) {
			return this.hiddenColumn(index);
		} else {
			return null;
		}
	}

	// @Override
	public void removeAll() {
		fieldDefinitions = new DataFieldDefinition[0];
	}

	@Override
	public int getColumnCount() {
		int count = 0;
		for (int i = 0; i < fieldDefinitions.length; i++) {
			count += fieldDefinitions[i].isHidden() ? 0 : 1;
		}

		return count;
	}

	@Override
	public DataFieldDefinition[] getColumns() {
		DataFieldDefinition[] newFieldDefinitions = new DataFieldDefinition[getColumnCount()];
		int j = 0;
		for (int i = 0; i < fieldDefinitions.length; i++) {
			if (!fieldDefinitions[i].isHidden()) {
				newFieldDefinitions[j++] = fieldDefinitions[i];
			}
		}

		return newFieldDefinitions;
	}

	// public int getColumnCount() {
	// return fieldDefinitions.length;
	// }
	//
	// public DataFieldDefinition[] getColumns() {
	// return fieldDefinitions;
	// }

	@Override
	public String[] getColumnNames() {
		DataFieldDefinition[] cols = getColumns();
		String[] values = new String[cols.length];
		for (int i = 0; i < cols.length; i++) {
			values[i] = cols[i].getName();
		}
		return values;
	}

	@Override
	public void setColumnNames(String... names) {
		int size = names.length > fieldDefinitions.length ? fieldDefinitions.length : names.length;
		for (int i = 0; i < size; i++) {
			fieldDefinitions[i].setName(names[i]);
		}
	}

	@Override
	public DataRecord newDataRecordInstance(boolean initFields) {
		// int count = 0;
		// for (int i = 0; i < fieldDefinitions.length; i++) {
		// count += fieldDefinitions[i].isHidden() ? 0 : 1;
		// }
		//
		// DataFieldDefinition[] newFieldDefinitions = new DataFieldDefinition[count];
		// int j = 0;
		// for (int i = 0; i < fieldDefinitions.length; i++) {
		// if (!fieldDefinitions[i].isHidden()) {
		// newFieldDefinitions[j++] = fieldDefinitions[i];
		// }
		// }

		final DataRecord dr = new DataRecordImpl(this.getColumns(), initFields);
		return dr;
	}

	@Override
	public DataRecordParser getParser() {
		return parser;
	}

	@Override
	public void setParser(DataRecordParser parser) {
		this.parser = parser;
		if (this.parser != null) {
			this.parser.setDefinition(this);
		}
	}

	@Override
	public DataRecordRender getRender() {
		return render;
	}

	@Override
	public void setRender(DataRecordRender Render) {
		this.render = Render;
		if (this.render != null) {
			this.render.setDefinition(this);
		}
	}

	// @Override
	// public DataRecordFilter getFilter() {
	// return recordFilter;
	// }
	//
	// @Override
	// public void setFilter(DataRecordFilter recordFilter) {
	// this.recordFilter = recordFilter;
	// }
	//
	// // public void parseTitle(Object value) throws DefinitionException, DataException {
	// // extractColumnAsStringType(value, true);
	// // }
	//
	// @Override
	// public boolean isMatched(DataRecord record) {
	// return (recordFilter == null ? true : recordFilter.isMatched(record));
	// }

	@Override
	public DataRecord parse(Object value) throws DataException {
		// DataRecord record = parser.parseRecord(value);
		// if (recordFilter != null) {
		// return recordFilter.isMatched(record) ? record : null;
		// }
		//
		// return record;

		return parser.parseRecord(value);
	}

	@Override
	public String rending(DataRecord value, int[] fieldIndexMapping) {
		return render.rending(value, fieldIndexMapping);
	}

	@Override
	public String rendingTitle() {
		return render.rendingTitle();
	}

	// public String[] rending(DataField[] values) {
	// String[] strvalues = new String[fieldDefinitions.size()];
	// for (int i = 0; i < strvalues.length && i < values.length; i++) {
	// if (values[i] != null) {
	// strvalues[i] = fieldDefinitions.get(i).rending(values[i]);
	// }
	// }
	// return strvalues;
	// }

	public boolean isDefinitionFromXml() {
		return definitionFromXml;
	}

	public void setDefinitionFromXml(boolean definitionFromXml) {
		this.definitionFromXml = definitionFromXml;
	}

	@Override
	public void validate(DataRecord value) throws InvalidDataException {
		List<String> errors = new ArrayList<String>();

		for (int i = 0; i < fieldDefinitions.length; i++) {
			DataFieldDefinition fd = fieldDefinitions[i];
			if (!fd.isHidden()) {
				try {
					fd.validate(value.get(i));
				} catch (InvalidDataException e) {
					// errors.add(("Column " + (i + 1) + " " + fd.getName() + " ") + e.getMessage());
					errors.add((fd.getName() + " : ") + e.getMessage());
				}
			}
		}

		if (errors.size() > 0) {
			throw new InvalidDataException(errors.toString());
		}

	}

}
