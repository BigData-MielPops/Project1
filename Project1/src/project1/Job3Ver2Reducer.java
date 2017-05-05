package project1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Job3Ver2Reducer extends  Reducer<Text, Text, Text, Text> {

	private static String separator = "\t";
	private Map<String, Set<String>> commonProducts = new TreeMap<String, Set<String>>();

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException{		
		// Climbing out of Hadoop Object Reuse Pitfall
		List<Text> valueList = new ArrayList<Text>();
		for (Text value : values) {
			valueList.add(new Text(value));
		}
		
		for (int i = 0; i < valueList.size(); i++) {
			Text user1 = valueList.get(i);
			for (int j = i+1; j < valueList.size(); j++) {
				Text user2 = valueList.get(j); 
				if(user1.compareTo(user2) != 0) {
					Text orderedCouple = new Text(Util.orderCouple(user1.toString(), user2.toString(), separator));
					if(commonProducts.get(orderedCouple.toString()) == null) {
						commonProducts.put(orderedCouple.toString(), new HashSet<String>());
					}
					commonProducts.get(orderedCouple.toString()).add(key.toString());
				}
			}
		}
	}
	
	protected void cleanup(Context context) throws IOException,
	InterruptedException {
		for (Map.Entry<String, Set<String>> entry : commonProducts.entrySet()) {
			if (entry.getValue().size() >= 3)
				context.write(new Text(entry.getKey()), new Text(entry.getValue().toString()));
		}
	}

}