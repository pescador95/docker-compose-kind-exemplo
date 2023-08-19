CREATE USER "quarkusapp" WITH PASSWORD 'quarkusapp';

ALTER USER "quarkusapp" WITH SUPERUSER;

CREATE DATABASE "quarkusapp";

GRANT ALL PRIVILEGES ON DATABASE "quarkusapp" TO "quarkusapp";