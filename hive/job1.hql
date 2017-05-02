CREATE TABLE IF NOT EXISTS 1999_2006 (id BIGINT, product_id STRING, user_id STRING, profile_name STRING, 
helpfulness_num INT, helpfulness_den INT, score INT, time TIMESTAMP, summary STRING, text STRING) 
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';

LOAD DATA LOCAL INPATH '/home/data/1999_2006.csv' OVERWRITE INTO TABLE 1999_2006;


CREATE TABLE top_five_products AS
	SELECT mpl.month, collect_set(mpl.product_id), collect_set(mpl.avg_score) 
	FROM
	(   SELECT mp.month, mp.product_id, mp.avg_score, row_number() OVER (PARTITION BY mp.month ORDER BY mp.avg_score DESC) AS top_position 
	    FROM
		(   SELECT date_format(time,'yyyyMM') AS month, product_id, AVG(score) AS avg_score
			FROM 1999_2006 
			GROUP BY date_format(time,'yyyyMM'), product_id 
			ORDER BY month ASC ) mp
		ORDER BY mp.avg_score DESC) mpl
	WHERE mpl.top_position <= 5
	GROUP BY mpl.month;