package com.amituofo.datatable.impl.fil;

import java.text.Format;

import com.amituofo.datatable.DataFieldDefinition;
import com.amituofo.datatable.DataFieldType;
import com.amituofo.datatable.DataTableDefinition;
import com.amituofo.datatable.DefinitionException;
import com.amituofo.datatable.impl.basic.StdDataTableDefinition;
import com.amituofo.datatable.impl.fil.parser.FilDataRecordParser;
import com.amituofo.datatable.impl.fil.render.FilDataRecordRender;
import com.amituofo.datatable.impl.txt.TxtDataTableDefinition;
import com.amituofo.datatable.type.StringFieldType;

public class FilDataTableDefinition extends TxtDataTableDefinition {

	public FilDataTableDefinition(String name, String charset, int startRow, boolean firstRowAsTitle) {
		super(name, charset, ',', '"', startRow, firstRowAsTitle);
		this.setParser(new FilDataRecordParser());
		this.setRender(new FilDataRecordRender());
	}

	public FilDataTableDefinition(String name, String charset, int startRow, boolean firstRowAsTitle, String rowTerminated) {
		super(name, charset, ',', '"', startRow, firstRowAsTitle, rowTerminated);
		this.setParser(new FilDataRecordParser());
		this.setRender(new FilDataRecordRender());
	}

	// @Override
	// public FilDataTableDefinition clone() {
	// FilDataTableDefinition cloneDef = new FilDataTableDefinition(name, charset, startRow, firstRowAsTitle);
	// copyTo(cloneDef);
	// return cloneDef;
	// }

	@Override
	public void resetColumn(DataTableDefinition fromDefinition) {
		this.removeAll();
		int startIndex = 0, lastLength = 0;
		for (int i = 0; i < fromDefinition.getColumnCount(); i++) {
			DataFieldDefinition fieldDef = fromDefinition.getColumn(i);

			// if (fieldDef instanceof FilDataFieldDefinition) {
			// FilDataFieldDefinition xsvdef = (FilDataFieldDefinition) fieldDef.clone();
			// this.addField(xsvdef);
			// } else {

//			if (fieldDef.getType().getTotalLength() <= 0) {
//				throw new DefinitionException(fieldDef.getName() + " length is 0.");
//			}

			startIndex = lastLength;
			DataFieldType type = fieldDef.getType().clone();
			Format informat = fieldDef.getParser() != null ? fieldDef.getParser().getFormat() : null;
			Format outformat = fieldDef.getRender() != null ? fieldDef.getRender().getFormat() : null;
			try {
				this.addColumn(new FilDataFieldDefinition(fieldDef.getName(), type, startIndex, type.getTotalLength(), informat, outformat));
			} catch (DefinitionException e) {
				e.printStackTrace();
			}
			lastLength = type.getTotalLength();
			// }
		}
	}

	@Override
	public void extractColumnAsStringType(Object row, boolean isTitleRow) throws DefinitionException {
		if (row != null && ((String) row).length() != 0) {
			if (this.getColumnCount() == 0) {
				DataFieldType type = new StringFieldType(((String) row).length(), true, null);

				try {
					this.addColumn(new FilDataFieldDefinition(null, type, 0, ((String) row).length(), null, null));
				} catch (DefinitionException e) {
					e.printStackTrace();
				}
			} else if (isTitleRow) {
				int beginIndex = 0, lastLength = 0;
				for (int i = 0; i < fieldDefinitions.length; i++) {
					FilDataFieldDefinition fd = (FilDataFieldDefinition) fieldDefinitions[i];
					beginIndex += lastLength;
					String title = ((String) row).substring(beginIndex, beginIndex + fd.getLength());
					if (fd.getName().length() == 0 || fd.getName().startsWith(StdDataTableDefinition.DEFAULT_COLNAME)) {
						fd.setName(title.trim());
					}
					lastLength = fd.getLength();
				}
			}
		}
	}
}
