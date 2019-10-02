import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Main {
	public static List<KeyValuePair<String, Integer>> splitFileIntoListStrings(String filepath) {
		List<KeyValuePair<String, Integer>> result = new ArrayList<KeyValuePair<String, Integer>>();
		try (Stream<String> stream = Files.lines(Paths.get(filepath))) {
			System.out.println("Mapper Output");
		    List<KeyValuePair<String, Integer>> mapperOutput = MyMapper.map(stream);
		    mapperOutput.forEach(System.out::println);

		    List<GroupByPair<String, List<Integer>>> reducerInput = MyReducer.reduceToGroupPair(mapperOutput.stream());
		    System.out.println("Reducer Input");
		    reducerInput.forEach(System.out::println);
		    
		    List<KeyValuePair<String, Integer>> reducerOutput = MyReducer.reduceGroupPairToSummedGroupPair(reducerInput.stream());
		    System.out.println("Reducer Ouput");
		    reducerOutput.forEach(System.out::println);
		} catch (IOException fnfe) {
		    fnfe.printStackTrace();
		}
		return result;
	}
	public static void main(String[] args) {
		List<KeyValuePair<String, Integer>> strings = splitFileIntoListStrings("test.txt");
		strings.stream().forEach(System.out::println);
	}
}