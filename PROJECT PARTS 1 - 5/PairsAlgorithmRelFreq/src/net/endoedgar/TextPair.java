package net.endoedgar;

import org.apache.hadoop.io.Text;

public class TextPair {
	private Text left;
	private Text right;
	public Text getLeft() {
		return left;
	}
	public void setLeft(Text left) {
		this.left = left;
	}
	public Text getRight() {
		return right;
	}
	public void setRight(Text right) {
		this.right = right;
	}
	public TextPair(Text left, Text right) {
		super();
		this.left = left;
		this.right = right;
	}
	public TextPair() { this(new Text(), new Text()); }
	@Override
	public String toString() {
		return "(" + left.toString() + ", " + right.toString() + ")";
	}
	
}
