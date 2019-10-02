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

	public List<KeyValuePair<String, Integer>> splitFileIntoListStrings(String filepath) {
		List<KeyValuePair<String, Integer>> result = new ArrayList<KeyValuePair<String, Integer>>();
		try (Stream<String> stream = Files.lines(Paths.get(filepath))) {			
			List<String> lines = stream.collect(Collectors.toList());
			int inputStripSize = (int)(lines.size()/m);
			
			List<MyMapper> mappers = new ArrayList<MyMapper>();
			List<MyReducer> reducers = new ArrayList<MyReducer>();
			
			for(int i = 0; i < m; ++i) {
				List<String> input = lines.subList(i*inputStripSize, i*inputStripSize+inputStripSize);
				System.out.println("Mapper " +i+ " Input");
				
				input.forEach(System.out::println);
				
				mappers.add(new MyMapper(i, input));
			}
			
			for(int i = 0; i < r; ++i) {
				reducers.add(new MyReducer(i));
			}
			
			for(MyMapper m : mappers) {
				m.map();
				
				System.out.println("Mapper " +m.getId()+ " Output");
				
				m.getOutput().forEach(System.out::println);
			}
			
			for(MyMapper m : mappers) {
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
			
			for(MyReducer reducer : reducers) {
				System.out.println("Reducer " +reducer.getId()+ " Input	");
				reducer.getInput().forEach(System.out::println);
			}
			
			for(MyReducer reducer : reducers) {
				reducer.reduce();
				System.out.println("Reducer " +reducer.getId()+ " Output	");
				reducer.getOutput().forEach(System.out::println);
			}
		} catch (IOException fnfe) {
		    fnfe.printStackTrace();
		}
		return result;
	}
	
	public int getPartition(String key) {
		return (int) Math.abs(key.hashCode()) % r;
	}
}
