package net.endoedgar;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

public class MyMapper extends Mapper<LongWritable, Text, Text, MyMapWritable> {
	private Logger logger = Logger.getLogger(this.getClass());
	private Map<String, Map<String, Integer>> H;
	@Override
	protected void setup(Mapper<LongWritable, Text, Text, MyMapWritable>.Context context)
			throws IOException, InterruptedException {
		super.setup(context);
		H = new HashMap<String, Map<String,Integer>>();
	}
	
	@Override
	protected void cleanup(Mapper<LongWritable, Text, Text, MyMapWritable>.Context context)
			throws IOException, InterruptedException {
		for (Entry<String,Map<String,Integer>> entry : H.entrySet())  {
			MyMapWritable m = new MyMapWritable();
			for(Entry<String, Integer> subEntry : entry.getValue().entrySet()) {
				m.put(new Text(subEntry.getKey()), new IntWritable(subEntry.getValue()));
			}
			logger.info("(" + entry.getKey() + ", " + entry.getValue() + ")");
			context.write(new Text(entry.getKey()), m); 
		}
		
		super.cleanup(context);
	}
	
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String line = value.toString();
		String[] elems = line.split("\t");
		assert(elems.length > 1);
		String country = elems[elems.length-1];
		String sex = elems[elems.length-2];
		if(!H.containsKey(country))
			H.put(country, new HashMap<String, Integer>());
		Map<String, Integer> selectedCountry = H.get(country);
		if(selectedCountry.containsKey(sex)) {
			selectedCountry.put(sex, selectedCountry.get(sex)+1);
		} else
			selectedCountry.put(sex, 1);
	}
}