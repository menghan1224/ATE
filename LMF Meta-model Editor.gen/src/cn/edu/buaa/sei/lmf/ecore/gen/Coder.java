package cn.edu.buaa.sei.lmf.ecore.gen;

public class Coder {
	
	private final StringBuilder buffer;
	private int indentation;
	
	public Coder(int indentation) {
		this.buffer = new StringBuilder();
		this.indentation = indentation;
	}
	
	public void reset() {
		this.buffer.delete(0, this.buffer.length());
		this.indentation = 0;
	}
	
	public void increaseIndentation() {
		this.indentation++;
	}
	
	public void decreaseIndentation() {
		this.indentation--;
	}
	
	public int getIndentation() {
		return indentation;
	}
	
	public void appendLine(String text) {
		for (int i = 1; i <= this.indentation; i++) {
			buffer.append('\t');
		}
		buffer.append(text);
		buffer.append('\n');
	}
	
	@Override
	public String toString() {
		return buffer.toString();
	}

}
