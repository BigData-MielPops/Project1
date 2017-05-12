package project1;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class Job3Ver1 extends Configured implements Tool {

	private static int numReduceTasksForMR2 = 1;
	
	public int run(String[] args) throws Exception {
		
		try {
			numReduceTasksForMR2 = Integer.parseInt(args[3]);
		}
		catch (Exception e) {
			System.err.println("No year arguments given, will use only " + numReduceTasksForMR2 + 
					   " Reducer" + ((numReduceTasksForMR2 !=1)? "s":"") + ".");
		}
		
		Configuration conf = new Configuration();
		
		/*
		 * Job 1
		 */		
		Job job = new Job(conf, "First MR");
		job.setJarByClass(Job3Ver1.class);
		
		job.setMapperClass(Job3Mapper1.class);
		job.setReducerClass(Job3Ver1Reducer1.class);
		
		job.setNumReduceTasks(1);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.waitForCompletion(true);

		/*
		 * Job 2
		 */
		Job job2 = new Job(conf, "Second MR");
		job2.setJarByClass(Job3Ver1.class);

		job2.setMapperClass(Job3Ver1Mapper2.class);
		job2.setReducerClass(Job3Ver1Reducer2.class);
		
		job2.setNumReduceTasks(numReduceTasksForMR2);

		job2.setMapOutputKeyClass(Text.class);
		job2.setMapOutputValueClass(Text.class);
		
		job2.setOutputKeyClass(Text.class);
		job2.setOutputValueClass(Text.class);

		FileInputFormat.addInputPath(job2, new Path(args[1]));
		FileOutputFormat.setOutputPath(job2, new Path(args[2]));

		return job2.waitForCompletion(true) ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 3) {
			System.err.println("Enter valid number of arguments <InputFile> <IntermediateFile> <OutputDirectory> [<Number of Reduce Tasks for second MR>]");
			System.exit(0);
		}
		ToolRunner.run(new Configuration(), new Job3Ver1(), args);
	}
}