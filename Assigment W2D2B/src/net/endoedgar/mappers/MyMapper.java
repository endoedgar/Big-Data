package net.endoedgar.mappers;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.endoedgar.primitives.KeyValuePair;

public class MyMapper extends BasicMapper<String, Integer> {
	public MyMapper(int id) {
		super(id);
	}
	public List<KeyValuePair<String, Integer>> map() {
		this.setOutput(this.getInput().stream().flatMap(line -> Pattern.compile("[\" '\\-]|\\.$|\\. ").splitAsStream(line))
		.filter(Pattern.compile("^[a-zA-Z]+$").asPredicate())
		.map(o -> new KeyValuePair<String, Integer>(o.toLowerCase(),1))
		.collect(Collectors.toList()));
		return this.getOutput();
	}
}
