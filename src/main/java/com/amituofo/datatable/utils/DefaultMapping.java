package com.amituofo.datatable.utils;

import com.amituofo.datatable.DataTableDefinition;
import com.amituofo.datatable.IndexMapping;

public class DefaultMapping {

	public final static IndexMapping COLUMN_NAME_MAPPING = new IndexMapping() {
		@Override
		public int[] indexMapping(DataTableDefinition thisTableDef, DataTableDefinition sourceTableDef) {
			int[] fieldIndexMapping = new int[thisTableDef.getColumnCount()];
			for (int i = 0; i < fieldIndexMapping.length; i++) {
				fieldIndexMapping[i] = -1;
			}
			
			for (int i = 0; i < fieldIndexMapping.length; i++) {
				int index = sourceTableDef.getColumnIndex(thisTableDef.getColumn(i).getName());
				if (index != -1) {
					fieldIndexMapping[i] = index;
				}
			}
			
			return fieldIndexMapping;
		}
	};

	public final static IndexMapping COLUMN_INDEX_MAPPING = new IndexMapping() {
		@Override
		public int[] indexMapping(DataTableDefinition thisTableDef, DataTableDefinition sourceTableDef) {
			int[] fieldIndexMapping = new int[thisTableDef.getColumnCount()];
			for (int i = 0; i < fieldIndexMapping.length; i++) {
				fieldIndexMapping[i] = -1;
			}
			
			for (int i = 0; i < fieldIndexMapping.length; i++) {
				fieldIndexMapping[i] = i;
			}
			
			return fieldIndexMapping;
		}
	};

}
