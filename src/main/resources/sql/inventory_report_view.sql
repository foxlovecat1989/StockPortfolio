SELECT ROW_NUMBER() OVER() AS id,
       au.id AS user_id,
       concat(s.name, '-', SUBSTR(s.symbol, 1, 4)) AS stock_name,
       c.name AS classify_name,
       i.amount AS amount_in_inventory,
       (s.price + 10) AS current_price,
       i.amount * (s.price + 10) AS current_total_worth,
       SUM(i.cost) AS total_cost,
       AVG(CAST(i.cost AS FLOAT) / CAST(i.amount AS FLOAT)) AS avg_price_in_inventory,
       i.amount * (s.price + 10) - sum(i.cost) AS income,
       ROUND((i.amount * (s.price + 10) - sum(i.cost)) / sum(i.cost) * 100, 2) AS rate_of_return
FROM app_user au, inventory i, tstock s, classify c
WHERE au.id = i.user_id AND i.tstock_id = s.id AND s.classify_id = c.classify_id
GROUP BY au.id, s.name, s.symbol, c.name, i.amount, s.price