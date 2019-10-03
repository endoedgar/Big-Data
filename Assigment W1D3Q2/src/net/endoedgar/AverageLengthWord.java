package net.endoedgar;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.endoedgar.mappers.MyMapper;
import net.endoedgar.reducers.MyReducer;

public class AverageLengthWord extends BasicHadoopImplementation<String, List<Integer>, Double> {
	private List<String> files;
	
	public AverageLengthWord(List<String> files, int r) {
		super(r, files.size());
		this.files = files;
	}

	public void process() {
		System.out.println("Number of Input-Splits: " + this.getM());
		System.out.println("Number of Reducers: " + this.getR());
		this.setupMappersAndReducers(i -> new MyMapper((int) i), i -> new MyReducer((int) i));
		for(int i = 0; i < this.getM(); ++i) {
			try (Stream<String> list = Files.lines(Paths.get(files.get(i)))) {
				this.getMappers().get(i).setInput(list.collect(Collectors.toList()));
			} catch (IOException fnfe) {
				fnfe.printStackTrace();
			}
		}
		this.mapAndReduceProcess();
	}
	

}
