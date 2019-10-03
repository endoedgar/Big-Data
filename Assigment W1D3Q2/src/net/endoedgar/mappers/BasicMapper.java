package net.endoedgar.mappers;

import java.util.ArrayList;
import java.util.List;

import net.endoedgar.primitives.KeyValuePair;

public abstract class BasicMapper<I, O> implements Mapper<I, O> {
	private int id;
	private List<I> input;
	private List<KeyValuePair<I, O>> output;
	
	public int getId() { return id; }
	public List<I> getInput() { return input; }
	public List<KeyValuePair<I, O>> getOutput() { return output; }
	public void setInput(List<I> input) { this.input = input; }
	public BasicMapper(int id) { super(); this.output = new ArrayList<KeyValuePair<I, O>>(); this.id = id; }
	public void emit(KeyValuePair<I, O> kv) { output.add(kv); }
	public void emit(I key, O value) { this.emit(new KeyValuePair<I,O>(key, value)); }
	public void initialize() {}
	public void close() {}
}
