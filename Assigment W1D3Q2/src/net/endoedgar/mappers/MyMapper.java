package net.endoedgar.mappers;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import net.endoedgar.primitives.KeyValuePair;

public class MyMapper extends BasicMapper<String, int[]> {
	public Map<String, int[]> h;
	
	public MyMapper(int id) {
		super(id);
	}
	@Override
	public void initialize() {
		h = new HashMap<String, int[]>();
	}
	public void map() {
		this.getInput().stream()
			.flatMap(line -> Pattern.compile("[\" '\\-]|\\.$|\\. ").splitAsStream(line))
			.filter(Pattern.compile("^[a-zA-Z]+$").asPredicate())
			.forEach(e -> {
				String key = e.substring(0, 1).toLowerCase();
				Optional<int[]> count = Optional.ofNullable(h.get(key));
				int r[] = count.orElseGet(() -> {
					int a[] = new int[2];
					a[0] = 0;
					a[1] = 0;
					return a;});
				r[0] += e.length();
				r[1]++;
				h.put(key, h);
			});
	}
	
	public void close() {
		h.forEach((k,v) -> {
			this.emit(k, v);
		});
	}
}
