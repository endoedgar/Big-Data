package net.endoedgar.reducers;

import java.util.List;

import net.endoedgar.primitives.GroupByPair;
import net.endoedgar.primitives.KeyValuePair;

public interface Reducer<K extends Comparable<?>,V extends Comparable<?>> {
	public int getId();
	
	public void setInput(List<GroupByPair<K, V>> input);
	public List<GroupByPair<K, V>> getInput();
	public void emit(K key, V value);
	public void emit(KeyValuePair<K, V> kv);
	public List<KeyValuePair<K, V>> getOutput();
	
	public void receiveFromMapper(List<KeyValuePair<K, V>> mapperOutput);
	public void reduce();
}
