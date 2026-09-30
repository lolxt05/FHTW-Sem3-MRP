CREATE TABLE IF NOT EXISTS "Media" (
	"mediaID" CHAR(36) NOT NULL,
	"mediaTitle" CHAR(64),
	"mediaDescription" CHAR(256),
	"mediaType" SMALLINT,
	"creatorId" CHAR(36),
	"releaseYear" SMALLINT,
	"genre" SMALLINT,
	"minAge" SMALLINT,
	"avgScore" DECIMAL,
	"FaviroteCount" INTEGER,
	PRIMARY KEY("mediaID")
);

CREATE TABLE IF NOT EXISTS "Ratings" (
	"ratingId" CHAR(36) NOT NULL,
	"creatorId" CHAR(36),
	"mediaId" CHAR(36),
	"rating" SMALLINT,
	"timestamp" DATE,
	"likes" INTEGER,
	"confirmedFlag" BOOLEAN,
	"comment" CHAR(256),
	PRIMARY KEY("ratingId")
);

CREATE TABLE IF NOT EXISTS "Users" (
	"userId" CHAR(36) NOT NULL,
	"userName" CHAR(64),
	"pwHash" CHAR(32),
	"avgStars" SMALLINT,
	PRIMARY KEY("userId")
);