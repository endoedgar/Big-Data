package net.endoedgar;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

public class MyMapper extends Mapper<LongWritable, Text, TextPairWritable, IntWritable> {
	private Logger logger = Logger.getLogger(MyMapper.class);
	private static final IntWritable one = new IntWritable(1);
	private static final Text token = new Text("*");

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String line = value.toString();
		String[] tokens = line.split("\\s");
		
		for(int i = 0; i < tokens.length; ++i) {
			Text word = new Text();
			word.set(tokens[i]);
			for(int j = i+1; j < tokens.length; ++j) {
				if(tokens[i].equals(tokens[j]))
					break;
				logger.info("<("+ tokens[i]+","+tokens[j]+"), 1>");
				context.write(new TextPairWritable(word, new Text(tokens[j])), one);
				context.write(new TextPairWritable(word, token), one);
			}
		}
	}
}