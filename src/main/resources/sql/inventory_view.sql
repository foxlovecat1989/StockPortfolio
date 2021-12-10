SELECT
    ROW_NUMBER() OVER() AS id,
        au.id AS user_id,
    s.id AS stock_id,
    s.name AS stcok,
    SUM(t.amount) AS amount,
    SUM(t.cost) AS total_cost,
    AVG(t.cost / t.amount) AS avg_price,
    t.price AS current_price
FROM app_user au, trade t, tstock s
WHERE au.id = t.user_id AND t.t_stock_id = s.id
GROUP BY au.id, s.id, t.price