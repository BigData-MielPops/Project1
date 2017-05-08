package project1;

import org.apache.spark.api.java.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.spark.SparkConf;

import scala.Tuple2;


public final class Job2 {

	private static String separator = "\t";
	private static JavaSparkContext ctx;

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.err.println("Usage: Job2 <input file> [<output file>]");
			System.exit(1);
		}
		SparkConf sparkConf = new SparkConf().setAppName("Job2");
		ctx = new JavaSparkContext(sparkConf);


		JavaRDD<String> csvLines = ctx.textFile(args[0], 1);
		JavaRDD<List<String>> lines = csvLines.map(s -> new ArrayList<String>(Arrays.asList(s.split(separator))));

		JavaPairRDD<String, String> productScoreByUser =
				lines.mapToPair(line -> 
				new Tuple2<>(
						line.get(2), 
						line.get(6) + separator + line.get(1)
						)
						);

		JavaPairRDD<String, Iterable<String>> productsByUser = productScoreByUser.groupByKey(1);

		JavaPairRDD<String, List<String>> topTenProductsByUser = 
				productsByUser.mapToPair(reGroupedLine ->
				new Tuple2<>(
						reGroupedLine._1(),
						Arrays.asList((takeFirst(reGroupedLine._2(), 10))).stream().filter(Objects::nonNull).collect(Collectors.toList())
						)
						);


		if (args.length > 1)
			topTenProductsByUser.coalesce(1).sortByKey(true).saveAsTextFile(args[1]);
		else {
			List<Tuple2<String, List<String>>> output = topTenProductsByUser.coalesce(1).sortByKey(true).collect();
			for
			(Tuple2<?,?> tuple	 :	output	) {
				System.out.println(tuple._1() + ": " + tuple._2());
			}
		}

		ctx.stop();
	}

	public static String[] takeFirst(Iterable<String> iterable, Integer howMany) {
		String[] app = new String[howMany];

		for (String data : iterable) {
			Util.insertInOrderByScore(data, app);
		}

		return app;
	}
}