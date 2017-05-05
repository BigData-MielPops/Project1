CREATE TABLE IF NOT EXISTS 1999_2006 (id BIGINT, product_id STRING, user_id STRING, profile_name STRING, 
helpfulness_num INT, helpfulness_den INT, score INT, time BIGINT, summary STRING, text STRING) 
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';

LOAD DATA LOCAL INPATH '/home/data/1999_2006.csv' OVERWRITE INTO TABLE 1999_2006;

DROP TABLE user_similarity;

CREATE TABLE user_similarity AS
	SELECT upn.user_couple, upn.products
	FROM
	(	SELECT up.user_couple, COLLECT_LIST(up.product_id) as products, COUNT(1) as num_products
		FROM 
		(	SELECT DISTINCT CONCAT_WS(',', SORT_ARRAY(ARRAY(jr.user_id, jl.user_id))) as user_couple, jr.product_id
			FROM 1999_2006 jr JOIN 1999_2006 jl ON jr.product_id = jl.product_id AND jr.score >= 4 AND jl.score >=4 
			WHERE jr.user_id != jl.user_id 
			) up
		GROUP BY up.user_couple 
		ORDER BY up.user_couple ASC
	) upn
	WHERE upn.num_products >= 3;


INSERT OVERWRITE LOCAL DIRECTORY '/home/csv/job3' ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' SELECT * FROM user_similarity;