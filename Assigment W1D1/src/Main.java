import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
	public static List<KeyValuePair<String, Integer>> splitFileIntoListStrings(String filepath) {
		List<KeyValuePair<String, Integer>> result = new ArrayList<KeyValuePair<String, Integer>>();
		try (Stream<String> stream = Files.lines(Paths.get(filepath))) {
		    result = stream
		    		.flatMap(line -> Pattern.compile("[ -\\._]").splitAsStream(line))
		    		.filter(Pattern.compile("^[a-zA-Z]+$").asPredicate())
		    		.map(o -> new KeyValuePair<String, Integer>(o.toLowerCase(),1))
		    		.sorted((i1, i2) -> i1.getKey().compareTo(i2.getKey()))
		    		.collect(Collectors.toList());
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