package project1;

import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Job3Ver1Mapper2 extends Mapper<Object, Text, Text, Text> {

	private static String separator = "\t";
	
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		String[] columns = value.toString().split(separator);

		context.write(new Text(columns[0] + separator + columns[1]), new Text(columns[2]));
		return;

	}
}