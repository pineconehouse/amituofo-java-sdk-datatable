package com.amituofo.datatable.impl.basic;

import com.amituofo.datatable.DataRecordRender;
import com.amituofo.datatable.DataTableDefinition;

public abstract class AbsDataRecordRender implements DataRecordRender {
	protected DataTableDefinition definition;

//	public AbsDataRecordParser(DataTableDefinition definition) {
//		this.definition = definition;
//	}
	
//	public abstract DataRecordRender clone();

	@Override
	public DataTableDefinition getDefinition() {
		return definition;
	}

	@Override
	public void setDefinition(DataTableDefinition definition) {
		this.definition = definition;
	}
	
}
