package net.endoedgar.mappers;

import java.util.List;

import net.endoedgar.primitives.KeyValuePair;

public abstract class BasicMapper<I extends Comparable<?>, O extends Comparable<?>> implements Mapper<I, O> {
	private int id;
	private List<I> input;
	private List<KeyValuePair<I, O>> output;
	
	public int getId() { return id; }
	public List<I> getInput() { return input; }
	public List<KeyValuePair<I, O>> getOutput() { return output; }
	protected void setOutput(List<KeyValuePair<I, O>> output) { this.output = output; }
	public void setInput(List<I> input) { this.input = input; }
	public BasicMapper(int id) { super(); this.id = id; }
}
