package net.endoedgar;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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

public class WordCount extends BasicHadoopImplementation {

	public WordCount(int r, int m) {
		super(r, m);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void process(String filepath) {
		System.out.println("Number of Input-Splits: " + this.getM());
		System.out.println("Number of Reducers: " + this.getR());
		try (Stream<String> stream = Files.lines(Paths.get(filepath))) {		
			List<String> lines = stream.collect(Collectors.toList());
			int inputStripSize = (int)(lines.size()/this.getM());
			
			List<Mapper> mappers = new ArrayList<Mapper>();
			List<Reducer> reducers = new ArrayList<Reducer>();
			
			for(int i = 0; i < this.getM(); ++i) { mappers.add(new MyMapper(i)); }
			for(int i = 0; i < this.getR(); ++i) { reducers.add(new MyReducer(i)); }
			
			// input splits for each mapper
			for(Mapper m : mappers) {
				List<String> input = lines.subList(m.getId()*inputStripSize, m.getId()*inputStripSize+inputStripSize);
				System.out.println("Mapper " +m.getId()+ " Input");
				input.forEach(System.out::println);
				
				m.setInput(input);
				
				m.map();
				
				System.out.println("Mapper " +m.getId()+ " Output");
				
				m.getOutput().forEach(System.out::println);
			}
			
			// SS
			for(Mapper m : mappers) {
				Map<Integer, List<KeyValuePair<String, Integer>>> resu = shuffleAndSort(m.getOutput());
				
				IntStream.range(0, this.getR()).boxed()
				.forEach(
					pairIndex -> { 
						System.out.println("Pairs send from Mapper " +m.getId()+ " Reducer "+pairIndex);
						if(resu.containsKey(pairIndex))
							resu.get(pairIndex).forEach((v) -> System.out.println(v));
					}
				);
				
				resu.forEach((k,v) -> reducers.get(k).receiveFromMapper(v));
			}
			
			// Show each Reducer Input
			for(Reducer reducer : reducers) {
				System.out.println("Reducer " +reducer.getId()+ " Input	");
				reducer.getInput().forEach(System.out::println);
			}
			
			// Show each Reducer Output
			for(Reducer reducer : reducers) {
				reducer.reduce();
				System.out.println("Reducer " +reducer.getId()+ " Output	");
				reducer.getOutput().forEach(System.out::println);
			}
		} catch (IOException fnfe) {
		    fnfe.printStackTrace();
		}
	}
	

}
