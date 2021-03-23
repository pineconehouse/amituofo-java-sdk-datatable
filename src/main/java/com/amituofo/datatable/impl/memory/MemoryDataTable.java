package com.amituofo.datatable.impl.memory;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.DataRecord;
import com.amituofo.datatable.DataTable;
import com.amituofo.datatable.DefinitionException;
import com.amituofo.datatable.IndexMapping;
import com.amituofo.datatable.impl.basic.AbsDataTable;

public class MemoryDataTable extends AbsDataTable<MemoryDataTableDefinition, String, String> implements Serializable {
	private static final long serialVersionUID = 3987622779806388581L;

	private List<DataRecord> records = new ArrayList<DataRecord>(1000);

	// slow
	// private List<DataRecord> records = new LinkedList<DataRecord>();

	public MemoryDataTable(String name) throws DataException, DefinitionException {
		super(name, new MemoryDataTableDefinition(name));
	}

	public MemoryDataTable(MemoryDataTableDefinition definition) throws DataException, DefinitionException {
		super(definition.getName(), definition);
	}

	public MemoryDataTable(DataTable asDataTable) throws DataException, DefinitionException {
		this(new MemoryDataTableDefinition(asDataTable.getDefinition()));
	}

	@Override
	public DataRecord readFirstRecord() throws DataException {
		currentReadingLine = 0;

		return readNextRecord();
	}

	@Override
	public DataRecord readNextRecord() throws DataException {
		do {
			if (currentReadingLine < records.size()) {
				DataRecord rec = records.get(currentReadingLine);
				currentReadingLine++;

				if (recordFilter.isMatched(rec)) {
					return rec;
				}
			} else {
				return null;
			}
		} while (true);
	}

	@Override
	public List<DataRecord> readFirstRecords(int readCount) throws DataException {
		currentReadingLine = 0;

		return readNextRecords(readCount);
	}

	@Override
	public List<DataRecord> readNextRecords(int readCount) throws DataException {
		if (readCount <= 0) {
			return null;
		}

		int index = 0;
		List<DataRecord> content = new ArrayList<DataRecord>(readCount);

		while (index < readCount && currentReadingLine < records.size()) {
			DataRecord rec = records.get(currentReadingLine);
			currentReadingLine++;

			if (recordFilter.isMatched(rec)) {
				content.add(rec);
				index++;
			}
		}

		if (content.size() == 0) {
			return null;
		} else {
			return content;
		}
	}
	
	@Override
	public List<DataRecord> readRecords(int startRowNo, int readCount) throws DataException {
		if (readCount <= 0) {
			return null;
		}

		if (startRowNo <= 1) {
			return this.readFirstRecords(readCount);
		} else {

			currentReadingLine = startRowNo;
			
			return readNextRecords(readCount);
		}
	}

	@Override
	public void appendRecord(DataRecord row, int[] fieldIndexMapping) throws DataException {
		currentWritenLine++;

		records.add(definition.parse(row));
	}

	@Override
	public void appendRecords(List<DataRecord> rows, int[] fieldIndexMapping) throws DataException {
		if (rows != null) {
			for (DataRecord row : rows) {
				records.add(definition.parse(row));
				currentWritenLine++;
			}
		}
	}

	@Override
	public void writeTitle() throws DataException {
	}

	@Override
	public void appendAll(DataTable tableSet, boolean includeTitle, IndexMapping indexMapping) throws DataException {

		List<DataRecord> lines = tableSet.readFirstRecords(500);
		if (lines != null && lines.size() > 0) {
			do {
				this.appendRecords(lines, null);
				lines = tableSet.readNextRecords(500);
			} while (lines != null && lines.size() > 0);
		}
	}

	@Override
	public void close() {
		definition.removeAll();
		records.clear();
	}

	@Override
	public void truncate() throws DataException {
		records.clear();
	}

	@Override
	public long getRecordCount() throws DataException {
		if (recordFilter != DEFAULT_FILTER) {
			return super.getFilteredRecordCount();
		}
		
		return records.size();
	}

}
