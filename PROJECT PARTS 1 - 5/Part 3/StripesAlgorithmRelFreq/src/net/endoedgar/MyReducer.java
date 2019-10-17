package net.endoedgar;

import java.io.IOException;
import java.util.Map.Entry;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Logger;

public class MyReducer extends Reducer<Text, MapWritable, Text, MyMapWritable> {
	private Logger logger = Logger.getLogger(MyReducer.class);
	
	@Override
	protected void setup(Reducer<Text, MapWritable, Text, MyMapWritable>.Context context)
			throws IOException, InterruptedException {
		super.setup(context);
	}
	
	public void reduce(Text key, Iterable<MapWritable> values, Context context) throws IOException, InterruptedException {
		MyMapWritable H = new MyMapWritable();
		String outputLog = "<" + key + ", {";
		int total = 0;
		
		for(MapWritable m : values) {
			for(Entry<Writable, Writable> e : m.entrySet()) {
				total += ((IntWritable)e.getValue()).get();
				if(!H.containsKey(e.getKey())) {
					H.put(e.getKey(), e.getValue());
				}
			}
		}
		
		for(Entry<Writable, Writable> e : H.entrySet()) {
			double value = ((IntWritable)e.getValue()).get()/(double)total;
			outputLog += e.getKey() + ":" + value + " ";
			e.setValue(new DoubleWritable(value));
		}
		outputLog += "}>";
		logger.info(outputLog);
		context.write(key, H);
	}
}
