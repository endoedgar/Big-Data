import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MyMapper {
	private int id;
	private List<String> input;
	private List<KeyValuePair<String, ?>> output;
	
	public MyMapper(int id, List<String> input) {
		super();
		this.id = id;
		this.input = input;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<String> getInput() {
		return input;
	}
	
	public List<KeyValuePair<String, ?>> getOutput() {
		return output;
	}

	public void setInput(List<String> input) {
		this.input = input;
	}

	public List<KeyValuePair<String, ?>> map() {
		this.output = input.stream().flatMap(line -> Pattern.compile("[\" '\\-]|\\.$|\\. ").splitAsStream(line))
		.filter(Pattern.compile("^[a-zA-Z]+$").asPredicate())
		.map(o -> new KeyValuePair<String, Integer>(o.toLowerCase(),1))
		.collect(Collectors.toList());
		return output;
	}
}
