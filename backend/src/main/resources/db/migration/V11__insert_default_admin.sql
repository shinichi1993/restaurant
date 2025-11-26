INSERT INTO users (
    email,
    password_hash,
    full_name,
    phone,
    avatar_url,
    is_active,
    created_at,
    updated_at
) VALUES 
(
    'admin@example.com',
    '$2a$10$Hj0c9c6y2A7V.AY5L7nXPeP2X2VINeodIZ6hO6PvxI6Bfq5lHppZC', -- password = 123456
    'Administrator',
    '0123456789',
    NULL,
    TRUE,
    NOW(),
    NOW()
);
INSERT INTO users (
    email,
    password_hash,
    full_name,
    phone,
    avatar_url,
    is_active,
    created_at,
    updated_at
) VALUES 
(
    'a',
    '$2a$10$h2qxKVL6uC62zfwZ2tWKS.5xvnMlxhEl0O1Iyo5YzjfVMPc2HX5gG', -- password = a
    'Administrator',
    '0123456789',
    NULL,
    TRUE,
    NOW(),
    NOW()
);