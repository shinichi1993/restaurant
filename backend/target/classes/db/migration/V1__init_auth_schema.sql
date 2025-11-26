-- ================================
-- V1 - INITIAL AUTH SCHEMA
-- MODULE 01 - Authentication & User
-- Không có phân quyền
-- ================================

-- 1) USERS TABLE
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    avatar_url VARCHAR(500),

    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    last_login_at TIMESTAMPTZ,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- 2) REFRESH TOKENS TABLE
CREATE TABLE refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,

    token TEXT NOT NULL,
    user_agent VARCHAR(255),
    ip_address VARCHAR(45),

    expired_at TIMESTAMPTZ NOT NULL,
    revoked BOOLEAN NOT NULL DEFAULT FALSE,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Index để tìm nhanh theo token
CREATE INDEX idx_refresh_token_token ON refresh_tokens(token);

-- 3) PASSWORD RESET TOKENS TABLE
CREATE TABLE password_reset_tokens (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,

    token TEXT NOT NULL,
    expired_at TIMESTAMPTZ NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Index để tìm token nhanh
CREATE INDEX idx_pwd_reset_token ON password_reset_tokens(token);
