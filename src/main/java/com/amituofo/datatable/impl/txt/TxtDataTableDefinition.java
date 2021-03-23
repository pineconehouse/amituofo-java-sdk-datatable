package com.amituofo.datatable.impl.txt;

import java.text.Format;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.DataFieldDefinition;
import com.amituofo.datatable.DataFieldType;
import com.amituofo.datatable.DataTableDefinition;
import com.amituofo.datatable.DefinitionException;
import com.amituofo.datatable.impl.basic.StdDataTableDefinition;
import com.amituofo.datatable.impl.txt.parser.TxtDataRecordParser;
import com.amituofo.datatable.impl.txt.render.TxtDataRecordRender;
import com.amituofo.datatable.type.StringFieldType;

public class TxtDataTableDefinition extends StdDataTableDefinition {
	public static final String CRLF = "\r\n";
	public static final String LF = "\n";

	private String charset = "UTF-8";
	private int startRow = 1;
	private boolean firstRowAsTitle = false;
	private String rowTerminated = CRLF;

	public TxtDataTableDefinition(String name, String charset, char delimiter, char quote, int startRow, boolean firstRowAsTitle) {
		super(name, new TxtDataRecordParser(delimiter, quote), new TxtDataRecordRender(delimiter));
		this.charset = charset;
		this.startRow = startRow;
		this.firstRowAsTitle = firstRowAsTitle;
	}

	public TxtDataTableDefinition(String name, String charset, char delimiter, char quote, int startRow, boolean firstRowAsTitle, String rowTerminated) {
		super(name, new TxtDataRecordParser(delimiter, quote), new TxtDataRecordRender(delimiter));
		this.charset = charset;
		this.startRow = startRow;
		this.firstRowAsTitle = firstRowAsTitle;
		this.rowTerminated = rowTerminated;
	}

	// @Override
	// public TxtDataTableDefinition clone() {
	// TxtDataTableDefinition cloneDef = new TxtDataTableDefinition(name, charset, ',', '"', startRow, firstRowAsTitle);
	// copyTo(cloneDef);
	// return cloneDef;
	// }

	@Override
	public DataFieldDefinition createField(String name, DataFieldType fieldType) {
		TxtDataFieldDefinition field = new TxtDataFieldDefinition(name, fieldType);
		field.setDelimiter(getDelimiter());
		field.setQuote(getQuote());
		return field;
	}

	public void resetColumn(DataTableDefinition fromDefinition) {
		this.removeAll();
		for (int i = 0; i < fromDefinition.getColumnCount(); i++) {
			DataFieldDefinition fieldDef = fromDefinition.getColumn(i);

			// if (fieldDef instanceof TxtDataFieldDefinition) {
			// TxtDataFieldDefinition xsvdef = (TxtDataFieldDefinition) fieldDef.clone();
			// xsvdef.setDelimiter(this.getDelimiter());
			// xsvdef.setQuote(this.getQuote());
			// this.addField(xsvdef);
			// } else {
			DataFieldType type = fieldDef.getType().clone();
			Format informat = fieldDef.getParser() != null ? fieldDef.getParser().getFormat() : null;
			Format outformat = fieldDef.getRender() != null ? fieldDef.getRender().getFormat() : null;
			TxtDataFieldDefinition xsvdef = new TxtDataFieldDefinition(fieldDef.getName(), i, type, this.getDelimiter(), this.getQuote(), false, false, informat, outformat);
			try {
				this.addColumn(xsvdef);
			} catch (DefinitionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// }
		}
	}

	@Override
	protected void extractColumnAsStringType(Object row, boolean isTitleRow) throws DefinitionException, DataException {
		if (row != null) {
			String[] titles = TxtDataRecordParser.split((String) row, getDelimiter(), getQuote(), false, false);
			int endIndex = titles.length;
			for (int i = titles.length - 1; i >= 0; i--) {
				if (titles[i].trim().length() == 0) {
					endIndex--;
				}
			}

			if (this.getColumnCount() == 0) {
				for (int i = 0; i < endIndex; i++) {
					try {
						this.addColumn(new TxtDataFieldDefinition(isTitleRow ? titles[i] : null, i, new StringFieldType(), getDelimiter(), getQuote(), false, false,
								null, null));
					} catch (DefinitionException e) {
						e.printStackTrace();
					}
				}
			} else if (isTitleRow) {
				for (int i = 0; i < this.getColumnCount(); i++) {
					DataFieldDefinition fielddef = this.getColumn(i);
					if (fielddef.getName().length() == 0 || fielddef.getName().startsWith(StdDataTableDefinition.DEFAULT_COLNAME)) {
						fielddef.setName(titles[i]);
					}
				}
			}
		}
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public char getDelimiter() {
		return ((TxtDataRecordParser) parser).getDelimiter();
	}

	public void setDelimiter(char delimiter) {
		((TxtDataRecordParser) parser).setDelimiter(delimiter);
		((TxtDataRecordRender) render).setDelimiter(delimiter);
	}

	public char getQuote() {
		return ((TxtDataRecordParser) parser).getQuote();
	}

	public void setQuote(char quote) {
		((TxtDataRecordParser) parser).setQuote(quote);
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public boolean isFirstRowAsTitle() {
		return firstRowAsTitle;
	}

	public void setFirstRowAsTitle(boolean firstRowAsTitle) {
		this.firstRowAsTitle = firstRowAsTitle;
	}

	public String getRowTerminated() {
		return rowTerminated;
	}

	public void setRowTerminated(String rowTerminated) {
		this.rowTerminated = rowTerminated;
	}

}
