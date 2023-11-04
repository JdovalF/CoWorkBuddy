CREATE TABLE "rooms" (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    user_id UUID REFERENCES "users"(id) ON DELETE CASCADE
);

CREATE TABLE "workers" (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    active BOOLEAN default true,
    room_id UUID REFERENCES "rooms"(id) ON DELETE CASCADE
);

CREATE TABLE "tasks" (
     id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
     name VARCHAR(255) NOT NULL,
     active BOOLEAN default true,
     room_id UUID REFERENCES "rooms"(id) ON DELETE CASCADE
);

ALTER TABLE users_roles DROP CONSTRAINT IF EXISTS users_roles_user_id_fkey;
ALTER TABLE users_roles
    ADD CONSTRAINT users_roles_user_id_fkey
        FOREIGN KEY (user_id) REFERENCES "users"(id) ON DELETE CASCADE;

