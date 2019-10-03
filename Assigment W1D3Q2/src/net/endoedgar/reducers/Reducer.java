package net.endoedgar.reducers;

import java.util.List;

import net.endoedgar.primitives.GroupByPair;
import net.endoedgar.primitives.KeyValuePair;

public interface Reducer<K extends Comparable<?>,V, O> {
	public int getId();
	
	public void setInput(List<GroupByPair<K, V>> input);
	public List<GroupByPair<K, V>> getInput();
	public void emit(K key, O value);
	public void emit(KeyValuePair<K, O> kv);
	public List<KeyValuePair<K, O>> getOutput();
	
	public void receiveFromMapper(List<KeyValuePair<K, V>> mapperOutput);
	public void reduce();
}
