package net.endoedgar;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.endoedgar.primitives.KeyValuePair;

public class BasicHadoopImplementation {
	private int r;
	private int m;
	
	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getM() {
		return m;
	}

	public void setM(int m) {
		this.m = m;
	}

	public BasicHadoopImplementation(int r, int m) {
		super();
		this.r = r;
		this.m = m;
	}
	
	public Map<Integer, List<KeyValuePair<String, Integer>>> shuffleAndSort(List<KeyValuePair<String, Integer>> mapperOutput) {
		return mapperOutput.stream()
		.sorted(Comparator.comparing(KeyValuePair::getKey))
		.collect(Collectors.groupingBy(pair -> getPartition(pair.getKey())));
	}
	
	public int getPartition(String key) {
		return (int) Math.abs(key.hashCode()) % r;
	}
}
