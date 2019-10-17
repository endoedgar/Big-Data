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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((left == null) ? 0 : left.hashCode());
		result = prime * result + ((right == null) ? 0 : right.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TextPair other = (TextPair) obj;
		if (left == null) {
			if (other.left != null)
				return false;
		} else if (!left.equals(other.left))
			return false;
		if (right == null) {
			if (other.right != null)
				return false;
		} else if (!right.equals(other.right))
			return false;
		return true;
	}
	
	
	
	
}
