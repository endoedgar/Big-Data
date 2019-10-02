import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class MyReducer {
	private List<GroupByPair<String, Integer>> input = new ArrayList<GroupByPair<String, Integer>>();
	private List<KeyValuePair<String, Integer>> output;
	private int id;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<GroupByPair<String, Integer>> getInput() {
		return input;
	}
	public void setInput(List<GroupByPair<String, Integer>> input) {
		this.input = input;
	}
	public List<KeyValuePair<String, Integer>> getOutput() {
		return output;
	}
	public void setOutput(List<KeyValuePair<String, Integer>> output) {
		this.output = output;
	}
	public void receiveFromMapper(List<KeyValuePair<String, Integer>> mapperOutput) {
		List<GroupByPair<String, Integer>> addedList = MyReducer.reduceToGroupPair(mapperOutput);
		this.input = Stream.concat(input.stream(), addedList.stream())
				.collect(
						Collectors.groupingBy(
								GroupByPair::getKey,
								Collectors.flatMapping(p -> p.getList().stream(), Collectors.toList())
								
						)
				).entrySet().stream()
				.map(e -> new GroupByPair<String, Integer>(e.getKey(), e.getValue()))
				.sorted((e1,e2) -> e1.getKey().compareTo(e2.getKey()))
				.collect(Collectors.toList());
	}
	
	private static List<GroupByPair<String, Integer>> reduceToGroupPair(List<KeyValuePair<String, Integer>> input) {
		return input.stream().collect(
				Collectors.groupingBy(KeyValuePair::getKey)).entrySet().stream()
				.map(pair -> 
					new GroupByPair<String, Integer>(
							pair.getKey(), 
							pair.getValue()
							.stream()
							.map(o -> o.getValue())
							.collect(Collectors.toList())))
							.sorted((i1, i2) -> i1.getKey().compareTo(i2.getKey()))
							.collect(Collectors.toList());
	}
	
	private static List<KeyValuePair<String, Integer>> reduceGroupPairToSummedGroupPair(List<GroupByPair<String, Integer>> input) {
		return input.stream().map(o -> new KeyValuePair<String, Integer>(
				o.getKey(), 
				o.getList().stream()
				.collect(Collectors.summingInt(Integer::intValue))))
				.collect(Collectors.toList());
	}
	public MyReducer(int id) {
		super();
		this.id = id;
	}
	
	public void reduce() {
		this.output = reduceGroupPairToSummedGroupPair(input);
	}
}
