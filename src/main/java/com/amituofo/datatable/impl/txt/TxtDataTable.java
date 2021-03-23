package com.amituofo.datatable.impl.txt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.DataRecord;
import com.amituofo.datatable.DataTable;
import com.amituofo.datatable.DefinitionException;
import com.amituofo.datatable.IndexMapping;
import com.amituofo.datatable.impl.basic.AbsDataTable;

public class TxtDataTable extends AbsDataTable<TxtDataTableDefinition, String, String> {

	// private LineReader bufferedReader = null;
	private BufferedReader bufferedReader = null;

	private InputStreamReader streamReader = null;
	private InputStream inStream = null;

	private OutputStreamWriter streamWriter = null;
	private OutputStream outStream = null;

	private File file = null;
	private String charset = "UTF-8";
	private int startRow = 1;
	private boolean firstRowAsTitle;
	private String rowTerminated = null;

	public TxtDataTable(String name, TxtDataTableDefinition definition, File file)
	// public TxtDataTable(String name, DataTableDefinition definition, File file, String charset, char delimiter, char smartQuote, int startRow, boolean firstRowAsTitle)
			throws DataException, DefinitionException {
		super(name, definition);
		this.file = file;
		this.charset = definition.getCharset();
		this.startRow = definition.getStartRow();
		this.firstRowAsTitle = definition.isFirstRowAsTitle();
		this.rowTerminated = definition.getRowTerminated();

		createWriter(true);

		createReader(false);

		extractColumnDefinition();
	}
	
	public TxtDataTable(String name, TxtDataTableDefinition definition, InputStream inStream, OutputStream outStream)
			throws DataException, DefinitionException {
		super(name, definition);
		this.charset = definition.getCharset();
		this.startRow = definition.getStartRow();
		this.firstRowAsTitle = definition.isFirstRowAsTitle();
		this.rowTerminated = definition.getRowTerminated();

		createWriter(true);

		createReader(false);

		extractColumnDefinition();
	}

	@Override
	public DataRecord readFirstRecord() throws DataException {
		createReader(true);

		return readNextRecord();
	}

	@Override
	public DataRecord readNextRecord() throws DataException {
		do {
			String row = null;

			try {
				row = bufferedReader.readLine();
			} catch (IOException e) {
				throw new DataException(e);
			}

			if (row != null) {
				DataRecord rec = definition.parse(row);
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
		createReader(true);

		return readNextRecords(readCount);
	}

	@Override
	public List<DataRecord> readNextRecords(int readCount) throws DataException {
		if (readCount <= 0) {
			return null;
		}

		int index = 1;
		List<DataRecord> content = new ArrayList<DataRecord>(readCount);

		do {
			String row = null;
			try {
				row = bufferedReader.readLine();
			} catch (IOException e) {
				throw new DataException(e);
			}

			if (row != null) {
				DataRecord rec = definition.parse(row);
				currentReadingLine++;

				if (recordFilter.isMatched(rec)) {
					content.add(rec);
					index++;
				}
			} else {
				break;
			}
		} while (index <= readCount);

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
			createReader(true);

			try {
				startRowNo--;
				String value = null;
				do {
					value = bufferedReader.readLine();
					currentReadingLine++;
				} while (value != null && currentReadingLine < startRowNo);
			} catch (IOException e) {
				throw new DataException(e);
			}
			
			return readNextRecords(readCount);
		}
	}

	@Override
	public void appendRecord(DataRecord row, int[] fieldIndexMapping) throws DataException {
		try {
			// streamWriter.write(recordParser.parse(definition, row) + rowTerminated);
			streamWriter.write(definition.rending(row, fieldIndexMapping) + rowTerminated);
		} catch (IOException e) {
			throw new DataException(e);
		}
		currentWritenLine++;
	}

	@Override
	public void appendRecords(List<DataRecord> rows, int[] fieldIndexMapping) throws DataException {
		if (rows != null) {
			for (DataRecord row : rows) {
				try {
					// streamWriter.write(recordParser.parse(definition, row) + rowTerminated);
					streamWriter.write(definition.rending(row, fieldIndexMapping) + rowTerminated);
				} catch (IOException e) {
					throw new DataException(e);
				}
				currentWritenLine++;
			}
		}
	}

	@Override
	public void writeTitle() throws DataException {
		try {
			streamWriter.write(definition.rendingTitle() + rowTerminated);
		} catch (IOException e) {
			throw new DataException(e);
		}
		currentWritenLine++;
	}

	// public void appendRecord(String[] line) throws DataException {
	// try {
	// streamWriter.write(recordParser.parse(definition, line) + rowTerminated);
	// } catch (IOException e) {
	// throw new DataException(e);
	// }
	// currentWritenLine++;
	// }
	//
	// public void appendRecord(Object[] line) throws DataException {
	// try {
	// streamWriter.write(recordParser.parse(definition, line) + rowTerminated);
	// } catch (IOException e) {
	// throw new DataException(e);
	// }
	// currentWritenLine++;
	// }

	@Override
	public void appendAll(DataTable tableSet, boolean includeTitle, IndexMapping indexMapping) throws DataException {
		int[] fieldIndexMapping = null;

		if (indexMapping != null) {
			fieldIndexMapping = indexMapping.indexMapping(definition, tableSet.getDefinition());
		}

		List<DataRecord> lines = tableSet.readFirstRecords(500);
		if (lines != null && lines.size() > 0) {
			if (includeTitle && firstRowAsTitle) {
				writeTitle();
			}

			do {
				this.appendRecords(lines, fieldIndexMapping);
				lines = tableSet.readNextRecords(500);
			} while (lines != null && lines.size() > 0);
		}
	}

	@Override
	public long getRecordCount() throws DataException {
		if (recordFilter != DEFAULT_FILTER) {
			return super.getFilteredRecordCount();
		}

		createReader(true);

		long count = 0;
		try {
			String value = bufferedReader.readLine();
			if (value != null) {
				while (value != null) {
					count++;
					value = bufferedReader.readLine();
				}
			}
		} catch (IOException e) {
			throw new DataException(e);
		}

		return count;
	}

	@Override
	public void close() {
		if (bufferedReader != null) {
			try {
				bufferedReader.close();
			} catch (IOException e) {
			}
			bufferedReader = null;
		}

		if (streamReader != null) {
			try {
				streamReader.close();
			} catch (IOException e) {
			}
			streamReader = null;
		}

		if (inStream != null) {
			try {
				inStream.close();
			} catch (IOException e) {
			}
			inStream = null;
		}

		if (streamWriter != null) {
			try {
				streamWriter.flush();
				streamWriter.close();
			} catch (IOException e) {
			}
			streamWriter = null;
		}

		if (outStream != null) {
			try {
				outStream.close();
			} catch (IOException e) {
			}
			outStream = null;
		}
	}

	// ----------------------------------------------------------------------------------------------
	// private Object getTitle() throws DataException {
	// String title = null;
	// if (firstRowAsTitle) {
	// try {
	// title = bufferedReader.readLine();
	// } catch (IOException e) {
	// throw new DataException(e);
	// }
	// }
	//
	// return title;
	// }
	//
	// private List<Object> getContents(int count) throws DataException {
	// List<Object> contents = new ArrayList<Object>(count);
	//
	// for (int i = 0; i < count; i++) {
	// String row = null;
	// try {
	// row = bufferedReader.readLine();
	// } catch (IOException e) {
	// throw new DataException(e);
	// }
	//
	// if (row != null) {
	// contents.add(row);
	// } else {
	// break;
	// }
	// }
	//
	// return contents;
	// }

	private void extractColumnDefinition() throws DataException, DefinitionException {
		String row = null;
		try {
			row = bufferedReader.readLine();
		} catch (IOException e) {
			throw new DataException(e);
		}

		definition.extractColumnDefinition(this, row, firstRowAsTitle);
	}

	// private void skipTitleRow() throws DataException, DefinitionException {
	// if (firstRowAsTitle) {
	// try {
	// bufferedReader.readLine();
	// } catch (IOException e) {
	// throw new DataException(e);
	// }
	// }
	// }

	// need when no title in csv
	// private void detectFieldsDefinition() throws DataException, DefinitionException {
	// String row = null;
	// try {
	// row = bufferedReader.readLine();
	// } catch (IOException e) {
	// throw new DataException(e);
	// }
	//
	// if (row != null) {
	// definition.extractColumnAsStringType(row, false);
	// }

	// }

	// private void resetFieldsType() throws DataException {
	// String row = null;
	// try {
	// row = bufferedReader.readLine();
	// } catch (IOException e) {
	// throw new DataException(e);
	// }
	//
	// currentReadingLine++;
	//
	// recordParser.detectFieldsType(definition, row);
	// }

	private int detectBomSize() throws IOException {
		int BOM_SIZE = 4;
		byte[] bom = new byte[BOM_SIZE];
		int bomsize = 0;
		FileInputStream is = null;
		try {
			is = new FileInputStream(this.file);
			is.read(bom, 0, bom.length);
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException localIOException) {
				}
		}
		if ((bom[0] == -17) && (bom[1] == -69) && (bom[2] == -65)) {
			this.charset = "UTF-8";
			bomsize = 3;
		} else if ((bom[0] == 0) && (bom[1] == 0) && (bom[2] == -2) && (bom[3] == -1)) {
			this.charset = "UTF-32BE";
			bomsize = 4;
		} else if ((bom[0] == -1) && (bom[1] == -2) && (bom[2] == 0) && (bom[3] == 0)) {
			this.charset = "UTF-32LE";
			bomsize = 4;
		} else if ((bom[0] == -2) && (bom[1] == -1)) {
			this.charset = "UTF-16BE";
			bomsize = 2;
		} else if ((bom[0] == -1) && (bom[1] == -2)) {
			this.charset = "UTF-16LE";
			bomsize = 2;
		} else {
			bomsize = 0;
		}

		return bomsize;
	}

	private void createReader(boolean skipTitleRow) throws DataException {
		try {
			int bomsize = detectBomSize();

			if (this.bufferedReader != null) {
				this.bufferedReader.close();
				this.streamReader.close();
				this.inStream.close();
			}
			this.inStream = new FileInputStream(file);
			this.streamReader = new InputStreamReader(this.inStream, charset);
			this.bufferedReader = new BufferedReader(this.streamReader);
			// this.bufferedReader = new LineReader(this.streamReader);
			this.currentReadingLine = 0;

			if (bomsize != 0) {
				byte[] buf = new byte[bomsize];
				this.inStream.read(buf);
			}

			for (int i = 1; i < startRow; i++) {
				bufferedReader.readLine();
			}

			if (skipTitleRow && firstRowAsTitle) {
				bufferedReader.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException(e);
		}
	}

	private void createWriter(boolean append) throws DataException {
		try {
			if (this.streamWriter != null) {
				this.streamWriter.close();
				this.outStream.close();
			}
			this.outStream = new FileOutputStream(file, append);
			this.streamWriter = new OutputStreamWriter(this.outStream, charset);
			this.currentWritenLine = 0;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DataException(e);
		}
	}

	public void truncate() throws DataException {
		try {
//			file.delete();
			createWriter(false);
		} catch (DataException e) {
			throw new DataException(e);
		}

		if (firstRowAsTitle) {
			writeTitle();
		}
	}

	public void setRowTerminated(String str) {
		this.rowTerminated = str;
	}

}
