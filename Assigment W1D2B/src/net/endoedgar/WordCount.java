package net.endoedgar;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import net.endoedgar.mappers.Mapper;
import net.endoedgar.mappers.MyMapper;
import net.endoedgar.primitives.KeyValuePair;
import net.endoedgar.reducers.MyReducer;
import net.endoedgar.reducers.Reducer;

public class WordCount {
	private int r;
	private int m;
	
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

	public WordCount(int r, int m) {
		super();
		this.r = r;
		this.m = m;
	}
	
	public Map<Integer, List<KeyValuePair<String, Integer>>> shuffleAndSort(List<KeyValuePair<String, Integer>> mapperOutput) {
		return mapperOutput.stream()
		.sorted(Comparator.comparing(KeyValuePair::getKey))
		.collect(Collectors.groupingBy(pair -> getPartition(pair.getKey())));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void process(String filepath) {
		System.out.println("Number of Input-Splits: " + m);
		System.out.println("Number of Reducers: " + r);
		try (Stream<String> stream = Files.lines(Paths.get(filepath))) {		
			List<String> lines = stream.collect(Collectors.toList());
			int inputStripSize = (int)(lines.size()/m);
			
			List<Mapper> mappers = new ArrayList<Mapper>();
			List<Reducer> reducers = new ArrayList<Reducer>();
			
			for(int i = 0; i < m; ++i) { mappers.add(new MyMapper(i)); }
			for(int i = 0; i < r; ++i) { reducers.add(new MyReducer(i)); }
			
			for(Mapper m : mappers) {
				List<String> input = lines.subList(m.getId()*inputStripSize, m.getId()*inputStripSize+inputStripSize);
				System.out.println("Mapper " +m.getId()+ " Input");
				input.forEach(System.out::println);
				
				m.setInput(input);
				
				m.map();
				
				System.out.println("Mapper " +m.getId()+ " Output");
				
				m.getOutput().forEach(System.out::println);
			}
			
			for(Mapper m : mappers) {
				Map<Integer, List<KeyValuePair<String, Integer>>> resu = shuffleAndSort(m.getOutput());
				
				IntStream.range(0, r).boxed()
				.forEach(
					pairIndex -> { 
						System.out.println("Pairs send from Mapper " +m.getId()+ " Reducer "+pairIndex);
						if(resu.containsKey(pairIndex))
							resu.get(pairIndex).forEach((v) -> System.out.println(v));
					}
				);
				
				resu.forEach((k,v) -> reducers.get(k).receiveFromMapper(v));
			}
			
			for(Reducer reducer : reducers) {
				System.out.println("Reducer " +reducer.getId()+ " Input	");
				reducer.getInput().forEach(System.out::println);
			}
			
			for(Reducer reducer : reducers) {
				reducer.reduce();
				System.out.println("Reducer " +reducer.getId()+ " Output	");
				reducer.getOutput().forEach(System.out::println);
			}
		} catch (IOException fnfe) {
		    fnfe.printStackTrace();
		}
	}
	
	public int getPartition(String key) {
		return (int) Math.abs(key.hashCode()) % r;
	}
}
