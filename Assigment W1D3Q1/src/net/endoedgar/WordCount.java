package net.endoedgar;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.endoedgar.mappers.Mapper;

public class WordCount extends BasicHadoopImplementation {

	public WordCount(int r, int m) {
		super(r, m);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void process(String filepath) {
		System.out.println("Number of Input-Splits: " + this.getM());
		System.out.println("Number of Reducers: " + this.getR());
		this.setupMappersAndReducers();
		try (Stream<String> stream = Files.lines(Paths.get(filepath))) {		
			List<String> lines = stream.collect(Collectors.toList());
			int inputStripSize = (int)(lines.size()/this.getM());
			
			// input splits for each mapper
			for(Mapper m : this.getMappers()) {
				List<String> input = lines.subList(m.getId()*inputStripSize, m.getId()*inputStripSize+inputStripSize);
				m.setInput(input);
			}
			this.mapAndReduceProcess();
		} catch (IOException fnfe) {
		    fnfe.printStackTrace();
		}
	}
	

}
