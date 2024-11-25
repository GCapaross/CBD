InvalidRequest: Error from server: code=2200 [Invalid query] message="User-defined functions are disabled in cassandra.yaml - set user_defined_functions_enabled=true to enable"
Need to enable this

```bash
DROP KEYSPACE IF EXISTS GameStoreDB;
CREATE KEYSPACE IF NOT EXISTS GameStoreDB
WITH REPLICATION = {'class' : 'SimpleStrategy', 'replication_factor' : 3};

USE GameStoreDB;

CREATE TABLE IF NOT EXISTS GameStoreDB.Games (
    game_id UUID PRIMARY KEY,
    game_name TEXT,
    genre TEXT,
    release_date DATE,
    tags SET<TEXT>,
    prices MAP<TEXT, DOUBLE>
);

-- INSERTS

INSERT INTO GameStoreDB.Games (game_id, game_name, genre, release_date, tags, prices)
VALUES (uuid(), 'Cyberpunk 2077', 'RPG', '2020-12-10', {'sci-fi', 'open-world'}, {'USD': 59.99, 'EUR': 49.99});

INSERT INTO GameStoreDB.Games (game_id, game_name, genre, release_date, tags, prices)
VALUES (uuid(), 'Minecraft', 'Sandbox', '2011-11-18', {'creative', 'multiplayer'}, {'USD': 26.95, 'EUR': 21.95});

INSERT INTO GameStoreDB.Games (game_id, game_name, genre, release_date, tags, prices)
VALUES (uuid(), 'Valorant', 'Shooter', '2020-06-02', {'multiplayer', 'competitive'}, {'USD': 0.0, 'EUR': 0.0});

INSERT INTO GameStoreDB.Games (game_id, game_name, genre, release_date, tags, prices)
VALUES (uuid(), 'Elden Ring', 'RPG', '2022-02-25', {'fantasy', 'hardcore'}, {'USD': 59.99, 'EUR': 59.99});

INSERT INTO GameStoreDB.Games (game_id, game_name, genre, release_date, tags, prices)
VALUES (uuid(), 'Among Us', 'Party', '2018-11-16', {'multiplayer', 'social'}, {'USD': 4.99, 'EUR': 3.99});


CREATE TABLE IF NOT EXISTS GameStoreDB.Stores (
    store_id UUID PRIMARY KEY,
    store_name TEXT,
    location TEXT,
    game_list LIST<UUID>
);

-- INSERTS

INSERT INTO GameStoreDB.Stores (store_id, store_name, location, game_list)
VALUES (uuid(), 'GameStore2', 'Los Angeles', [uuid(), uuid()]);

INSERT INTO GameStoreDB.Stores (store_id, store_name, location, game_list)
VALUES (uuid(), 'GameStore3', 'San Francisco', [uuid(), uuid()]);

INSERT INTO GameStoreDB.Stores (store_id, store_name, location, game_list)
VALUES (uuid(), 'GameStore4', 'Chicago', [uuid(), uuid()]);

INSERT INTO GameStoreDB.Stores (store_id, store_name, location, game_list)
VALUES (uuid(), 'GameStore5', 'Seattle', [uuid(), uuid()]);

INSERT INTO GameStoreDB.Stores (store_id, store_name, location, game_list)
VALUES (uuid(), 'GameStore6', 'Miami', [uuid(), uuid()]);


CREATE TABLE IF NOT EXISTS GameStoreDB.Users (
    user_id UUID PRIMARY KEY,
    username TEXT,
    email TEXT,
    purchase_history LIST<UUID>
);

-- INSERTS

INSERT INTO GameStoreDB.Users (user_id, username, email, purchase_history)
VALUES (uuid(), 'proGamer99', 'progamer99@example.com', [uuid(), uuid()]);

INSERT INTO GameStoreDB.Users (user_id, username, email, purchase_history)
VALUES (uuid(), 'casualPlayer', 'casual@example.com', [uuid()]);

INSERT INTO GameStoreDB.Users (user_id, username, email, purchase_history)
VALUES (uuid(), 'streamer123', 'streamer123@example.com', [uuid(), uuid(), uuid()]);

INSERT INTO GameStoreDB.Users (user_id, username, email, purchase_history)
VALUES (uuid(), 'speedRunner', 'speedrunner@example.com', [uuid()]);

INSERT INTO GameStoreDB.Users (user_id, username, email, purchase_history)
VALUES (uuid(), 'retroGamer', 'retrogamer@example.com', [uuid()]);



DROP TABLE IF EXISTS GameStoreDB.Purchases;

CREATE TABLE IF NOT EXISTS GameStoreDB.Purchases (
    user_id UUID,
    purchase_id UUID,
    game_id UUID,
    purchase_date DATE,
    amount DOUBLE,
    PRIMARY KEY (user_id, purchase_id)
);

-- INSERTS

INSERT INTO GameStoreDB.Purchases (purchase_id, user_id, game_id, purchase_date, amount)
VALUES (uuid(), uuid(), uuid(), '2024-11-20', 19.99);

INSERT INTO GameStoreDB.Purchases (purchase_id, user_id, game_id, purchase_date, amount)
VALUES (uuid(), uuid(), uuid(), '2024-11-22', 59.99);

INSERT INTO GameStoreDB.Purchases (purchase_id, user_id, game_id, purchase_date, amount)
VALUES (uuid(), uuid(), uuid(), '2024-11-23', 4.99);

INSERT INTO GameStoreDB.Purchases (purchase_id, user_id, game_id, purchase_date, amount)
VALUES (uuid(), uuid(), uuid(), '2024-11-24', 49.99);

INSERT INTO GameStoreDB.Purchases (purchase_id, user_id, game_id, purchase_date, amount)
VALUES (uuid(), uuid(), uuid(), '2024-11-25', 0.0);


-- UDF

CREATE FUNCTION IF NOT EXISTS calculate_discounted_price(price double, discount double)
RETURNS NULL ON NULL INPUT
RETURNS double
LANGUAGE java AS '
    return price * (1 - discount / 100);
';

-- UDA 

CREATE FUNCTION IF NOT EXISTS GameStoreDB.sum_amounts(state Double, amount Double)
CALLED ON NULL INPUT
RETURNS Double
LANGUAGE java AS '
    if (state == null) return amount;
    if (amount == null) return state;
    return state + amount;
';


CREATE AGGREGATE IF NOT EXISTS GameStoreDB.calculate_total_spent(double)
SFUNC sum_amounts
STYPE double
INITCOND 0;

SELECT game_name, GameStoreDB.calculate_discounted_price(prices['USD'], 10) AS discounted_price
FROM GameStoreDB.Games;

SELECT GameStoreDB.calculate_total_spent(amount) AS total_spent
FROM GameStoreDB.Purchases
WHERE user_id =  a8ccb14a-13a5-4332-baa3-a56265d485b8;

SELECT * FROM USERS; -- And choose an ID after

```

## INDEXES 
```bash
CREATE INDEX IF NOT EXISTS game_name_idx ON GameStoreDB.Games(game_name);
CREATE INDEX IF NOT EXISTS store_location_idx ON GameStoreDB.Stores(location);
CREATE INDEX IF NOT EXISTS genre_idx ON GameStoreDB.Games (genre);
CREATE INDEX IF NOT EXISTS tags_idx ON GameStoreDB.Games (tags);
CREATE INDEX IF NOT EXISTS release_date_idx ON GameStoreDB.Games(release_date);

```


## DELETES
```bash
-- DELETES / SELECT * FROM Games / SELECT * FROM Stores / SELECT * FROM Users / SELECT * FROM Purchases

DELETE FROM GameStoreDB.Stores WHERE store_id = 123e4567-e89b-12d3-a456-426614174000;
DELETE FROM GameStoreDB.Purchases WHERE user_id = 1f362205-6e95-4519-a4ef-067c3813528b AND purchase_id = 58d83b82-fcb1-4b2d-b7a3-7f850b5192a7;
DELETE FROM GameStoreDB.Users WHERE user_id = 123e4567-e89b-12d3-a456-426614174000;
DELETE FROM GameStoreDB.Games WHERE game_id = 550e8400-e29b-41d4-a716-446655440000;
```

## UPDATES
```bash
-- UPDATES / SELECT * FROM Games / SELECT * FROM Stores / SELECT * FROM Users / SELECT * FROM Purchases
UPDATE GameStoreDB.Games SET tags = tags + {'single-player'} WHERE game_id = uuid();
UPDATE GameStoreDB.Stores SET store_name = 'GameStore2' WHERE store_id = uuid();
UPDATE GameStoreDB.Users SET email = 'newemail@example.com' WHERE user_id = uuid();
UPDATE GameStoreDB.Games SET prices = prices + {'USD': 49.99} WHERE game_id = uuid();
UPDATE GameStoreDB.Games SET tags = tags + {'multiplayer'} WHERE game_id = uuid();

UPDATE GameStoreDB.Stores SET game_list = game_list - [550e8400-e29b-41d4-a716-446655440000] 
WHERE store_id = 123e4567-e89b-12d3-a456-426614174000;
```




## QUERIES

### QUERY 1 - All games from a specified genre
**Command**
```bash
SELECT * FROM GameStoreDB.Games
WHERE genre = 'RPG'
LIMIT 10;
```
**Result**
```bash
 game_id                              | game_name      | genre | prices                       | release_date | tags
--------------------------------------+----------------+-------+------------------------------+--------------+--------------------------
 6dadfa48-8078-43b3-8a59-a9b52ba634bb | Cyberpunk 2077 |   RPG | {'EUR': 49.99, 'USD': 59.99} |   2020-12-10 | {'open-world', 'sci-fi'}
 f33948a2-f40f-418a-901a-4e9bf7abca85 |     Elden Ring |   RPG | {'EUR': 59.99, 'USD': 59.99} |   2022-02-25 |  {'fantasy', 'hardcore'}
```
### QUERY 2 - Games by tag
**Command**
```bash
SELECT game_name, tags 
FROM GameStoreDB.Games 
WHERE tags CONTAINS 'open-world' 
LIMIT 5;
```
**Result**
```bash
 game_name      | tags
----------------+--------------------------
 Cyberpunk 2077 | {'open-world', 'sci-fi'}
```
### QUERY 3
**Command**
```bash
SELECT game_name, prices
FROM GameStoreDB.Games
WHERE prices['USD'] = 59.99 
LIMIT 10
ALLOW FILTERING;
```
- I was avoiding creating other tables
**Result**
```bash
 game_name      | prices
----------------+------------------------------
 Cyberpunk 2077 | {'EUR': 49.99, 'USD': 59.99}
     Elden Ring | {'EUR': 59.99, 'USD': 59.99}
```
### QUERY 4
**Command**
```bash
SELECT game_name, genre, release_date
FROM GameStoreDB.Games
WHERE genre = 'RPG'
AND release_date >= '2010-01-01'
LIMIT 5 ALLOW FILTERING;
```
**Result**
```bash
 game_name      | genre | release_date
----------------+-------+--------------
 Cyberpunk 2077 |   RPG |   2020-12-10
     Elden Ring |   RPG |   2022-02-25
```
- Se eu tiver que criar uma tabela auxiliar nova, tenho que criar mais dados, assim como se tiver que alterar uma tabela.
### QUERY 5
**Command**
```bash
SELECT username, email
FROM GameStoreDB.Users
WHERE user_id = uuid()
LIMIT 5;
```
- Do a SELECT * FROM TABLE and substitute in uuid
**Result**
```bash
 username | email
----------+-------
```
### QUERY 6
**Command**
```bash
SELECT game_name, genre, prices 
FROM GameStoreDB.Games 
WHERE game_id = 1bbd2a17-9fd9-42ce-8d90-05e915409ff6;
```
**Result**
```bash
 game_name | genre   | prices
-----------+---------+------------------------------
 Minecraft | Sandbox | {'EUR': 21.95, 'USD': 26.95}

```
### QUERY 7
**Command**
```bash
SELECT store_name, location 
FROM GameStoreDB.Stores 
WHERE location = 'Chicago' 
LIMIT 3;
```
**Result**
```bash
 store_name | location
------------+----------
 GameStore4 |  Chicago
```
### QUERY 8
**Command**
```bash
SELECT store_name, location
FROM GameStoreDB.Stores
WHERE store_id = uuid();
```
**Result**
```bash
 store_name | location
------------+----------
```
### QUERY 9
**Command**
```bash
SELECT game_id, user_id, purchase_date
FROM GameStoreDB.Purchases
WHERE amount > 10
ALLOW FILTERING;
```
**Result**
```bash
 game_id                              | user_id                              | purchase_date
--------------------------------------+--------------------------------------+---------------
 86de1bff-82ea-4c04-9935-be3b5d48a435 | eb23155a-5fe6-4a19-a2fc-7946b8bb08e3 |    2024-11-20
 059ea3a2-0578-4b5b-8acb-667572efffee | b499cf00-f425-405b-84a0-aeefe911660e |    2024-11-24
 ba8141ee-dd4a-4ca8-9266-4685d46d1755 | 7b414629-6c6f-43ea-876a-e107e43ef182 |    2024-11-22
```
### QUERY 10
**Command**
```bash
SELECT * 
FROM GameStoreDB.Users 
WHERE user_id IN (
    e70d10d5-2d1e-4a22-9f5e-b2da4204e198,
    3e76d98e-3526-44d0-bc84-2ea865f2d9e6
);
```
**Result**
```bash
 user_id | email | purchase_history | username
---------+-------+------------------+----------
```
