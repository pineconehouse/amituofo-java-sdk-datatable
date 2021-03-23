package com.amituofo.datatable;

import java.text.Format;

public interface DataFieldParser {
	// public DataFieldParser clone();

	// public DataFieldDefinition getDefinition();

	// public void setDefinition(DataFieldDefinition definition);

	// public DataField parse(String value) throws DataException;

	public DataField parse(DataFieldDefinition definition, Object value) throws DataException;

	public Format getFormat();

	public void setFormat(Format format);
	// public void validate(DataField value) throws DataException;
}
