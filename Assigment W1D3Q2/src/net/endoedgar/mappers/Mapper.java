package net.endoedgar.mappers;

import java.util.List;

import net.endoedgar.primitives.KeyValuePair;

public interface Mapper<I, O> {
	public void initialize();
	public void map();
	public void close();
	
	public void emit(I key, O value);
	public void emit(KeyValuePair<I, O> kv);
	
	public int getId();
	public void setInput(List<I> input);
	public List<I> getInput();
	public List<KeyValuePair<I, O>> getOutput();
}
