package net.endoedgar;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

public class MyMapper extends Mapper<LongWritable, Text, Text, IntArrayWritable> {
	private Logger logger = Logger.getLogger(this.getClass());
	private Map<String, Integer[]> H; // map key to int size, int count 
	@Override
	protected void setup(Mapper<LongWritable, Text, Text, IntArrayWritable>.Context context)
			throws IOException, InterruptedException {
		super.setup(context);
		H = new HashMap<String, Integer[]>();
	}
	
	@Override
	protected void cleanup(Mapper<LongWritable, Text, Text, IntArrayWritable>.Context context)
			throws IOException, InterruptedException {
		for (Map.Entry<String,Integer[]> entry : H.entrySet())  {
			logger.info("(" + entry.getKey() + ", [" + entry.getValue()[0] + ", " + entry.getValue()[1] + "])");
			context.write(new Text(entry.getKey()), new IntArrayWritable(entry.getValue())); 
		}
		
		super.cleanup(context);
	}
	
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String line = value.toString();
		String[] elems = line.split(" ");
		assert(elems.length > 1);
		int size;
		try {
			size = Integer.parseInt(elems[elems.length-1]);
		} catch (NumberFormatException e) {
			size = 0;
		}
		if(H.containsKey(elems[0])) {
			H.put(elems[0], new Integer[] { H.get(elems[0])[0]+size, H.get(elems[0])[1]+1 });
		} else
			H.put(elems[0], new Integer[] { size,1 });
	}
}