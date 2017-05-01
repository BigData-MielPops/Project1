package project1;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Job1 {
	private static int numReduceTasks = 1;
	
	public static int minYear = 0;
	public static int maxYear = 0;

	public static int main(String[] args) throws Exception {
		Job job = new Job(new Configuration(), "Job1");
		job.setJarByClass(Job1.class);
		
		job.setMapperClass(Job1Mapper.class);
		job.setPartitionerClass(Job1Partitioner.class);
		job.setReducerClass(Job1Reducer.class);
		
		job.setMapOutputKeyClass(Job1KeyWritable.class);
		job.setMapOutputValueClass(IntWritable.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		// With only 2 arguments there will be only 1 Reducer to keep the Cleanup ordering consistant.
		// But if the user specifies a min year and a max year, then there will be a higher number 
		// of Reducers and a custom Partitioner to give 1 year to each Reducer, in order to keep a 
		// consistant ordering in the Cleanup.
		try {
			minYear = Integer.parseInt(args[2]);
			maxYear = Integer.parseInt(args[3]);
			int range = (1 + maxYear - minYear);
			if(range !=0) numReduceTasks = range;
		}
		catch (Exception e) {
			System.err.println("No year arguments given, will use only " + numReduceTasks + 
							   " Reducer" + ((numReduceTasks !=1)? "s":"") + ".");
		}
		job.setNumReduceTasks(numReduceTasks);
		
		return job.waitForCompletion(true) ? 0:1;
	}
}