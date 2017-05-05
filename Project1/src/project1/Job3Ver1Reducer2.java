package project1;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Job3Ver1Reducer2 extends  Reducer<Text, Text, Text, Text> {

	@SuppressWarnings("unused")
	private static String separator = "\t";

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException{	
		// Climbing out of Hadoop Object Reuse Pitfall
		Set<Text> valueList = new HashSet<Text>();
		for (Text value : values) {
			valueList.add(new Text(value));
		}
		if(valueList.size() >= 3) context.write(key, new Text(valueList.toString()));
	}

}