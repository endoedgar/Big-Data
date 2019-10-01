import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyReducer {
	static List<GroupByPair<String, List<Integer>>> reduceToGroupPair(Stream<KeyValuePair<String, Integer>> input) {
		return input.collect(
				Collectors.groupingBy(KeyValuePair::getKey)).entrySet().stream()
				.map(pair -> 
					new GroupByPair<String, List<Integer>>(
							pair.getKey(), 
							pair.getValue()
							.stream()
							.map(o -> o.getValue())
							.collect(Collectors.toList())))
							.sorted((i1, i2) -> i1.getKey().compareTo(i2.getKey()))
							.collect(Collectors.toList());
	}
	
	static List<KeyValuePair<String, Integer>> reduceGroupPairToSummedGroupPair(Stream<GroupByPair<String, List<Integer>>> input) {
		return input.map(o -> new KeyValuePair<String, Integer>(
				o.getKey(), 
				o.getList().stream()
				.collect(Collectors.summingInt(Integer::intValue))))
				.collect(Collectors.toList());
	}
}
