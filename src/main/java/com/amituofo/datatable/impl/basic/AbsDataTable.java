package com.amituofo.datatable.impl.basic;

import java.io.IOException;
import java.util.List;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.DataRecord;
import com.amituofo.datatable.DataRecordFilter;
import com.amituofo.datatable.DataTable;
import com.amituofo.datatable.DataTableDefinition;
import com.amituofo.datatable.DefinitionException;

public abstract class AbsDataTable<TBL_DEF extends DataTableDefinition, RECORD_TYPE, FIELD_TYPE> implements DataTable {

	protected String name = "";
	protected int currentReadingLine = 0;
	protected int currentWritenLine = 0;
	protected TBL_DEF definition = null;
	protected DataRecordFilter recordFilter = DEFAULT_FILTER;
	protected static final DataRecordFilter DEFAULT_FILTER = new DataRecordFilter() {
		@Override
		public boolean isMatched(DataRecord record) {
			return true;
		}
	};

	public AbsDataTable(String name, TBL_DEF definition) {
		this.name = name;
		this.definition = definition;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public TBL_DEF getDefinition() {
		return definition;
	}

	// public void setDefinition(DataTableDefinition definition) {
	// this.definition = definition;
	// // this.recordParser = definition.getParser();
	// // this.recordRender = definition.getRender();
	// }

	@Override
	public int currentReadingLine() {
		return currentReadingLine;
	}

	// public String[] getColumnNames() {
	// return definition.getColumnNames();
	// }

	@Override
	public int currentWritenLine() {
		return currentWritenLine;
	}

	@Override
	public void resetColumn(DataTableDefinition definition) throws DefinitionException {
		this.definition.resetColumn(definition);
	}

	@Override
	public void setFilter(DataRecordFilter recordFilter) {
		if (recordFilter == null) {
			this.recordFilter = DEFAULT_FILTER;
		} else {
			this.recordFilter = recordFilter;
		}
	}

	@Override
	public DataRecordFilter getFilter() {
		return this.recordFilter;
	}

	// @Override
	protected int getFilteredRecordCount() throws DataException {
		int count = 0;

		// not include filter
		List<DataRecord> rows = this.readFirstRecords(500);
		while (rows != null) {
			count += rows.size();
			rows = this.readNextRecords(500);
		}

		return count;
	}
	
	@Override
	public void appendRecord(DataRecord row) throws DataException {
		appendRecord(row, null);
	}

	@Override
	public void appendRecords(List<DataRecord> rows) throws DataException {
		appendRecords(rows, null);
	}
}
