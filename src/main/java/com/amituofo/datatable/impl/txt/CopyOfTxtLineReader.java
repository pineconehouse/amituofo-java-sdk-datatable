package com.amituofo.datatable.impl.txt;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CopyOfTxtLineReader {
	private InputStreamReader streamReader;
	private String charset;
	final int BOM_SIZE = 4;
	final byte[] bombuf = new byte[BOM_SIZE];
	// final int BUF_SIZE = 1000;
	// final byte[] buf = new byte[BUF_SIZE];
	byte bomsize = 0;
	boolean bomIncluded = false;

	public CopyOfTxtLineReader(InputStream inStream, String charset) throws IOException {
		detectBom(inStream);
		this.streamReader = new InputStreamReader(inStream, charset);
	}

	public String readLine() throws IOException {
		StringBuilder sb = new StringBuilder(100);
		if (!bomIncluded) {
			for (int i = bomsize; i < BOM_SIZE; i++) {
				sb.append((char) bombuf[i]);
			}
			bomIncluded = true;
		}

		int c = -1;
		do {
			c = streamReader.read();
			if (c == -1) {
				if (sb.length() == 0) {
					return null;
				} else {
					return sb.toString();
				}
			} else if (c == '\r') {
				continue;
			} else if (c == '\n') {
				break;
			} else {
				sb.append((char) c);
			}
		} while (true);

		return sb.toString();
	}

	public String getCharset() {
		return charset;
	}

	public void close() throws IOException {
		if (streamReader != null) {
			streamReader.close();
			streamReader = null;
		}
	}

	private byte[] detectBom(InputStream inStream) throws IOException {

		inStream.read(bombuf, 0, bombuf.length);

		if ((bombuf[0] == -17) && (bombuf[1] == -69) && (bombuf[2] == -65)) {
			this.charset = ("UTF-8");
			bomsize = 3;
		} else if ((bombuf[0] == 0) && (bombuf[1] == 0) && (bombuf[2] == -2) && (bombuf[3] == -1)) {
			this.charset = ("UTF-32BE");
			bomsize = 4;
		} else if ((bombuf[0] == -1) && (bombuf[1] == -2) && (bombuf[2] == 0) && (bombuf[3] == 0)) {
			this.charset = ("UTF-32LE");
			bomsize = 4;
		} else if ((bombuf[0] == -2) && (bombuf[1] == -1)) {
			this.charset = ("UTF-16BE");
			bomsize = 2;
		} else if ((bombuf[0] == -1) && (bombuf[1] == -2)) {
			this.charset = ("UTF-16LE");
			bomsize = 2;
		} else {
			bomsize = 0;
		}

		return bombuf;
	}
}
