package project1;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Job2 {
	private static int numReduceTasks = 1;

	public static void main(String[] args) throws Exception {
		Job job = new Job(new Configuration(), "Job2");
		job.setJarByClass(Job2.class);

		job.setMapperClass(Job2Mapper.class);
		job.setReducerClass(Job2Reducer.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		try {
			numReduceTasks = Integer.parseInt(args[2]);
		}
		catch (Exception e) {
			System.err.println("No year arguments given, will use only " + numReduceTasks + 
					   " Reducer" + ((numReduceTasks !=1)? "s":"") + ".");
		}
		job.setNumReduceTasks(numReduceTasks);

		job.waitForCompletion(true);
	}
}