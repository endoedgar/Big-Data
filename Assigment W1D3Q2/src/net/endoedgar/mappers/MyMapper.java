package net.endoedgar.mappers;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import net.endoedgar.primitives.KeyValuePair;

public class MyMapper extends BasicMapper<String, Integer> {
	public Map<String, Integer> h;
	
	public MyMapper(int id) {
		super(id);
	}
	@Override
	public void initialize() {
		h = new HashMap<String, Integer>();
	}
	public void map() {
		this.getInput().stream()
			.flatMap(line -> Pattern.compile("[\" '\\-]|\\.$|\\. ").splitAsStream(line))
			.filter(Pattern.compile("^[a-zA-Z]+$").asPredicate())
			.map(o -> new KeyValuePair<String, Integer>(o.toLowerCase(),1))
			.forEach(e -> {
				Optional<Integer> count = Optional.ofNullable(h.get(e.getKey()));
				h.put(e.getKey(), count.orElse(0)+1);
			});
	}
	
	public void close() {
		h.forEach((k,v) -> {
			this.emit(k, v);
		});
	}
}
