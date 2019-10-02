package net.endoedgar.mappers;
import java.util.regex.Pattern;

import net.endoedgar.primitives.KeyValuePair;

public class MyMapper extends BasicMapper<String, Integer> {
	public MyMapper(int id) {
		super(id);
	}
	public void map() {
		this.getInput().stream()
			.flatMap(line -> Pattern.compile("[\" '\\-]|\\.$|\\. ").splitAsStream(line))
			.filter(Pattern.compile("^[a-zA-Z]+$").asPredicate())
			.map(o -> new KeyValuePair<String, Integer>(o.toLowerCase(),1))
			.forEach(e -> this.emit(e));
	}
}
