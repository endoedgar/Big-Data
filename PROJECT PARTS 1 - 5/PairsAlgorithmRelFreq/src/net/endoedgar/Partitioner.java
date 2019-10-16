package net.endoedgar;

import org.apache.hadoop.io.IntWritable;

public class Partitioner extends org.apache.hadoop.mapreduce.Partitioner<TextPairWritable, IntWritable> {

	@Override
	public int getPartition(TextPairWritable key, IntWritable value, int numPartitions) {
		return Math.abs(key.getLeft().hashCode())%numPartitions;
	}

}
