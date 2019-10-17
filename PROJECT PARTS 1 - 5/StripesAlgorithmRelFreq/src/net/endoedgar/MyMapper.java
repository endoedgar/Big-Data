package net.endoedgar;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

public class MyMapper extends Mapper<LongWritable, Text, Text, MapWritable> {
	private Logger logger = Logger.getLogger(MyMapper.class);
	private Map<String, Map<String, Integer>> H;
	@Override
	protected void setup(Mapper<LongWritable, Text, Text, MapWritable>.Context context)
			throws IOException, InterruptedException {
		super.setup(context);
		
		H = new HashMap<String, Map<String, Integer>>();
	}
	
	@Override
	protected void cleanup(Mapper<LongWritable, Text, Text, MapWritable>.Context context)
			throws IOException, InterruptedException {
		for(Map.Entry<String, Map<String, Integer>> h : H.entrySet()) {
			String outputLog = "<" + h.getKey() + ", {";
			MapWritable mw = new MapWritable();
			for(Map.Entry<String, Integer> k : h.getValue().entrySet()) {
				outputLog += k.getKey() + ":" + k.getValue() + " ";
				mw.put(new Text(k.getKey()), new IntWritable(k.getValue()));
			}
			outputLog += "}>";
			logger.info(outputLog);
			context.write(new Text(h.getKey()), mw);
		}
		
		super.cleanup(context);
	}

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String line = value.toString();
		String[] tokens = line.split("\\s");
		
		for(int i = 0; i < tokens.length; ++i) {
			String word = tokens[i];
			for(int j = i+1; j < tokens.length; ++j) {
				String secondWord = tokens[j];
				if(word.equals(secondWord))
					break;
				if(!H.containsKey(word)) {
					H.put(word, new HashMap<String, Integer>());
				}
				Map<String, Integer> h = H.get(word);
				if(h.containsKey(secondWord)) {
					h.put(secondWord, h.get(secondWord)+1);
				} else {
					h.put(secondWord, 1);
				}
			}
		}
	}
}