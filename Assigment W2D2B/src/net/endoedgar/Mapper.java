package net.endoedgar;

import java.util.List;

public interface Mapper<I extends Comparable<?>, O extends Comparable<?>> {
	public int getId();
	public void setInput(List<I> input);
	public List<KeyValuePair<I, O>> map();
	public List<KeyValuePair<I, O>> getOutput();
}
