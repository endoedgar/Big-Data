package net.endoedgar;

import java.io.IOException;
import java.util.Map.Entry;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Logger;

public class MyReducer extends Reducer<TextPairWritable, IntWritable, Text, MyMapWritable> {
	private Logger logger = Logger.getLogger(MyReducer.class);
	private MyMapWritable H;
	private Text uprev;
	
	@Override
	protected void setup(Reducer<TextPairWritable, IntWritable, Text, MyMapWritable>.Context context)
			throws IOException, InterruptedException {
		super.setup(context);
		H = new MyMapWritable();
		uprev = null;
	}
	
	private void sendStoredH(Context context) throws IOException, InterruptedException {
		int total = 0;
		for(Entry<Writable, Writable> e : H.entrySet()) {
			total += ((IntWritable)e.getValue()).get();
		}
		for(Entry<Writable, Writable> e : H.entrySet()) {
			e.setValue(new DoubleWritable(((IntWritable)e.getValue()).get()/(double)total));
		}
		logger.info("<("+ uprev +"), " + H + ">");
		context.write(uprev, H);
	}
	
	public void reduce(TextPairWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
		if(!key.getLeft().equals(uprev) && uprev != null) {
			this.sendStoredH(context);
			H = new MyMapWritable();
		}
		int sum = 0;
		
		for(IntWritable e : values) {
			sum += e.get();
		}
		H.put(new Text(key.getRight()), new IntWritable(sum));
		uprev = new Text(key.getLeft());
	}
	
	@Override
	protected void cleanup(Reducer<TextPairWritable, IntWritable, Text, MyMapWritable>.Context context)
			throws IOException, InterruptedException {
		this.sendStoredH(context);
		super.cleanup(context);
	}
}
