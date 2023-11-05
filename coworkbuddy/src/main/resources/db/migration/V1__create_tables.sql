CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE SCHEMA IF NOT EXISTS coworkbuddy;

CREATE TABLE "users" (
                         id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                         username VARCHAR(255) UNIQUE NOT NULL,
                         email VARCHAR(255) UNIQUE,
                         password VARCHAR(255) NOT NULL
);

CREATE TABLE roles (
                       id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                       name VARCHAR(255) NOT NULL
);

INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO roles (name) VALUES ('USER');

CREATE TABLE users_roles (
                             id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                             user_id UUID REFERENCES "users"(id) ON DELETE CASCADE,
                             role_id UUID REFERENCES "roles"(id) ON DELETE CASCADE
);

CREATE TABLE "rooms" (
                         id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                         name VARCHAR(255) NOT NULL,
                         user_id UUID REFERENCES "users"(id) ON DELETE CASCADE
);

CREATE TABLE "tasks" (
                         id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                         name VARCHAR(255) NOT NULL,
                         url TEXT,
                         active BOOLEAN DEFAULT true,
                         room_id UUID REFERENCES "rooms"(id) ON DELETE CASCADE
);

CREATE TABLE "workers" (
                           id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                           name VARCHAR(255) NOT NULL,
                           active BOOLEAN DEFAULT true,
                           task_id UUID REFERENCES "tasks"(id) ON DELETE SET NULL,
                           room_id UUID REFERENCES "rooms"(id) ON DELETE CASCADE
);

CREATE TABLE "pairs" (
                         id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                         pair_id UUID NOT NULL,
                         worker_id UUID REFERENCES "workers"(id) ON DELETE CASCADE,
                         last_pairing_date TIMESTAMP
);

CREATE INDEX idx_pairs_worker_id ON "pairs" (worker_id);
CREATE INDEX idx_pairs_pair_id ON "pairs" (pair_id);
CREATE INDEX idx_pairs_last_pairing_date ON "pairs" (last_pairing_date);
