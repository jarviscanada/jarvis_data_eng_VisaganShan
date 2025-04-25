-- Show table schema 
\d+ retail;

-- Show first 10 rows
SELECT * FROM retail limit 10;

-- Check # of records
SELECT COUNT(*) FROM retail;

-- number of clients (e.g. unique client ID)
SELECT COUNT(DISTINCT customer_id) FROM retail;

-- invoice date range (max/min dates)
SELECT MAX(invoice_date) AS max, MIN(invoice_date) AS min FROM retail;

--  number of SKU/merchants (e.g. unique stock code)
SELECT COUNT(DISTINCT stock_code) FROM retail;

-- Calculate average invoice amount excluding invoices with a negative amount (e.g. canceled orders have negative amount)

SELECT AVG(total_value) AS avg
FROM (
SELECT SUM(unit_price * quantity) AS total_value
FROM retail
GROUP BY invoice_no
HAVING SUM(unit_price * quantity) > 0
) AS invoice_totals;

-- Calculate total revenue (e.g. sum of unit_price * quantity)

SELECT SUM(total_value) AS sum
FROM (
SELECT SUM(unit_price * quantity) as total_value
FROM retail
GROUP BY invoice_no
) AS invoice_totals

-- Calculate total revenue by YYYYMM

SELECT TO_CHAR(invoice_date, 'YYYYMM') AS date, SUM(unit_price * quantity) as total_value
FROM retail
GROUP by DISTINCT date
ORDER BY date ASC;