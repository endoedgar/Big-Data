package net.endoedgar;

import java.util.Arrays;

public class Main {
	public static void main(String[] args) {
		AverageLengthWord wc = new AverageLengthWord(Arrays.asList("File1.txt", "File2.txt", "File3.txt", "File4.txt"), 3);
		wc.process();
	}
}