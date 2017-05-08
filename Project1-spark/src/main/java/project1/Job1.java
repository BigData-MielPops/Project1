package project1;

import org.apache.spark.api.java.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.spark.SparkConf;

import scala.Tuple2;


public final class Job1 {

	private static String separator = "\t";
	private static JavaSparkContext ctx;

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.err.println("Usage: Job1 <input file> [<output file>]");
			System.exit(1);
		}
		SparkConf sparkConf = new SparkConf().setAppName("Job1");
		ctx = new JavaSparkContext(sparkConf);


		JavaRDD<String> csvLines = ctx.textFile(args[0], 1);
		JavaRDD<List<String>> lines = csvLines.map(s -> new ArrayList<String>(Arrays.asList(s.split(separator))));

		JavaPairRDD<String, Tuple2<Integer, Integer>> countByMonthProduct =
				lines.mapToPair(line -> 
				new Tuple2<>(
						timestampToMonth(Long.parseLong(line.get(7)) * 1000) + separator + line.get(1), 
						new Tuple2<>(Integer.parseInt(line.get(6)), 1)
						)
						).reduceByKey((a, b) -> 
						new Tuple2<>(
								a._1() + b._1(), // total score by month+product
								a._2() + b._2() // count by month+product
								)
								);

		JavaPairRDD<String, Iterable<String>> averageAndProductListByMonth = 
				countByMonthProduct.mapToPair(reducedLine -> 
				new Tuple2<>(
						reducedLine._1().split(separator)[0], // month
						String.valueOf(
								(float)reducedLine._2()._1() / reducedLine._2()._2()) + separator + reducedLine._1().split(separator)[1]
						) // (product \t average)
						).groupByKey(1);

		JavaPairRDD<String, List<String>> topFiveProductsByMonth = 
				averageAndProductListByMonth.mapToPair(reGroupedLine ->
				new Tuple2<>(
						reGroupedLine._1(),
						Arrays.asList((takeFirst(reGroupedLine._2(), 5))).stream().filter(Objects::nonNull).collect(Collectors.toList())
						)
						);


		if (args.length > 1)
			topFiveProductsByMonth.coalesce(1).sortByKey(true).saveAsTextFile(args[1]);
		else {
			List<Tuple2<String, List<String>>> output = topFiveProductsByMonth.coalesce(1).sortByKey(true).collect();
			for
			(Tuple2<?,?> tuple	 :	output	) {
				System.out.println(tuple._1() + ": " + tuple._2());
			}
		}

		ctx.stop();
	}


	public static String timestampToMonth(Long timestamp) {
		Date date = new Date(timestamp);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		Integer year = cal.get(Calendar.YEAR);
		Integer month = cal.get(Calendar.MONTH)+1;

		return String.valueOf(year) + String.format("%02d", month);
	}

	public static String[] takeFirst(Iterable<String> iterable, Integer howMany) {
		String[] app = new String[howMany];

		for (String data : iterable) {
			Util.insertInOrderByScore(data, app);
		}

		return app;
	}
}