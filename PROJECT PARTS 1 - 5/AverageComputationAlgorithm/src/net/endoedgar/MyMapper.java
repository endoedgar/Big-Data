package net.endoedgar;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

public class MyMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Override
	protected void setup(Mapper<LongWritable, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		super.setup(context);
	}
	
	@Override
	protected void cleanup(Mapper<LongWritable, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		super.cleanup(context);
	}
	
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String line = value.toString();
		String[] elems = line.split(" ");
		assert(elems.length > 1);
		logger.info("(" + elems[0] + ", " + elems[elems.length-1] + ")");
		int size;
		try {
			size = Integer.parseInt(elems[elems.length-1]);
		} catch (NumberFormatException e) {
			size = 0;
		}
		context.write(new Text(elems[0]), new IntWritable(size));
	}
}