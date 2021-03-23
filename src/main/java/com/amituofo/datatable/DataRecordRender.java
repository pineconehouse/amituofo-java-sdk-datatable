package com.amituofo.datatable;

public interface DataRecordRender {

	public DataTableDefinition getDefinition();

	public void setDefinition(DataTableDefinition definition);

	// public DataRecordRender clone();

	public String rending(DataRecord record, int[] fieldIndexMapping);

	public String rendingTitle();
}
