package net.endoedgar;

import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;

public class Partitioner extends org.apache.hadoop.mapreduce.Partitioner<Text, MapWritable> {

	@Override
	public int getPartition(Text key, MapWritable value, int numPartitions) {
		return Math.abs(key.hashCode())%numPartitions;
	}

}
