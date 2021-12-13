SELECT ROW_NUMBER() OVER() AS id,
       au.id AS user_id,
       concat(s.name, '-', SUBSTR(s.symbol, 1, 4)) AS stock_name,
       c.name AS classify_name,
       i.amount AS amount_in_inventory,
       (s.price + 10) AS current_price,
       i.amount * (s.price + 10) AS current_total_worth,
       SUM(i.avg_price * i.amount) AS total_cost,
       i.avg_price AS avg_price_in_inventory,
       i.amount * s.price - sum(i.avg_price * i.amount) AS income,
       ROUND(CAST(((i.amount * (s.price + 10) - sum(i.avg_price * i.amount)) / sum(NULLIF((i.avg_price * i.amount), 0)) * 100) AS DECIMAL), 2) AS rate_of_return
FROM app_user au, inventory i, tstock s, classify c
WHERE au.id = i.user_id AND i.tstock_id = s.id AND s.classify_id = c.classify_id
GROUP BY au.id, s.name, s.symbol, c.name, i.amount, i.avg_price, s.price