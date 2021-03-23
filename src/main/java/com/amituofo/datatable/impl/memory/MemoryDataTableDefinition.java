package com.amituofo.datatable.impl.memory;

import com.amituofo.datatable.DataFieldDefinition;
import com.amituofo.datatable.DataFieldType;
import com.amituofo.datatable.DataTableDefinition;
import com.amituofo.datatable.DefinitionException;
import com.amituofo.datatable.impl.basic.StdDataTableDefinition;
import com.amituofo.datatable.impl.memory.parser.MemoryDataRecordParser;
import com.amituofo.datatable.impl.memory.render.MemoryDataRecordRender;
import com.amituofo.datatable.impl.txt.TxtDataFieldDefinition;

public class MemoryDataTableDefinition extends StdDataTableDefinition {

	public MemoryDataTableDefinition(String name) {
		super(name, new MemoryDataRecordParser(), new MemoryDataRecordRender());
	}

	public MemoryDataTableDefinition(DataTableDefinition fromDefinition) {
		super(fromDefinition.getName(), new MemoryDataRecordParser(), new MemoryDataRecordRender());
		resetColumn(fromDefinition);
	}
	
	public MemoryDataTableDefinition(String name, DataTableDefinition fromDefinition) {
		super(name, new MemoryDataRecordParser(), new MemoryDataRecordRender());
		resetColumn(fromDefinition);
	}

	@Override
	public DataFieldDefinition createField(String name, DataFieldType fieldType) {
		MemoryDataFieldDefinition field = new MemoryDataFieldDefinition(name, fieldType);
		return field;
	}
	
	@Override
	public void resetColumn(DataTableDefinition fromDefinition) {
		this.removeAll();
		for (int i = 0; i < fromDefinition.getColumnCount(); i++) {
			DataFieldDefinition fieldDef = fromDefinition.getColumn(i);

			DataFieldType type = fieldDef.getType().clone();
			MemoryDataFieldDefinition def = new MemoryDataFieldDefinition(fieldDef.getName(), i, type);
			try {
				this.addColumn(def);
			} catch (DefinitionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// }
		}
	}

	@Override
	public void extractColumnAsStringType(Object row, boolean isTitleRow) throws DefinitionException {
		// Do Nothing;
		//throw new DefinitionException("Unsupport method!");
	}

}
