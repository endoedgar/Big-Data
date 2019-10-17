package net.endoedgar;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Logger;

public class MyReducer extends Reducer<Text, IntArrayWritable, Text, DoubleWritable> {
	private Logger logger = Logger.getLogger(this.getClass());
	
	public void reduce(Text key, Iterable<IntArrayWritable> values, Context context) throws IOException, InterruptedException {
		double sum = 0.0d;
		int count = 0;
		for (IntArrayWritable val : values) {
			sum += ((IntWritable)val.get()[0]).get();
			count += ((IntWritable)val.get()[1]).get();
		}
		logger.info("(" + key.toString() + ", " + sum/count + ")");
		context.write(key, new DoubleWritable(sum/count));
	}
}
