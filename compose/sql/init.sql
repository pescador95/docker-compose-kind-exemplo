CREATE USER "postgres" WITH PASSWORD 'postgres';

ALTER USER "postgres" WITH SUPERUSER;

CREATE DATABASE "postgres";

GRANT ALL PRIVILEGES ON DATABASE "postgres" TO "postgres";