package net.endoedgar;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

public class MyMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	private Logger logger = Logger.getLogger(MyMapper.class);
	private Map<String, Integer> H;
	
	@Override
	protected void setup(Mapper<LongWritable, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		super.setup(context);
		H = new HashMap<String, Integer>();
	}
	
	@Override
	protected void cleanup(Mapper<LongWritable, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		for (Map.Entry<String,Integer> entry : H.entrySet())  {
			logger.info("(" + entry.getKey() + ", " + entry.getValue() + ")");
			context.write(new Text(entry.getKey()), new IntWritable(entry.getValue())); 
		}
		
		super.cleanup(context);
	}
	
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String line = value.toString();
		StringTokenizer tokenizer = new StringTokenizer(line);
		
		while (tokenizer.hasMoreTokens()) {
			String word = tokenizer.nextToken();
			if(H.containsKey(word)) {
				H.put(word, H.get(word)+1);
			} else
				H.put(word, 1);
		}
	}
}