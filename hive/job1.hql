CREATE TABLE IF NOT EXISTS 1999_2006 (id BIGINT, product_id STRING, user_id STRING, profile_name STRING, 
helpfulness_num INT, helpfulness_den INT, score INT, time TIMESTAMP, summary STRING, text STRING) 
ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t'; 

LOAD DATA LOCAL INPATH '/home/BigData/Project1/data/1999_2006.csv' OVERWRITE INTO TABLE 1999_2006;

CREATE TABLE top_five_products AS

SELECT mpl.month, collect_set(mpl.product_id), collect_set(mpl.avg_score) FROM
(   SELECT mp.month, mp.product_id, mp.avg_score FROM
    (   SELECT date_format(time,'yyyyMM') AS month, product_id, AVG(score) AS avg_score
        FROM 1999_2006 
        GROUP BY month, product_id 
        ORDER BY month ASC ) mp
    ORDER BY mp.avg_score ASC LIMIT 5) mpl
GROUP BY mp.month;