DROP TABLE 1999_2006;

CREATE TABLE IF NOT EXISTS 1999_2006 (id BIGINT, product_id STRING, user_id STRING, profile_name STRING, 
helpfulness_num INT, helpfulness_den INT, score INT, time BIGINT, summary STRING, text STRING) 
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';

LOAD DATA LOCAL INPATH '/home/workspace/data/1999_2006.csv' OVERWRITE INTO TABLE 1999_2006;

DROP TABLE top_five_products;

CREATE TABLE top_five_products AS
	SELECT mpl.month, COLLECT_LIST(mpl.product_id), COLLECT_LIST(mpl.avg_score) 
	FROM
	(   SELECT mp.month, mp.product_id, mp.avg_score, row_number() OVER (PARTITION BY mp.month ORDER BY mp.avg_score DESC) AS top_position 
	    FROM
		(   SELECT FROM_UNIXTIME(time,'yyyyMM') AS month, product_id, AVG(score) AS avg_score
			FROM 1999_2006 
			GROUP BY FROM_UNIXTIME(time,'yyyyMM'), product_id 
			ORDER BY month ASC ) mp
		ORDER BY mp.avg_score DESC) mpl
	WHERE mpl.top_position <= 5
	GROUP BY mpl.month;
	
INSERT OVERWRITE LOCAL DIRECTORY '/home/workspace/Project1-hive/results/job1' ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' SELECT * FROM top_five_products;