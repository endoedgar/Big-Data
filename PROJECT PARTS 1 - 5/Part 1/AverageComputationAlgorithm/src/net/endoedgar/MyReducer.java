package net.endoedgar;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Logger;

public class MyReducer extends Reducer<Text, IntWritable, Text, DoubleWritable> {
	private Logger logger = Logger.getLogger(this.getClass());
	
	public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
		double sum = 0.0d;
		int count = 0;
		for (IntWritable val : values) {
			sum += val.get();
			count += 1;
		}
		logger.info("(" + key.toString() + ", " + sum/count + ")");
		context.write(key, new DoubleWritable(sum/count));
	}
}
