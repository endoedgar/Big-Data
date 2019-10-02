package net.endoedgar.mappers;

import java.util.List;

import net.endoedgar.primitives.KeyValuePair;

public interface Mapper<I extends Comparable<?>, O extends Comparable<?>> {
	public int getId();
	public void initialize();
	public void close();
	public void setInput(List<I> input);
	public void emit(I key, O value);
	public void emit(KeyValuePair<I, O> kv);
	public void map();
	public List<KeyValuePair<I, O>> getOutput();
}
