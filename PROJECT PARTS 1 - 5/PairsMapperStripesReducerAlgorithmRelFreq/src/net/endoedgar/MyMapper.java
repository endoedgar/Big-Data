package net.endoedgar;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

public class MyMapper extends Mapper<LongWritable, Text, TextPairWritable, IntWritable> {
	private Logger logger = Logger.getLogger(MyMapper.class);
	private Map<TextPairWritable, Integer> H;
	@Override
	protected void setup(Mapper<LongWritable, Text, TextPairWritable, IntWritable>.Context context)
			throws IOException, InterruptedException {
		super.setup(context);
		
		H = new HashMap<TextPairWritable, Integer>();
	}
	
	@Override
	protected void cleanup(Mapper<LongWritable, Text, TextPairWritable, IntWritable>.Context context)
			throws IOException, InterruptedException {
		for(Map.Entry<TextPairWritable, Integer> k : H.entrySet()) {
			logger.info("<" + k.getKey() + ", " + k.getValue() + ">");
			context.write(k.getKey(), new IntWritable(k.getValue()));
		}
		
		super.cleanup(context);
	}

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String line = value.toString();
		String[] tokens = line.split("\\s");
		
		for(int i = 0; i < tokens.length; ++i) {
			Text word = new Text();
			word.set(tokens[i]);
			for(int j = i+1; j < tokens.length; ++j) {
				if(tokens[i].equals(tokens[j]))
					break;
				TextPairWritable wordKey = new TextPairWritable(word, new Text(tokens[j]));
				if(H.containsKey(wordKey)) {
					H.put(wordKey, H.get(wordKey)+1);
				} else {
					H.put(wordKey, 1);
				}
			}
		}
	}
}