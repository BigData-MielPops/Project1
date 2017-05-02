CREATE TABLE 1999_2006 (
id INT, product_id TEXT, user_id TEXT, profile_name TEXT, 
helpfulness_num INT, helpfulness_den INT, score INT, time TIMESTAMP, summary TEXT, text TEXT
);

INSERT INTO 1999_2006 (product_id, user_id, score, time)
VALUES ('A1', 'U1', 5, CURRENT_TIMESTAMP);

INSERT INTO 1999_2006 (product_id, user_id, score, time)
VALUES ('A2', 'U2', 4, CURRENT_TIMESTAMP);

INSERT INTO 1999_2006 (product_id, user_id, score, time)
VALUES ('A3', 'U2', 4, CURRENT_TIMESTAMP);

INSERT INTO 1999_2006 (product_id, user_id, score, time)
VALUES ('A4', 'U2', 4, CURRENT_TIMESTAMP);

INSERT INTO 1999_2006 (product_id, user_id, score, time)
VALUES ('A5', 'U2', 4, CURRENT_TIMESTAMP);

INSERT INTO 1999_2006 (product_id, user_id, score, time)
VALUES ('A6', 'U2', 4, CURRENT_TIMESTAMP);

INSERT INTO 1999_2006 (product_id, user_id, score, time)
VALUES ('A6', 'U2', 4, '2013-08-05 18:19:03');


SELECT mpl.month, GROUP_CONCAT(mpl.product_id), GROUP_CONCAT(mpl.avg_score) 
FROM
(   SELECT mp.month, mp.product_id, mp.avg_score, row_number() OVER (PARTITION BY mp.month ORDER BY mp.avg_score DESC) AS top_position 
	FROM
	(   SELECT MONTH(time) AS month, product_id, AVG(score) AS avg_score
        FROM 1999_2006 
		GROUP BY MONTH(time), product_id 
		ORDER BY month ASC ) mp
	ORDER BY mp.avg_score DESC) mpl
WHERE mpl.top_position <= 5
GROUP BY mpl.month;