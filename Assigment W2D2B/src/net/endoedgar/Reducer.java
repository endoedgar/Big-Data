package net.endoedgar;

import java.util.List;

public interface Reducer<K extends Comparable<?>,V extends Comparable<?>> {
	public int getId();
	
	public void setInput(List<GroupByPair<K, V>> input);
	public List<GroupByPair<K, V>> getInput();
	
	public List<KeyValuePair<K, V>> getOutput();
	public void setOutput(List<KeyValuePair<K, V>> output);
	
	public void receiveFromMapper(List<KeyValuePair<K, V>> mapperOutput);
	public List<KeyValuePair<K, V>> reduce();
}
