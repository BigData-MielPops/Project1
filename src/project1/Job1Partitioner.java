package project1;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Partitioner;

public class Job1Partitioner extends Partitioner <Job1KeyWritable, IntWritable>
{
	@Override
	public int getPartition(Job1KeyWritable key, IntWritable value, int numReduceTasks)
	{
		if(numReduceTasks == 0)
		{
			return 0;
		}
		return key.getYear() % numReduceTasks;
	}
}