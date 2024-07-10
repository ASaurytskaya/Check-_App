CREATE ROLE check_app_manager WITH
	LOGIN
	SUPERUSER
	CREATEDB
	CREATEROLE
	INHERIT
	NOREPLICATION
	CONNECTION LIMIT -1
	PASSWORD 'check_app_password';

CREATE DATABASE "check"
    WITH
    OWNER = check_app_manager
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

CREATE TABLE public.product
(
    id bigserial,
    description character varying(50) NOT NULL,
    price numeric(1000, 2) NOT NULL,
    quantity_in_stock integer NOT NULL,
    wholesale_product boolean NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public.product
    OWNER to check_app_manager;

INSERT INTO public.product (description, price, quantity_in_stock, wholesale_product)
VALUES
('Milk', 1.07, 10, true),
('Cream 400g', 2.71, 20, true),
('Yogurt 400g', 2.10, 7, true),
('Packed potatoes 1kg', 1.47, 30, false),
('Packed cabbage 1kg', 1.19, 15, false),
('Packed tomatoes 350g', 1.60, 50, false),
('Packed apples 1kg', 2.78, 18, false),
('Packed oranges 1kg', 3.20, 12, false),
('Packed bananas 1kg', 1.10, 25, true),
('Packed beef fillet 1kg', 12.80, 7, false),
('Packed pork fillet 1kg', 8.52, 14, false),
('Packed chicken breasts 1kg', 10.75, 18, false),
('Baguette 360g', 1.30, 10, true),
('Drinking water 1,5l', 0.80, 100, false),
('Olive oil 500ml', 5.30, 16, false),
('Sunflower oil 1l', 1.20, 12, false),
('Chocolate Ritter sport 100g', 1.10, 50, true),
('Paulaner 0,5l', 1.10, 100, false),
('Whiskey Jim Beam 1l', 13.99, 30, false),
('Whiskey Jack Daniels 1l', 17.19, 20, false);

CREATE TABLE public.discount_card
(
    id bigserial,
    "number" integer NOT NULL,
    discount_amount smallint NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT check_discount_amount CHECK (discount_amount >= 0 AND discount_amount <= 100) NOT VALID
);

ALTER TABLE IF EXISTS public.discount_card
    OWNER to check_app_manager;

INSERT INTO public.discount_card ("number", discount_amount)
VALUES
(1111, 3),
(2222, 3),
(3333, 4),
(4444, 5);


CREATE ROLE test_manager WITH
	LOGIN
	NOSUPERUSER
	CREATEDB
	NOCREATEROLE
	INHERIT
	NOREPLICATION
	CONNECTION LIMIT -1
	PASSWORD 'test';

CREATE DATABASE check_test
    WITH
    OWNER = test_manager
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

