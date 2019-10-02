package net.endoedgar.reducers;
import java.util.stream.Collectors;

import net.endoedgar.primitives.KeyValuePair;

public class MyReducer extends BasicReducer<String, Integer> {
	public void reduce() {
		this.getInput().stream()
			.map(o -> 
				new KeyValuePair<String, Integer>(
						o.getKey(), 
						o.getList().stream()
						.collect(Collectors.summingInt(Integer::intValue))
				)
			)
			.forEach(e -> this.emit(e));
	}
	
	public MyReducer(int id) { super(id); }
}
