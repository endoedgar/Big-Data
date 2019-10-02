package net.endoedgar.reducers;
import java.util.List;
import java.util.stream.Collectors;

import net.endoedgar.primitives.KeyValuePair;

public class MyReducer extends BasicReducer<String, Integer> {
	public List<KeyValuePair<String, Integer>> reduce() {
		this.setOutput(this.getInput().stream().map(o -> new KeyValuePair<String, Integer>(
				o.getKey(), 
				o.getList().stream()
				.collect(Collectors.summingInt(Integer::intValue))))
				.collect(Collectors.toList()));
		return this.getOutput();
	}
	
	public MyReducer(int id) { super(id); }
}
