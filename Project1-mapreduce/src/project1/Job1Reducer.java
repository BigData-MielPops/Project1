package project1;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Job1Reducer extends  Reducer<Job1KeyWritable, IntWritable, Text, Text> {

	private static String separator = "\t";
	// ordered map using year+month as key, storing the top 5 products and their average score
	private static Map<String, String[]> topByMonth = new TreeMap<String, String[]>();

	public void reduce(Job1KeyWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException{

		Integer totalCount = 0;
		Float sum = 0f;

		for(IntWritable data : values)
		{
			sum += data.get();
			totalCount++;
		}

		Float avg = sum / totalCount;		

		String topKey = String.valueOf(key.getYear()) + separator + String.format("%02d", key.getMonth());
		if(topByMonth.get(topKey) == null) {
			topByMonth.put(String.valueOf(topKey), new String[5]);
		}

		Util.insertInOrderByScore(String.valueOf(avg) + separator + key.getProductId(), topByMonth.get(topKey));
	}

	protected void cleanup(Context context) throws IOException,
	InterruptedException {
		for (Map.Entry<String, String[]> entry : topByMonth.entrySet()) {
			String results = "";
			for(int i=0; i < entry.getValue().length; i++) {
				try {
					results += "(" + entry.getValue()[i].split(separator)[1] + 
							", " + entry.getValue()[i].split(separator)[0] + ")" 
							+ separator;
				}
				catch (Exception e) {;}
			}			
			context.write(new Text(entry.getKey()), new Text(results));
		}
	}

}