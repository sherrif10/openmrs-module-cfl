CREATE view NUMBERS_100   AS
SELECT (t1 * 10) + t0 + 1 AS number
FROM   DIGITS_0           AS t0
JOIN   DIGITS_1           AS t1
WHERE  (t1 * 10) + t0 + 1 < 100;
