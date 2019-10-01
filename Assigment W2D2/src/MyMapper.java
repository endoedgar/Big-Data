import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyMapper {
	static List<KeyValuePair<String, Integer>> map(Stream<String> input) {
		return input.flatMap(line -> Pattern.compile("[ -\\._]").splitAsStream(line))
		.filter(Pattern.compile("^[a-zA-Z]+$").asPredicate())
		.map(o -> new KeyValuePair<String, Integer>(o.toLowerCase(),1))
		.sorted((i1, i2) -> i1.getKey().compareTo(i2.getKey()))
		.collect(Collectors.toList());
	}
}
