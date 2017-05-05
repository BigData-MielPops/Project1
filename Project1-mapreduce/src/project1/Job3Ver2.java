package project1;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Job3Ver2 {

	public static int main(String[] args) throws Exception {
		Job job = new Job(new Configuration(), "Job3Ver2");
		job.setJarByClass(Job3Ver2.class);
		
		job.setMapperClass(Job3Mapper1.class);
		job.setNumReduceTasks(1);
		job.setReducerClass(Job3Ver2Reducer.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		return job.waitForCompletion(true) ? 0:1;
	}
}