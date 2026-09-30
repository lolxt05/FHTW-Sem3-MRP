-- stolen from the internet
-- no ai and altered to fit me
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'db_migrator') THEN
        CREATE ROLE db_migrator LOGIN PASSWORD '12345678';
    END IF;

    IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'db_rw') THEN
        CREATE ROLE db_rw LOGIN PASSWORD '12345678';
    END IF;

    IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'db_ro') THEN
        CREATE ROLE db_ro LOGIN PASSWORD '12345678';
    END IF;
END
$$;

CREATE DATABASE prod;

\connect project_db

-- Remove unsafe default privileges
REVOKE ALL ON DATABASE project_db FROM PUBLIC;
REVOKE CREATE ON SCHEMA public FROM PUBLIC;

-- Prisma requires the db_migrator to own the schema
ALTER SCHEMA public OWNER TO db_migrator;

-- db_Migrator full control
GRANT USAGE, CREATE ON SCHEMA public TO db_migrator;

-- Read/Write application user
GRANT USAGE ON SCHEMA public TO db_rw;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO db_rw;
GRANT USAGE, SELECT, UPDATE ON ALL SEQUENCES IN SCHEMA public TO db_rw;

-- Readonly user
GRANT USAGE ON SCHEMA public TO db_ro;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO db_ro;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO db_ro;

-- Default privileges for FUTURE objects created by db_migrator
ALTER DEFAULT PRIVILEGES FOR ROLE db_migrator IN SCHEMA public
    GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO db_rw;

ALTER DEFAULT PRIVILEGES FOR ROLE db_migrator IN SCHEMA public
    GRANT SELECT ON TABLES TO db_ro;

ALTER DEFAULT PRIVILEGES FOR ROLE db_migrator IN SCHEMA public
    GRANT USAGE, SELECT, UPDATE ON SEQUENCES TO db_rw;

ALTER DEFAULT PRIVILEGES FOR ROLE db_migrator IN SCHEMA public
    GRANT USAGE, SELECT ON SEQUENCES TO db_ro;

-- Allow connections
GRANT CONNECT ON DATABASE project_db TO db_migrator, db_rw, db_ro;

-------------------------------
-- Setup for prod database
-------------------------------
\connect prod

REVOKE ALL ON DATABASE prod FROM PUBLIC;
REVOKE CREATE ON SCHEMA public FROM PUBLIC;

ALTER SCHEMA public OWNER TO db_migrator;
GRANT USAGE, CREATE ON SCHEMA public TO db_migrator;

GRANT USAGE ON SCHEMA public TO db_rw, db_ro;

GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO db_rw;
GRANT USAGE, SELECT, UPDATE ON ALL SEQUENCES IN SCHEMA public TO db_rw;

GRANT SELECT ON ALL TABLES IN SCHEMA public TO db_ro;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO db_ro;

ALTER DEFAULT PRIVILEGES FOR ROLE db_migrator IN SCHEMA public
    GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO db_rw;

ALTER DEFAULT PRIVILEGES FOR ROLE db_migrator IN SCHEMA public
    GRANT SELECT ON TABLES TO db_ro;

ALTER DEFAULT PRIVILEGES FOR ROLE db_migrator IN SCHEMA public
    GRANT USAGE, SELECT, UPDATE ON SEQUENCES TO db_rw;

ALTER DEFAULT PRIVILEGES FOR ROLE db_migrator IN SCHEMA public
    GRANT USAGE, SELECT ON SEQUENCES TO db_ro;

GRANT CONNECT ON DATABASE prod TO db_migrator, db_rw, db_ro;

-- this is my setup script

CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE users (
   user_id UUID PRIMARY KEY,
   user_name VARCHAR(64) NOT NULL UNIQUE,
   user_pw CHAR(64) NOT NULL,
   avg_stars NUMERIC(3, 2) NOT NULL
);

CREATE TABLE media (
   media_id UUID PRIMARY KEY,
   media_title VARCHAR(64) NOT NULL,
   media_description VARCHAR(256),
   media_type SMALLINT NOT NULL,
   creator_id UUID NOT NULL,
   release_year INTEGER NOT NULL,
   genre SMALLINT NOT NULL,
   min_age INTEGER NOT NULL,
   avg_score NUMERIC(3, 2) NOT NULL,
   favorite_count INTEGER NOT NULL,
   FOREIGN KEY (creator_id)
       REFERENCES users(user_id)
       ON DELETE CASCADE
);

CREATE TABLE ratings (
     rating_id UUID PRIMARY KEY,
     creator_id UUID NOT NULL,
     media_id UUID NOT NULL,
     star_rating INTEGER NOT NULL
     timestamp TIMESTAMPTZ NOT NULL,
     likes INTEGER NOT NULL,
     confirmed_flag INTEGER NOT NULL,
     comment VARCHAR(256),

     FOREIGN KEY (creator_id)
         REFERENCES users(user_id)
         ON DELETE CASCADE,

     FOREIGN KEY (media_id)
         REFERENCES media(media_id)
         ON DELETE CASCADE
);

CREATE TABLE user_favorites (
    user_id UUID NOT NULL,
    media_id UUID NOT NULL,

    PRIMARY KEY (user_id, media_id),

    FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE,

    FOREIGN KEY (media_id)
        REFERENCES media(media_id)
        ON DELETE CASCADE
);

CREATE TABLE rating_likes (
    user_id UUID NOT NULL,
    rating_id UUID NOT NULL,

    PRIMARY KEY (user_id, rating_id),

    FOREIGN KEY (user_id)
      REFERENCES users(user_id)
      ON DELETE CASCADE,

    FOREIGN KEY (rating_id)
      REFERENCES ratings(rating_id)
      ON DELETE CASCADE
);

CREATE INDEX idx_media_creator
    ON media(creator_id);

CREATE INDEX idx_media_genre
    ON media(genre);

CREATE INDEX idx_media_type
    ON media(media_type);

CREATE INDEX idx_ratings_creator
    ON ratings(creator_id);

CREATE INDEX idx_ratings_media
    ON ratings(media_id);

CREATE INDEX idx_user_favorites_media
    ON user_favorites(media_id);

CREATE INDEX idx_rating_likes_rating
    ON rating_likes(rating_id);






