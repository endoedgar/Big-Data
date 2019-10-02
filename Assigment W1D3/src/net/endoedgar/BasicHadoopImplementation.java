package net.endoedgar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.endoedgar.mappers.Mapper;
import net.endoedgar.mappers.MyMapper;
import net.endoedgar.primitives.KeyValuePair;
import net.endoedgar.reducers.MyReducer;
import net.endoedgar.reducers.Reducer;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class BasicHadoopImplementation {
	private int r;
	private int m;
	private List<Mapper> mappers;
	private List<Reducer> reducers;
	
	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getM() {
		return m;
	}

	public void setM(int m) {
		this.m = m;
	}

	public BasicHadoopImplementation(int r, int m) {
		super();
		this.r = r;
		this.m = m;
		this.mappers = new ArrayList<Mapper>();
		this.reducers = new ArrayList<Reducer>();
	}
	
	public Map<Integer, List<KeyValuePair<String, Integer>>> shuffleAndSort(List<KeyValuePair<String, Integer>> mapperOutput) {
		return mapperOutput.stream()
		.sorted(Comparator.comparing(KeyValuePair::getKey))
		.collect(Collectors.groupingBy(pair -> getPartition(pair.getKey())));
	}
	
	public int getPartition(String key) {
		return (int) Math.abs(key.hashCode()) % r;
	}
	
	public void setupMappersAndReducers() {
		for(int i = 0; i < this.getM(); ++i) { Mapper m = new MyMapper(i); m.initialize(); mappers.add(m); }
		for(int i = 0; i < this.getR(); ++i) { reducers.add(new MyReducer(i)); }
	}
	
	public void mapAndReduceProcess() {
		for(Mapper m : this.getMappers()) {
			System.out.println("Mapper " +m.getId()+ " Input");
			m.getInput().forEach(System.out::println);
			
			m.map();
			
			System.out.println("Mapper " +m.getId()+ " Output");
			
			m.getOutput().forEach(System.out::println);
		}
		
		for(Mapper m : this.getMappers()) {
			m.close();
		}
		
		// SS
		for(Mapper m : this.getMappers()) {
			Map<Integer, List<KeyValuePair<String, Integer>>> resu = shuffleAndSort(m.getOutput());
			
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
		for(Reducer reducer : this.getReducers()) {
			System.out.println("Reducer " +reducer.getId()+ " Input	");
			reducer.getInput().forEach(System.out::println);
		}
		
		// Show each Reducer Output
		for(Reducer reducer : this.getReducers()) {
			reducer.reduce();
			System.out.println("Reducer " +reducer.getId()+ " Output	");
			reducer.getOutput().forEach(System.out::println);
		}
	}

	public List<Mapper> getMappers() {
		return mappers;
	}

	public List<Reducer> getReducers() {
		return reducers;
	}
}
