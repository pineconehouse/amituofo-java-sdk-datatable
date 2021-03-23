package com.amituofo.datatable.impl.memory.parser;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.DataRecord;
import com.amituofo.datatable.impl.basic.AbsDataRecordParser;

public class MemoryDataRecordParser extends AbsDataRecordParser {

	public MemoryDataRecordParser() {
	}

//	@Override
//	public DataRecordParser<String, String> clone() {
//		TxtDataRecordParser newObj = new TxtDataRecordParser(delimiter, quote);
//		return newObj;
//	}
	
	@Override
	protected Object[] parseValues(Object record) throws DataException {
		return ((DataRecord)record).getValues();
	}

}
