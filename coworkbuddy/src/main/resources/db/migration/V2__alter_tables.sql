ALTER TABLE "users"
    ADD COLUMN creation_date TIMESTAMP;
ALTER TABLE "rooms"
    ADD COLUMN creation_date TIMESTAMP;
ALTER TABLE "tasks"
    ADD COLUMN creation_date TIMESTAMP;
ALTER TABLE "workers"
    ADD COLUMN creation_date TIMESTAMP;