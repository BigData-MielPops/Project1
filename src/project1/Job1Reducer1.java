package project1;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Job1Reducer1 extends  Reducer<Job1KeyWritable, IntWritable, Text, Text> {

	// data structure divided by year+month, storing the top 5 products and their average score
	private static Map<String, String[]> topByMonth = new TreeMap<String, String[]>();

	public void reduce(Job1KeyWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException{
		System.out.println("==============");
		System.out.println(key);
		System.out.println();

		Integer totalCount = 0;
		Integer maxSum = 0;
		for(IntWritable data : values)
		{
			Integer app = data.get();
			if (app > maxSum) {
				maxSum = app;
			}
			totalCount++;
		}
		System.out.println(maxSum);
		System.out.println(totalCount);

		float avg = (float)maxSum / totalCount;		

		String topKey = String.valueOf(key.getYear()) + "\t" + String.format("%02d", key.getMonth());
		if(topByMonth.get(topKey) == null) {
			topByMonth.put(String.valueOf(topKey), new String[5]);
		}

		Util.insert(String.valueOf(avg) + "\t" + key.getProductId(), topByMonth.get(topKey));

		System.out.println("DONE");
		System.out.println();
	}

	protected void cleanup(Context context) throws IOException,
	InterruptedException {
		System.out.println("==========================================");
		System.out.println("Cleanup");
		System.out.println();

		for (Map.Entry<String, String[]> entry : topByMonth.entrySet()) {
			String results = "";
			for(int i=0; i < entry.getValue().length; i++) {
				try {
					results += "(" + entry.getValue()[i].split("\t")[1] + ", " + entry.getValue()[i].split("\t")[0] + ")\t";
				}
				catch (Exception e) {;}
			}

			System.out.println(entry.getKey());
			System.out.println(results);
			System.out.println();
			
			context.write(new Text(entry.getKey()), new Text(results));
		}
		System.out.println("CLEANUP DONE");
	}

}