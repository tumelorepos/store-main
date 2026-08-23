-- Insert sample products
INSERT INTO product (id, description) VALUES (1, 'Widget A');
INSERT INTO product (id, description) VALUES (2, 'Widget B');
INSERT INTO product (id, description) VALUES (3, 'Gadget X');
INSERT INTO product (id, description) VALUES (4, 'Gadget Y');
INSERT INTO product (id, description) VALUES (5, 'Component Alpha');
INSERT INTO product (id, description) VALUES (6, 'Component Beta');
INSERT INTO product (id, description) VALUES (7, 'Service Premium');
INSERT INTO product (id, description) VALUES (8, 'Service Standard');
INSERT INTO product (id, description) VALUES (9, 'License Pro');
INSERT INTO product (id, description) VALUES (10, 'License Basic');

-- Associate products with orders (sample associations)
INSERT INTO order_product (order_id, product_id) VALUES (1, 1);
INSERT INTO order_product (order_id, product_id) VALUES (1, 2);
INSERT INTO order_product (order_id, product_id) VALUES (2, 3);
INSERT INTO order_product (order_id, product_id) VALUES (2, 4);
INSERT INTO order_product (order_id, product_id) VALUES (3, 1);
INSERT INTO order_product (order_id, product_id) VALUES (3, 5);
INSERT INTO order_product (order_id, product_id) VALUES (4, 2);
INSERT INTO order_product (order_id, product_id) VALUES (4, 6);
INSERT INTO order_product (order_id, product_id) VALUES (5, 7);
INSERT INTO order_product (order_id, product_id) VALUES (5, 8);
INSERT INTO order_product (order_id, product_id) VALUES (6, 9);
INSERT INTO order_product (order_id, product_id) VALUES (6, 10);
INSERT INTO order_product (order_id, product_id) VALUES (7, 1);
INSERT INTO order_product (order_id, product_id) VALUES (7, 3);
INSERT INTO order_product (order_id, product_id) VALUES (8, 2);
INSERT INTO order_product (order_id, product_id) VALUES (8, 4);
INSERT INTO order_product (order_id, product_id) VALUES (9, 5);
INSERT INTO order_product (order_id, product_id) VALUES (10, 6);
