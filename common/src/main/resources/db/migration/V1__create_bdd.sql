-- V1__initial_schema.sql

-- 1) Creamos la tabla de usuarios
CREATE TABLE users (
  id       BIGINT   AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  enabled  BOOLEAN  NOT NULL DEFAULT TRUE
);

-- 2) Semilla de admin
INSERT INTO users (username, password, enabled)
VALUES (
  'admin',
  '$2a$10$HLCU7Azr7j2myu4DKV4tru1IOB0mFf094QN8Cn3ct2FNKfHSbWBfi', 
  TRUE
);

-- 3) Creamos la tabla de customers
CREATE TABLE customers (
  id            BIGINT       AUTO_INCREMENT PRIMARY KEY,
  first_name    VARCHAR(100) NOT NULL,
  last_name     VARCHAR(100) NOT NULL,
  age           INT          NOT NULL,
  date_of_birth DATE         NOT NULL,
  created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at    TIMESTAMP    NULL        ON UPDATE CURRENT_TIMESTAMP
);

