import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	public static List<KeyValuePair<String, Integer>> splitFileIntoListStrings(String filepath) {
		List<KeyValuePair<String, Integer>> result = new ArrayList<KeyValuePair<String, Integer>>();
		try (Scanner scanner = new Scanner(new File(filepath))) {
		    while (scanner.hasNext()) {
		    	String line = scanner.nextLine();
		    	Pattern p = Pattern.compile("([a-zA-Z0-9]+)");
		    	Matcher m = p.matcher(line);
		    	while(m.find()) {
		    		if(!m.group(1).matches(".*\\d.*"))
		    			result.add(new KeyValuePair<String, Integer>(m.group(1).toLowerCase(),1));
		    	}
		    }
		} catch (FileNotFoundException fnfe) {
		    fnfe.printStackTrace();
		}

		return result;
	}
	public static List<KeyValuePair<String, Integer>> extractListOfWordsFromFile(String filepath) {
		List<KeyValuePair<String, Integer>> strings = splitFileIntoListStrings(filepath);
		strings.sort((i1, i2) -> i1.getT().compareTo(i2.getT()));
		return strings;
	}
	public static void main(String[] args) {
		List<KeyValuePair<String, Integer>> strings = extractListOfWordsFromFile("test.txt");
		strings.stream().forEach(System.out::println);

	}
}
