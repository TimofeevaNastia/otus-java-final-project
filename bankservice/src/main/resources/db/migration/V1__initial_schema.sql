create table bank
(
    id bigserial not null primary key,
    name varchar(50),
    code varchar(50)
);

create table currency
(
    id bigserial not null primary key,
    code_digit int,
    code_name varchar(50),
    name varchar(50)
);

create table users_data
(
    id bigserial not null primary key,
    user_id bigserial,
    selected_bank varchar(50),
    selected_currency varchar(50),
    rate float,
    chat_id bigserial,
    scenario_id int
);

create table addresses
(
    id bigserial not null primary key,
    city varchar(50),
    street varchar(50),

    home varchar(50),
    building varchar(50),
    bank_id bigserial not null references bank (id)
);


INSERT INTO currency (code_digit, code_name, name) VALUES
(978, 'EUR', 'Евро'),
(643, 'RUB', 'Российский рубль'),
(840, 'USD', 'Доллар США');

INSERT INTO bank (code, name) VALUES
('alfa', 'Альфа банк'),
('sber', 'Сбербанк');

INSERT INTO addresses (city, street, home, building, bank_id) VALUES
('Москва', 'Новый Арбат', '56', null, 1),
('Москва', 'проспект Ленина', '346', null, 2);


