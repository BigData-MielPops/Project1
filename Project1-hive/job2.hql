CREATE TABLE IF NOT EXISTS 1999_2006 (id BIGINT, product_id STRING, user_id STRING, profile_name STRING, 
helpfulness_num INT, helpfulness_den INT, score INT, time BIGINT, summary STRING, text STRING) 
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';

LOAD DATA LOCAL INPATH '/home/data/1999_2006.csv' OVERWRITE INTO TABLE 1999_2006;

DROP TABLE top_favourite_products;

CREATE TABLE top_favourite_products AS
	SELECT ups.user_id, COLLECT_LIST(ups.product_id), COLLECT_LIST(ups.score) 
	FROM
	(   SELECT user_id, product_id, score, row_number() OVER (PARTITION BY user_id ORDER BY score DESC) AS top_position 
	    FROM 1999_2006 
		ORDER BY score DESC) ups
	WHERE ups.top_position <= 10
	GROUP BY ups.user_id
	ORDER BY ups.user_id ASC;
	
INSERT OVERWRITE LOCAL DIRECTORY '/home/csv/job2' ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' SELECT * FROM top_favourite_products;