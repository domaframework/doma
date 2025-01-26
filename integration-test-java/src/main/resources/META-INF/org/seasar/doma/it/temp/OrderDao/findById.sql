SELECT
    o.id,
    o.status_code AS os_code,
    os.name AS os_name,
    oi.order_id AS oi_order_id,
    oi.item_code AS oi_item_code,
    oi.quantity AS oi_quantity,
    oc.order_id AS oc_order_id,
    oc.coupon_code AS oc_coupon_code,
    i.code AS i_code,
    i.name AS i_name,
    i.price AS i_price,
    ct.code AS ct_code,
    ct.name AS ct_name,
    cp.code AS cp_code,
    cp.name AS cp_name,
    cp.price AS cp_price
FROM
    t_order o
    INNER JOIN c_order_status os ON os.code = o.status_code
    INNER JOIN t_order_item oi ON oi.order_id = o.id
    INNER JOIN m_item i ON i.code = oi.item_code
    INNER JOIN m_item_category ic ON ic.item_code = i.code
    INNER JOIN m_category ct ON ct.code = ic.category_code
    LEFT JOIN t_order_coupon oc ON oc.order_id = o.id
    LEFT JOIN m_coupon cp ON cp.code = oc.coupon_code
WHERE
    o.id = /* id */0
ORDER BY
    i_code ASC,
    ct_code ASC,
    cp_code ASC