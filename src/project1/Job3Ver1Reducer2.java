package project1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Job3Ver1Reducer2 extends  Reducer<Text, Text, Text, Text> {

	@SuppressWarnings("unused")
	private static String separator = "\t";

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException{	
		// Climbing out of Hadoop Object Reuse Pitfall
		List<Text> valueList = new ArrayList<Text>();
		for (Text value : values) {
			valueList.add(new Text(value));
		}
		if(valueList.size() >= 3) context.write(key, new Text(valueList.toString()));
	}

}