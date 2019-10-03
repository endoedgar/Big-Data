package net.endoedgar.reducers;
import java.util.Arrays;
import java.util.List;

import net.endoedgar.primitives.KeyValuePair;

public class MyReducer extends BasicReducer<String, List<Integer>, Double> {
	public void reduce() {
		this.getInput().stream()
			.map(o -> 
				new KeyValuePair<String, Double>(
						o.getKey(), 
						o.getList().stream()
						.reduce((e1,e2) -> Arrays.asList(e1.get(0)+e2.get(0), e1.get(1)+e2.get(1)))
						.map(e -> e.get(0)/Double.valueOf(e.get(1)))
						.orElse(0.0d)
				)
			).forEach(e -> this.emit(e));
	}
	
	public MyReducer(int id) { super(id); }
}
