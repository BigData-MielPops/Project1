package project1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Job3Ver1Reducer2 extends  Reducer<Text, Text, Text, Text> {

	@SuppressWarnings("unused")
	private static String separator = "\t";

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException{	
		@SuppressWarnings("unchecked")
		List<Text> valueList = (ArrayList<Text>) IteratorUtils.toList(values.iterator()); 
		if(valueList.size() >= 3) context.write(key, new Text(valueList.toString()));
	}

}