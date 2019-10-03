package net.endoedgar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.endoedgar.mappers.Mapper;
import net.endoedgar.primitives.KeyValuePair;
import net.endoedgar.reducers.Reducer;

public class BasicHadoopImplementation<K extends Comparable<K>, V, O> {
	private int r;
	private int m;
	private List<Mapper<K, V>> mappers;
	private List<Reducer<K, V, O>> reducers;
	
	public Map<Integer, List<KeyValuePair<K, V>>> shuffleAndSort(List<KeyValuePair<K, V>> mapperOutput) {
		return mapperOutput.stream()
		.sorted(Comparator.comparing(KeyValuePair<K,V>::getKey))
		.collect(Collectors.groupingBy(pair -> getPartition(pair.getKey())));
	}
	
	public int getPartition(Object key) {
		return (int) Math.abs(key.hashCode()) % r;
	}
	
	public void setupMappersAndReducers(Function<Integer, Mapper<K,V>> mapperFunc, Function<Integer, Reducer<K,V,O>> reducerFunc) {
		for(int i = 0; i < this.getM(); ++i) { Mapper<K,V> m = mapperFunc.apply(i); m.initialize(); mappers.add(m); }
		for(int i = 0; i < this.getR(); ++i) { reducers.add(reducerFunc.apply(i)); }
	}
	
	public void mapAndReduceProcess() {
		// Show each Mapper input
		// And do map
		for(Mapper<K,V> m : this.getMappers()) {
			System.out.println("Mapper " +m.getId()+ " Input");
			m.getInput().forEach(System.out::println);
			
			m.map();
		}
		
		// Close each Mapper
		// And show the output
		for(Mapper<K,V> m : this.getMappers()) {
			m.close();
			
			System.out.println("Mapper " +m.getId()+ " Output");
			
			m.getOutput().forEach(System.out::println);
		}
		
		// Shuffle and Sort
		for(Mapper<K,V> m : this.getMappers()) {
			Map<Integer, List<KeyValuePair<K, V>>> resu = shuffleAndSort(m.getOutput());
			
			IntStream.range(0, this.getR()).boxed()
			.forEach(
				pairIndex -> { 
					System.out.println("Pairs send from Mapper " +m.getId()+ " Reducer "+pairIndex);
					if(resu.containsKey(pairIndex))
						resu.get(pairIndex).forEach((v) -> System.out.println(v));
				}
			);
			
			resu.forEach((k,v) -> this.getReducers().get(k).receiveFromMapper(v));
		}
		
		// Show each Reducer Input
		for(Reducer<K,V,O> reducer : this.getReducers()) {
			System.out.println("Reducer " +reducer.getId()+ " Input	");
			reducer.getInput().forEach(System.out::println);
		}
		
		// Do reduce and
		// Show each Reducer Output
		for(Reducer<K,V,O> reducer : this.getReducers()) {
			reducer.reduce();
			System.out.println("Reducer " +reducer.getId()+ " Output	");
			reducer.getOutput().forEach(System.out::println);
		}
	}

	public List<Mapper<K,V>> getMappers() { return mappers; }
	public List<Reducer<K,V,O>> getReducers() { return reducers; }
	public int getR() { return r; }
	public int getM() { return m; }

	public BasicHadoopImplementation(int r, int m) {
		super();
		this.r = r;
		this.m = m;
		this.mappers = new ArrayList<Mapper<K,V>>();
		this.reducers = new ArrayList<Reducer<K,V,O>>();
	}
}
