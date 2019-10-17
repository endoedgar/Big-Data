package net.endoedgar;

import java.io.IOException;
import java.util.Map.Entry;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Logger;

public class MyReducer extends Reducer<Text, MyMapWritable, Text, MyMapWritable> {
	private Logger logger = Logger.getLogger(this.getClass());
	
	public void reduce(Text key, Iterable<MyMapWritable> values, Context context) throws IOException, InterruptedException { 
		MyMapWritable hFinal = new MyMapWritable();
		for (MyMapWritable val : values) {
			for(Entry<Writable, Writable> e : val.entrySet()) {
				Text mapKey = new Text((Text)e.getKey());
				IntWritable mapValue = (IntWritable)e.getValue();
				if(hFinal.containsKey(mapKey)) {
					hFinal.put(mapKey, new IntWritable(((IntWritable)hFinal.get(mapKey)).get()+mapValue.get()));
				} else {
					hFinal.put(mapKey, new IntWritable(mapValue.get()));
				}
			}
		}
		logger.info("(" + key.toString() + ", " + hFinal + ")");
		context.write(key, hFinal);
	}
}
