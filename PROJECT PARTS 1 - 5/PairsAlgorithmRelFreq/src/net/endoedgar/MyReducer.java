package net.endoedgar;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Logger;

public class MyReducer extends Reducer<TextPairWritable, IntWritable, TextPairWritable, DoubleWritable> {
	private Logger logger = Logger.getLogger(MyReducer.class);
	private static final Text token = new Text("*");
	private int groupSum = 0;
	
	public void reduce(TextPairWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
		int sum = 0;
		
		for (IntWritable val : values) {
			sum += val.get();
		}
		
		if(key.getRight().equals(token)) {
			groupSum = sum;
		} else {
			logger.info("<("+ key.toString() +"), " + sum + ">");
			context.write(key, new DoubleWritable(sum/(double)groupSum));
		}
	}
}
