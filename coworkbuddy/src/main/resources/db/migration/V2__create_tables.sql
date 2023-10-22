
CREATE TABLE "users" (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255),
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
               user_id UUID REFERENCES "users"(id),
               role_id UUID REFERENCES "roles"(id)
);
-- CREATE TABLE "rooms" (
--     id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
--     name VARCHAR(255)
-- );
--
-- CREATE TABLE "users_rooms" (
--     id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
--     name VARCHAR(255),
--     room_id UUID REFERENCES "rooms"(id),
--     user_id UUID REFERENCES "users"(id)
-- );
--
-- CREATE TABLE "workers" (
--     id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
--     name VARCHAR(255)
-- );
--
-- CREATE TABLE "tasks" (
--     id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
--     name VARCHAR(255)
-- );
--
-- CREATE TABLE "pairs" (
--     id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
--     name VARCHAR(255),
--     url TEXT,
--     task_id UUID REFERENCES "tasks"(id),
--     worker_id UUID REFERENCES "workers"(id),
--     last_pairing_date TIMESTAMP
-- );
