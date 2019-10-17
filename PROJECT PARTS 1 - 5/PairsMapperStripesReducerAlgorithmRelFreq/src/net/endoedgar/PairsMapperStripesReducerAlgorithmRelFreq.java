package net.endoedgar;
     
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
     
public class PairsMapperStripesReducerAlgorithmRelFreq {
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		@SuppressWarnings("deprecation")
		Job job = new Job(conf, PairsMapperStripesReducerAlgorithmRelFreq.class.getName());
		job.setJarByClass(PairsMapperStripesReducerAlgorithmRelFreq.class);
		
		job.setMapOutputKeyClass(TextPairWritable.class);
		job.setMapOutputValueClass(IntWritable.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(MyMapWritable.class);
		
		job.setMapperClass(MyMapper.class);
		job.setReducerClass(MyReducer.class);
		job.setPartitionerClass(Partitioner.class);
	    job.setNumReduceTasks(3);
		
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		job.waitForCompletion(true);
	}
}