package project1;

import org.apache.spark.api.java.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.spark.SparkConf;

import scala.Tuple2;


public final class Job3 {

	private static String separator = "\t";
	private static JavaSparkContext ctx;

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.err.println("Usage: Job3 <input file> [<output file>]");
			System.exit(1);
		}
		SparkConf sparkConf = new SparkConf().setAppName("Job3");
		ctx = new JavaSparkContext(sparkConf);
		
		
		JavaRDD<String> csvLines = ctx.textFile(args[0]);
		JavaRDD<List<String>> lines = csvLines.map(s -> new ArrayList<String>(Arrays.asList(s.split(separator))));

		JavaPairRDD<String, Tuple2<String, Integer>> userScoreByProduct =
				lines.mapToPair(line -> 
					new Tuple2<>(
							line.get(1), // product
							new Tuple2<>(line.get(2), Integer.parseInt(line.get(6))) // (user, score)
						)
					).filter(line -> line._2()._2() >= 4); // (user, 4 | 5)
		
		JavaPairRDD<String, String> joinOnProduct = userScoreByProduct
				.join(userScoreByProduct) // JavaPairRDD<String, Tuple2<Tuple2<String, Integer>, Tuple2<String, Integer>>>
				.filter(line -> line._2()._1()._1().compareTo(line._2()._2()._1()) != 0) // different users
				.mapToPair(line -> 
					new Tuple2<>(
							Util.orderCouple(line._2()._1()._1(), line._2()._2()._1(), separator), // (u1	u2)
							line._1() // product
						) 
					).distinct();
		
		JavaPairRDD<String, Iterable<String>> similarUsersAndCommonProducts = joinOnProduct.groupByKey(1)
				.filter(line -> gte(line._2(), 3)).sortByKey(true);
		
		
		if (args.length > 1)
			similarUsersAndCommonProducts.saveAsTextFile(args[1]);
		else {
			List<Tuple2<String, Iterable<String>>> output = similarUsersAndCommonProducts.collect();
			for
			(Tuple2<?,?> tuple	 :	output	) {
				System.out.println(tuple._1() + ": " + tuple._2());
			}
		}
		
		ctx.stop();
	}
	
	public static <T> Boolean gte(Iterable<T> iterable, Integer howMany) {		
		int count = 0;
		Iterator<T> it = iterable.iterator();
		
		while (it.hasNext()) {
			it.next();
			count++;
		}
		
		return count >= howMany;
	}
}