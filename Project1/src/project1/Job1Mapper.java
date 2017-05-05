package project1;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Job1Mapper extends Mapper<Object, Text, Job1KeyWritable, IntWritable> {

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

		String productId = columns[1];

		Date date = new Date();
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
				date = new Date(Long.parseLong(columns[7+offset])*1000);
				passed = true;
			}
		catch (Exception e) {
			offset++;
		}
		// ---

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		Integer year = cal.get(Calendar.YEAR);
		Integer month = cal.get(Calendar.MONTH)+1;

		Job1KeyWritable k = new Job1KeyWritable(month, year, new Text(productId));

		context.write(k, new IntWritable(score));
		return;

	}
}