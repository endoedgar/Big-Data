package net.endoedgar.reducers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.endoedgar.primitives.GroupByPair;
import net.endoedgar.primitives.KeyValuePair;

public abstract class BasicReducer<K extends Comparable<K>,V, O> implements Reducer<K, V, O> {
	private List<GroupByPair<K, V>> input;
	private List<KeyValuePair<K, O>> output;
	private int id;
	
	public void receiveFromMapper(List<KeyValuePair<K, V>> mapperOutput) {
		List<GroupByPair<K, V>> addedList = this.reduceToGroupPair(mapperOutput);
		this.input = 
				Stream.concat(this.getInput().stream(), addedList.stream())
				.collect(
						Collectors.groupingBy(
								GroupByPair::getKey,
								Collectors.flatMapping(p -> p.getList().stream(), Collectors.toList())
								
						)
				).entrySet().stream()
				.map(e -> new GroupByPair<K, V>(e.getKey(), e.getValue()))
				.sorted((e1,e2) -> e1.getKey().compareTo(e2.getKey()))
				.collect(Collectors.toList());
	}
	
	private List<GroupByPair<K, V>> reduceToGroupPair(List<KeyValuePair<K, V>> input) {
		return input.stream().collect(
				Collectors.groupingBy(KeyValuePair::getKey)).entrySet().stream()
				.map(pair -> 
					new GroupByPair<K, V>(
							pair.getKey(), 
							pair.getValue()
								.stream()
								.map(o -> o.getValue())
								.collect(Collectors.toList()))
				).sorted((i1, i2) -> i1.getKey().compareTo(i2.getKey()))
				.collect(Collectors.toList());
	}
	
	public void initialize() {};
	public void close() {};
	public void emit(KeyValuePair<K, O> kv) { output.add(kv); }
	public void emit(K key, O value) { this.emit(new KeyValuePair<K,O>(key, value)); }
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public List<GroupByPair<K, V>> getInput() { return input; }
	public List<KeyValuePair<K, O>> getOutput() { return output; }
	public BasicReducer(int id) { super(); this.input = new ArrayList<GroupByPair<K, V>>(); this.output = new ArrayList<KeyValuePair<K, O>>(); this.id = id; }
}
