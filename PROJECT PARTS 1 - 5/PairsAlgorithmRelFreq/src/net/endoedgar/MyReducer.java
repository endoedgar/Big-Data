package net.endoedgar;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Logger;

public class MyReducer extends Reducer<TextPairWritable, IntWritable, TextPairWritable, IntWritable> {
	private Logger logger = Logger.getLogger(MyReducer.class);
	
	public void reduce(TextPairWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
		int sum = 0;
		
		for (IntWritable val : values) {
			sum += val.get();
		}
		logger.info("<("+ key.toString() +"), " + sum + ">");
		context.write(key, new IntWritable(sum));
	}
}
