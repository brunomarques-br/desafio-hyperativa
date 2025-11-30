INSERT INTO users (id, email, password, name, role, active, created_at)
VALUES (UUID(),
        'admin@hyperativa.com',
        '$2a$10$KaTotCzXa8qYNu4hxfVP/e5hFW5aWUf4PWbuBpelhX5B7VEk0S8di', -- admin123
        'Administrator',
        'ADMIN',
        true,
        NOW());