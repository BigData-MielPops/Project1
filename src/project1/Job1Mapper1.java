package project1;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Job1Mapper1 extends Mapper<Object, Text, Job1KeyWritable, IntWritable> {

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		System.out.println("==============");
		System.out.println(key);
		System.out.println(value);
		System.out.println();

		String[] columns = value.toString().split("\t");
		
		try {
			@SuppressWarnings("unused")
			Long test1 = Long.parseLong(columns[0]);
		}
		catch (Exception e) {  // if column[0] is not a number, then this is the csv header
			System.out.println("SKIPPED");
			System.out.println();
			return;
		}

		String productId = columns[1];

		System.out.println(columns);
		System.out.println();

		Date date = new Date();
		Integer score = 0;

		boolean passed = false;
		Integer offset = 0;
		for(int i=0; !passed && i<100; i++)
			try {
				// the part where random separators can appear is followed by 4 integers
				// this piece of code was used when the dataset was comma-separated
				// now it should be fixed, but still is safe to have it
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

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		Integer year = cal.get(Calendar.YEAR);
		Integer month = cal.get(Calendar.MONTH)+1;

		System.out.println(year);
		System.out.println(month);
		System.out.println();

		System.out.println(score);
		System.out.println();

		Job1KeyWritable k = new Job1KeyWritable(month, year, new Text(productId));

		System.out.println("DONE");
		System.out.println();

		System.out.println(k);

		context.write(k, new IntWritable(score));
		return;

	}
}