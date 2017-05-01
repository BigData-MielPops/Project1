package project1;

import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Job2Mapper extends Mapper<Object, Text, Text, Text> {

	private static String separator = "\t";
	
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		String[] columns = value.toString().split(separator);
		try {
			@SuppressWarnings("unused")
			Long test1 = Long.parseLong(columns[0]);
		}
		catch (Exception e) {  // if column[0] is not a number, then this is the csv header
			return;
		}
		
		String userId = columns[2];
		String productId = columns[1];
		
		Integer score = 0;

		// the part where random separators can appear is followed by 4 integers
		// this piece of code was used when the dataset was comma-separated
		// now it should be fixed, but still is safe to have it
		boolean passed = false;
		Integer offset = 0;
		for(int i=0; !passed && i<100; i++)
			try {
				@SuppressWarnings("unused")
				Integer test2;
				test2 = Integer.parseInt(columns[4+offset]);
				test2 = Integer.parseInt(columns[5+offset]);
				score = Integer.parseInt(columns[6+offset]);
				passed = true;
			}
		catch (Exception e) {
			offset++;
		}
		// ---

		context.write(new Text(userId), new Text(score + separator + productId));
		return;

	}
}