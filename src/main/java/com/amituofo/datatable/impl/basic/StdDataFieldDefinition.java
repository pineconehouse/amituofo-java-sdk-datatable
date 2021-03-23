package com.amituofo.datatable.impl.basic;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.DataField;
import com.amituofo.datatable.DataFieldDefinition;
import com.amituofo.datatable.DataFieldParser;
import com.amituofo.datatable.DataFieldRender;
import com.amituofo.datatable.DataFieldType;
import com.amituofo.datatable.InvalidDataException;
import com.amituofo.datatable.impl.basic.parser.Object2StringFieldParser;
import com.amituofo.datatable.impl.basic.render.StringFieldRender;
import com.amituofo.datatable.type.StringFieldType;

public abstract class StdDataFieldDefinition extends StdDefinition implements DataFieldDefinition {

	protected int mappingIndex = -1;
	protected DataFieldType type = new StringFieldType();
	protected DataFieldParser parser = new Object2StringFieldParser();
	protected DataFieldRender render = new StringFieldRender();
	protected String description = "";
	protected boolean hidden = false;
	private DataField defaultValue = null;

//	public abstract DataFieldDefinition clone();
	
	public StdDataFieldDefinition(String name, int mappingIndex, DataFieldType type, DataFieldParser parser, DataFieldRender render) {
		super(name);
		this.mappingIndex = mappingIndex;
		this.type = type;
		this.render = render;
		this.parser = parser;
//		this.parser.setDefinition(this);
//		this.render.setDefinition(this);
		
		this.defaultValue  = type.newField();
	}

//	public void copyTo(StdDataFieldDefinition newObj) {
//		newObj.name = this.name;
//		newObj.type = this.type.clone();
//		newObj.parser = this.parser.clone();
//		newObj.render = this.render.clone();
//		newObj.description = this.description;
//	}

//	public DataFieldDefinition clone() {
//		StdDataFieldDefinition newObj = new StdDataFieldDefinition(null, null, null, null);
//		copyTo(newObj);
//		return newObj;
//	}
	
	@Override
	public DataField getDefault() {
		return defaultValue;
	}

	@Override
	public DataField newDataFieldInstance() {
		return type.newField();
	}

	@Override
	public boolean equals(Object obj) {
		StdDataFieldDefinition f1 = (StdDataFieldDefinition) obj;

		return f1.name.equals(this.name);
	}

	@Override
	public void validate(DataField value) throws InvalidDataException {
		type.validate(value);
	}

	@Override
	public DataField parse(Object value) throws DataException {
		return parser.parse(this, value);
	}

	@Override
	public String rending(DataField value) {
		return render.rending(value);
	}

//	@Override
//	public String rendingDefault() {
//		return render.rendingDefault();
//	}

	@Override
	public DataFieldParser getParser() {
		return parser;
	}

//	@Override
//	public void setParser(DataFieldParser parser) {
//		this.parser = parser;
//	}

	@Override
	public DataFieldRender getRender() {
		return render;
	}

//	public void setRender(DataFieldRender Render) {
//		this.render = Render;
//	}

	@Override
	public DataFieldType getType() {
		return type;
	}

//	public void setType(DataFieldType type) {
//		this.type = type;
//	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int getMappingIndex() {
		return mappingIndex;
	}

	@Override
	public void setMappingIndex(int mappingIndex) {
		this.mappingIndex = mappingIndex;
	}

	@Override
	public boolean isHidden() {
		return hidden;
	}

	@Override
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

}
