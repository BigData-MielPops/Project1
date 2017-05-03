package project1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Job3Ver1Reducer1 extends  Reducer<Text, Text, Text, Text> {

	private static String separator = "\t";

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException{		
		// Climbing out of Hadoop Object Reuse Pitfall
		List<Text> valueList = new ArrayList<Text>();
		for (Text value : values) {
			valueList.add(new Text(value));
		}
		
		for (int i = 0; i < valueList.size(); i++) {
			Text user1 = valueList.get(i);
			for (int j = 0; j < valueList.size(); j++) {
				Text user2 = valueList.get(j); 
				if(user1.compareTo(user2) != 0) {
					Text orderedCouple = new Text(Util.orderCouple(user1.toString(), user2.toString(), separator));
					context.write(orderedCouple, key);
				}
			}
		}
	}

}