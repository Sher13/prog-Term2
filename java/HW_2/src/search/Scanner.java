package search;

import java.lang.*;
import java.io.*;
import java.util.*;
import java.io.InputStream;
import java.util.Arrays;

public class Scanner {
    private BufferedReader in ;
    private String line = "";
    private int start = 0;
    private boolean end = false;
    public Scanner(String file) throws IOException {
        try {
            this.in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(file)), "utf8"), 1024);
            this._read();
        } catch (FileNotFoundException e) {
            this.in = new BufferedReader(new StringReader(file), 1024);
            this._read();
        }
    }
    public Scanner(InputStream sourсe) throws IOException {
        this.in = new BufferedReader(new InputStreamReader(sourсe), 1024);
        this._read();
    }
    public void close() throws IOException {
        this.in.close();
    }
	private boolean prov(char c) {
        if (Character.isLetter(c) || (Character.DASH_PUNCTUATION == Character.getType(c)) || (c == '\''))
            return true;
        return false;
    }
    private void _read() throws IOException {
        StringBuilder str = new StringBuilder();
        this.start = 0;
        while (true) {
            int x = in.read();
            if (x != -1) {
                if ((char) x != '\n') {
                    str.append((char) x);
                } else {
                    this.line = str.toString();
                    break;
                }
            } else {
                this.end = true;
                this.line = str.toString();
                break;
            }
        }
    }
    private boolean _delSpace() throws IOException {
        while (this.start >= this.line.length()) {
            if (this.end == true) {
                return false;
            } else
                this._read();
        }
        while (Character.isWhitespace(this.line.charAt(this.start))) {
            this.start++;
            while (this.start >= this.line.length()) {
                if (this.end == true) {
                    return false;
                } else
                    this._read();
            }
        }
        return true;
    }
	private boolean _delAll() throws IOException {
		while(this.start < this.line.length() && prov(this.line.charAt(this.start)) == false) {
			this.start++;
		}
		if (this.start >= this.line.length())
			return false;
		return true;
	}
    public boolean hasNextInt() throws IOException {
        int st = this.start;
        if (this._delSpace() == false) {
            return false;
        }
        if (Character.isDigit(this.line.charAt(this.start)) || (this.line.charAt(this.start) == '-' && this.start + 1 < this.line.length() && Character.isDigit(this.line.charAt(this.start + 1)))) {
            this.start = st;
            return true;
        } else {
            this.start = st;
            return false;
        }
    }
    public int nextInt() throws IOException {
        this._delSpace();
        int fin = this.start;
        int fl = 1;
        if (this.line.charAt(fin) == '-') {
            fl = -1;
            fin++;
            this.start++;
        }
        while (fin < this.line.length() && Character.isDigit(this.line.charAt(fin))) {
            fin++;
        }
        int x = Integer.parseUnsignedInt(this.line.substring(this.start, fin), 10) * fl;
        this.start = fin;
        return x;
    }
    public String nextLine() throws IOException {
        String s = this.line;
        this._read();
        return s;
    }
    public boolean hasNextLine() throws IOException {
        while (this.start >= this.line.length()) {
            if (this.end == true) {
                return false;
            } else
                this._read();
        }
        return true;
    }
	public String nextWord() throws IOException {
		boolean x = this._delAll();
		if (x == false || this.start >= this.line.length()) {
			return "";
		}
        StringBuilder str = new StringBuilder();
		while(this.start < this.line.length() && prov(this.line.charAt(this.start))) {
			str.append(line.charAt(this.start));
			this.start++;
		}
		return str.toString();
	}
    public boolean hasNext() throws IOException {
        if (this.start >= this.line.length()) {
			return false;
		}
		return true;
    }
}