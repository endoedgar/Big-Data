package net.endoedgar.mappers;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

public class MyMapper extends BasicMapper<String, List<Integer>> {
	public Map<String, List<Integer>> h;
	
	@Override
	public void initialize() {
		h = new HashMap<String, List<Integer>>();
	}
	
	public void map() {
		this.getInput().stream()
			.flatMap(line -> Pattern.compile("[\" '\\-]|\\.$|\\. ").splitAsStream(line))
			.filter(Pattern.compile("^[a-zA-Z]+$").asPredicate())
			.forEach(e -> {
				String key = e.substring(0, 1).toLowerCase();
				Optional<List<Integer>> count = Optional.ofNullable(h.get(key));
				List<Integer> r = count.orElseGet(() -> {
					List<Integer> a = Arrays.asList(0,0);
					return a;});
				r.set(0, r.get(0)+e.length());
				r.set(1, r.get(1)+1);
				h.put(key, r);
			});
	}
	
	public void close() {
		h.forEach((k,v) -> {
			this.emit(k, v);
		});
	}
	
	public MyMapper(int id) { super(id); }
}
