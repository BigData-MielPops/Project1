package project1;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Job1 {

	public static void main(String[] args) throws Exception {
		Job job = new Job(new Configuration(), "Job1");
		job.setJarByClass(Job1.class);
		
		job.setMapperClass(Job1Mapper1.class);
		job.setNumReduceTasks(1);
		job.setReducerClass(Job1Reducer1.class);
		
		job.setMapOutputKeyClass(Job1KeyWritable.class);
		job.setMapOutputValueClass(IntWritable.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		job.waitForCompletion(true);
	}
}