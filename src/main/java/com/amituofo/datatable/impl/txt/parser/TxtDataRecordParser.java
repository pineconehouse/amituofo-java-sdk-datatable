package com.amituofo.datatable.impl.txt.parser;

import java.util.ArrayList;
import java.util.List;

import com.amituofo.datatable.DataException;
import com.amituofo.datatable.impl.basic.AbsDataRecordParser;

public class TxtDataRecordParser extends AbsDataRecordParser {
	// public static enum PARSE_MODE {
	// // 定义为主，设置的row列数大于定义时 忽略row
	// DEFINITION_PRIORITY,
	// // 源Row为主，设置的row列数大于定义时 扩充定义
	// SOURCE_PRIORITY, INTERSECTIONS_PRIORITY
	// }

	private char delimiter = ',';
	private char quote = '"';

	public TxtDataRecordParser(char delimiter, char quote) {
		this.delimiter = delimiter;
		this.quote = quote;
	}

//	@Override
//	public DataRecordParser<String, String> clone() {
//		TxtDataRecordParser newObj = new TxtDataRecordParser(delimiter, quote);
//		return newObj;
//	}
	
	@Override
	protected Object[] parseValues(Object record) throws DataException {
		return split((String) record, delimiter, quote, false, true);
	}

	public char getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(char delimiter) {
		this.delimiter = delimiter;
	}

	public char getQuote() {
		return quote;
	}

	public void setQuote(char quote) {
		this.quote = quote;
	}

	public static String[] split(String row, char delimiter, char quote, boolean groupQuote, boolean blankAsNull) throws DataException {
		int beginIndex, endIndex;
		List<String> strs = new ArrayList<String>();

		int length = row.length();
		if (length > 0) {
			if (quote != ' ') {
				beginIndex = 0;
				int[] indexs = null;
				do {
					indexs = indexOfField(row, length, beginIndex, delimiter, quote, groupQuote);
					if (blankAsNull && indexs[0] == indexs[1]) {
						strs.add(null);
					} else {
						strs.add(row.substring(indexs[0], indexs[1]));
					}
					beginIndex = indexs[2];
				} while (beginIndex < length);

				if (row.charAt(length - 1) == delimiter) {
					strs.add("");
				}
			} else {
				beginIndex = 0;
				endIndex = row.indexOf(delimiter);
				while (endIndex != -1) {
					strs.add(row.substring(beginIndex, endIndex));
					beginIndex = endIndex + 1;
					endIndex = row.indexOf(delimiter, beginIndex);
				}
				strs.add(row.substring(beginIndex, row.length()));
			}
		}

		String[] result = new String[strs.size()];
		strs.toArray(result);
		return result;
	}

	private static int[] indexOfField(String row, int length, int beginIndex, char delimiter, char quote, boolean groupQuote) throws DataException {
		int[] indexs = new int[3];
		int endIndex = 0;
		int index = beginIndex;
		int j;
		char c;
		// get the first not empty char
		while (true) {
			c = row.charAt(index);
			if (c == ' ') {
				if (++index >= length) {
					indexs[0] = beginIndex;
					indexs[1] = length;
					indexs[2] = length;
					return indexs;
				}

				continue;
			} else {
				break;
			}
		}

		if (c == delimiter) {
			indexs[0] = beginIndex;
			indexs[1] = index;
			indexs[2] = index + 1;
		} else if (c == quote) {
			if (groupQuote) {
				int quoteOffset = index;
				int quoteEndIndex = 0;
				int floor = 1;
				boolean isfieldEnd = false;
				while (!isfieldEnd) {
					do {
						quoteEndIndex = row.indexOf(quote, quoteOffset + 1);
						// not found
						if (quoteEndIndex == -1) {
							quoteEndIndex = length - 1;
							break;
						}
						//
						quoteOffset = quoteEndIndex;
					} while (--floor != 0);

					j = quoteEndIndex;
					// get the first not empty char
					while (true) {
						if (++j >= length) {
							indexs[0] = index;
							indexs[1] = quoteEndIndex + 1;
							indexs[2] = length;
							return indexs;
						}

						c = row.charAt(j);
						if (c == ' ') {
							continue;
						} else {
							break;
						}
					}

					if (c == delimiter) {
						isfieldEnd = true;
						indexs[0] = index;
						indexs[1] = quoteEndIndex + 1;
						indexs[2] = j + 1;
					} else {
						floor = 2;
						quoteOffset = j - 1;
					}
				}
			} else {
				int quoteOffset = index;
				int quoteEndIndex = 0;
				boolean isfieldEnd = false;
				while (!isfieldEnd) {
					quoteEndIndex = row.indexOf(quote, quoteOffset + 1);
					// not found
					if (quoteEndIndex == -1) {
						quoteEndIndex = length - 1;
					}

					j = quoteEndIndex;
					// get the first not empty char
					while (true) {
						if (++j >= length) {
							indexs[0] = index;
							indexs[1] = quoteEndIndex + 1;
							indexs[2] = length;
							return indexs;
						}

						c = row.charAt(j);
						if (c == ' ') {
							continue;
						} else {
							break;
						}
					}

					if (c == delimiter) {
						isfieldEnd = true;
						indexs[0] = index;
						indexs[1] = quoteEndIndex + 1;
						indexs[2] = j + 1;
					} else {
						quoteOffset = j - 1;
					}
				}
			}
		} else {
			endIndex = row.indexOf(delimiter, index + 1);
			if (endIndex != -1) {
				indexs[0] = beginIndex;
				indexs[1] = endIndex;
				indexs[2] = endIndex + 1;
			} else {
				indexs[0] = beginIndex;
				indexs[1] = length;
				indexs[2] = length;
			}
		}

		return indexs;
	}

}
