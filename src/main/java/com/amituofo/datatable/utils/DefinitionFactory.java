package com.amituofo.datatable.utils;

import com.amituofo.datatable.DataFieldDefinition;
import com.amituofo.datatable.DataTableDefinition;
import com.amituofo.datatable.utils.ClassicDefinition.FieldDef;
import com.amituofo.datatable.utils.ClassicDefinition.TableDef;


public class DefinitionFactory {
	public static DataTableDefinition newTableDefinition(String name, TableDef type ) {
		switch(type) {
		case CSV_UTF8_WITH_TITLE:
			break;
		case TSV_UTF8_WITH_TITLE:
			break;
		case CSV_UTF8_WITHOUT_TITLE:
			break;
		case TSV_UTF8_WITHOUT_TITLE:
			break;
			
		}
		
		return null;
	}
	
	public static DataFieldDefinition newFieldDefinition(String name, FieldDef type ) {
		switch(type) {
		case CSV:
			break;
		case TSV:
			break;
			
		}
		
		return null;
	}
}
