import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Main {
	public static void main(String[] args) {
		WordCount wc = new WordCount(4, 3);
		wc.splitFileIntoListStrings("test.txt");
		//strings.stream().forEach(System.out::println);
	}
}