
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE users (
   user_id UUID PRIMARY KEY,
   user_name VARCHAR(64) NOT NULL UNIQUE,
   user_pw VARCHAR(255) NOT NULL,
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
     star_rating INTEGER NOT NULL,
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







