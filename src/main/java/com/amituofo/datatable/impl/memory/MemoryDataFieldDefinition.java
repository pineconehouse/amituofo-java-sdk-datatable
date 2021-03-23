package com.amituofo.datatable.impl.memory;

import com.amituofo.datatable.DataFieldType;
import com.amituofo.datatable.impl.basic.StdDataFieldDefinition;
import com.amituofo.datatable.impl.basic.parser.Object2ObjectFieldParser;
import com.amituofo.datatable.impl.memory.render.MemoryDataFieldRender;

public class MemoryDataFieldDefinition extends StdDataFieldDefinition {

	public MemoryDataFieldDefinition(String name, int mappingIndex, DataFieldType type) {
		super(name, mappingIndex, type, new Object2ObjectFieldParser(), new MemoryDataFieldRender());
	}
	
	public MemoryDataFieldDefinition(String name, DataFieldType type) {
		super(name, -1, type, new Object2ObjectFieldParser(), new MemoryDataFieldRender());
	}
	
//	public SimpleDataFieldDefinition(String name, int mappingIndex, DataFieldType type, Format informat, Format outformat) {
//		super(name, mappingIndex, type, new Object2ObjectFieldParser(), new SimpleDataFieldRender());
//	}

	@Override
	public void setType(DataFieldType type) {
		this.type = type;
		this.parser = new Object2ObjectFieldParser();
	}

//	public Format getOutformat() {
//		return ((TxtDataFieldRender) render).getFormat();
//	}
//
//	public void setOutformat(Format outformat) {
//		((TxtDataFieldRender) render).setFormat(outformat);
//	}
//
//	public Format getInformat() {
//		return ((AbsTxtString2XDataFieldParser<?,?>) parser).getFormat();
//	}
//
//	public void setInformat(Format informat) {
//		((AbsTxtString2XDataFieldParser<?,?>) parser).setFormat(informat);
//	}

}
