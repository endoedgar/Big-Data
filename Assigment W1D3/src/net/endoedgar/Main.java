package net.endoedgar;
public class Main {
	public static void main(String[] args) {
		WordCount wc = new WordCount(4, 3);
		wc.process("test.txt");
	}
}