package com.amituofo.datatable.impl.txt;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TxtLineReader {
	private InputStreamReader streamReader;
	private String charset;
	
	final int BOM_SIZE = 4;
	final byte[] bombuf = new byte[BOM_SIZE];
	byte bomsize = 0;
	
	final int LASTBUF_SIZE = 2000;
	final char[] lastbuf = new char[LASTBUF_SIZE];
	int lastbufLength = 0;
	final int BUF_SIZE = 2000;
	char[] buf = new char[BUF_SIZE];
	int bufLength = 0;

	// boolean bomIncluded = false;

	public TxtLineReader(InputStream inStream, String charset) throws IOException {
		detectBom(inStream);
		this.streamReader = new InputStreamReader(inStream, charset);

		int j = 0;
		for (int i = bomsize; i < BOM_SIZE; i++) {
			lastbuf[j++] = (char) bombuf[i];
		}
		lastbufLength = BOM_SIZE - bomsize;
	}
	
	private String getLine(char[] buffer, int length, char[] toBuffer) {
		StringBuilder sb = new StringBuilder();

		int indexLF = -1;
		for (int i = 0; i < length; i++) {
			char c = buffer[i];

			if (c == '\r') {
				continue;
			} else if (c == '\n') {
				length = length - i;
				System.arraycopy(buffer, i+1, toBuffer, 0, length);
				return sb.toString();
			} else {
				sb.append((char) c);
			}
		}
		
		return sb.toString();
	}
	
	public String readLine() throws IOException {
		StringBuilder sb = new StringBuilder();

		int indexLF = -1;
		for (int i = 0; i < lastbufLength; i++) {
			char c = lastbuf[i];

			if (c == '\r') {
				continue;
			} else if (c == '\n') {
				lastbufLength = lastbufLength - i;
				System.arraycopy(lastbuf, i+1, lastbuf, 0, lastbufLength);
				return sb.toString();
			} else {
				sb.append((char) c);
			}
		}

		int readcnt = -1;
		do {
			readcnt = streamReader.read(buf);
			if (readcnt == -1) {
				if (lastbufLength != 0) {
					buf = lastbuf;
					readcnt = lastbufLength;
					// System.arraycopy(lastbufLength, 0, buf, 0, lastbufLength);
				} else {
					if (sb.length() == 0) {
						return null;
					} else {
						return sb.toString();
					}
				}
			}

			for (int i = 0; i < readcnt; i++) {
				char c = buf[i];

				if (c == '\r') {
					continue;
				} else if (c == '\n') {
					lastbufLength = readcnt - i;
					System.arraycopy(buf, i+1, lastbuf, 0, lastbufLength);
					return sb.toString();
				} else {
					sb.append((char) c);
				}
			}
		} while (true);
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

	public static void main(String[] arg) throws FileNotFoundException, IOException {
		TxtLineReader r = new TxtLineReader(new FileInputStream("D:\\WORKSPACE3.7\\AMTF_Datafile3\\Test_Data\\GCMPart.csv"), "utf-8");
		String row = r.readLine();
		while (row != null) {
			System.out.println(row);
			row = r.readLine();
		}
	}
}
