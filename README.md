# Big Data - Project 1
Group: MielPops

Members: Gaetano Bonofiglio, Veronica Iovinella

____

Assignment: [http://torlone.dia.uniroma3.it/bigdata/PrimoProgetto.pdf](http://torlone.dia.uniroma3.it/bigdata/PrimoProgetto.pdf)

Dataset: [http://torlone.dia.uniroma3.it/bigdata/FineFoodReviews.zip](http://torlone.dia.uniroma3.it/bigdata/FineFoodReviews.zip)


## Requirements
Hadoop >2.7, Hive >1.0, Spark >1.6

## Instructions
- Download the dataset and load it on your HDFS.
- Import code as Maven project and run as Maven install. 
- To run MapReduce jobs (replace NUMBER with the job number, optional arguments are between "()"):
```bash
# additional arguments like the number of mapreduce tasks or the range of the years for Job1
yarn jar /path/to/project1-mapreduce-mielpops.jar "project1.JobNUMBER" /path/to/input_data (/path/to/intermediate_file_for_Job3Ver1) /path/to/output (additional arguments)
```
- To run Hive jobs:
```bash
# the query inside the hql file will load data from your file system to Hive tables om HDFS. Edit paths accordingly.
hive -f /path/to/jobNUMBER.hql
```
- To run Spark jobs: 
```bash
# in the tests we also used --master yarn --driver-memory 7g --executor-memory 3g --num-executors 19
spark-submit --class project1.JobNUMBER /path/to/project1-spark-mielpops.jar /path/to/input_data /path/to/output
```
