package project1;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Job2Reducer extends  Reducer<Text, Text, Text, Text> {

	private static String separator = "\t";
	// ordered map using year+month as key, storing the top 5 products and their average score
	private static Map<String, String[]> topByUser = new TreeMap<String, String[]>();

	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException{	

		for(Text data : values)
		{
			String topKey = key.toString();
			if(topByUser.get(topKey) == null) {
				topByUser.put(topKey, new String[10]);
			}

			Util.insertInOrderByScore(data.toString(), topByUser.get(topKey));
		}
		
	}

	protected void cleanup(Context context) throws IOException,
	InterruptedException {
		for (Map.Entry<String, String[]> entry : topByUser.entrySet()) {
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