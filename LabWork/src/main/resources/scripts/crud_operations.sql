
-- Users CRUD Operations
INSERT INTO users (login, role, password, enabled) VALUES (?, ?, ?, ?);
SELECT * FROM users WHERE id = ?;
SELECT * FROM users WHERE login = ?;
UPDATE users SET password = ? WHERE id = ?;
UPDATE users SET role = ? WHERE id = ?;
UPDATE users SET enabled = ? WHERE id = ?;
DELETE FROM users WHERE id = ?;

-- Functions CRUD Operations
INSERT INTO functions (user_id, name, signature) VALUES (?, ?, ?) RETURNING id;
SELECT * FROM functions WHERE id = ?;
SELECT * FROM functions WHERE user_id = ?;
UPDATE functions SET name = ? WHERE id = ?;
UPDATE functions SET signature = ? WHERE id = ?;
DELETE FROM functions WHERE id = ?;

-- Points CRUD Operations
INSERT INTO points (function_id, x_value, y_value) VALUES (?, ?, ?);
SELECT * FROM points WHERE id = ?;
SELECT * FROM points WHERE function_id = ?;
UPDATE points SET x_value = ?, y_value = ? WHERE id = ?;
DELETE FROM points WHERE id = ?;
DELETE FROM points WHERE function_id = ?;