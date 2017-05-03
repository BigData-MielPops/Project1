CREATE TABLE IF NOT EXISTS 1999_2006 (id BIGINT, product_id STRING, user_id STRING, profile_name STRING, 
helpfulness_num INT, helpfulness_den INT, score INT, time BIGINT, summary STRING, text STRING) 
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t';

LOAD DATA LOCAL INPATH '/home/data/1999_2006.csv' OVERWRITE INTO TABLE 1999_2006;

DROP TABLE user_similarity;

CREATE TABLE user_similarity AS
	SELECT CONCAT_WS(',', part.user_couple), COLLECT_LIST(part.product) 
	FROM
	(	SELECT SORT_ARRAY(ARRAY(jr.user_id, jl.user_id)) as user_couple, jr.product_id as product, row_number() OVER (PARTITION BY jr.user_id, jl.user_id ORDER BY ORDER BY jr.user_id, jl.user_id ASC) AS num_products
		FROM 1999_2006 jr JOIN 1999_2006 jl ON ( jr.product_id = jl.product_id AND jr.score >= 4 AND jl.score >=4 AND jr.user_id != jl.user_id ) 
		) part 
	WHERE part.num_products >= 3 
	GROUP part.user_couple 
	ORDER BY part.user_couple ASC
	
	
INSERT OVERWRITE LOCAL DIRECTORY '/home/csv/job3' ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' SELECT * FROM user_similarity;