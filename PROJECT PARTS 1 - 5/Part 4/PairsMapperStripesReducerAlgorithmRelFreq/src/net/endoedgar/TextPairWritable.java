package net.endoedgar;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

public class TextPairWritable extends TextPair implements WritableComparable<TextPairWritable> {
	public TextPairWritable() { super(); }
	
	public TextPairWritable(Text left, Text right) {
		super(left, right);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		this.getLeft().write(out);
		this.getRight().write(out);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.getLeft().readFields(in);
		this.getRight().readFields(in);
	}

	@Override
	public int compareTo(TextPairWritable other) {
		int k = this.getLeft().compareTo(other.getLeft());
		if(k != 0) return k;
		return this.getRight().compareTo(other.getRight());
	}

}
