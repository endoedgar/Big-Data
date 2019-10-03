package net.endoedgar.reducers;

import java.util.List;

import net.endoedgar.primitives.GroupByPair;
import net.endoedgar.primitives.KeyValuePair;

public interface Reducer<K,V, O> {
	public void receiveFromMapper(List<KeyValuePair<K, V>> mapperOutput);
	public void reduce();
	
	public void emit(K key, O value);
	public void emit(KeyValuePair<K, O> kv);
	
	public int getId();
	public List<GroupByPair<K, V>> getInput();
	public List<KeyValuePair<K, O>> getOutput();
}
