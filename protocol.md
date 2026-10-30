# MRP Protocol

Git: https://github.com/lolxt05/FHTW-Sem3-MRP

## Structure

The project is split into a few packages. `config` starts the server and opens the database connection. `controller` has one API class per resource, and the shared stuff like error handling, the token check and reading JSON is in `BaseApi`. `services` has the logic, so things like who owns what and which status code gets returned. `repository` is the data access. Every `Manager` is a singleton and uses a `DB` class and a `Cache` class that share an interface. `model` has `User`, `Media`, `Rating` and `Session` and the request classes that validate the JSON.

A request goes Server -> Api -> Service -> Manager -> DB and Cache.

## Endpoints

Users can register with `POST /users` and log in with `POST /sessions`, which gives back a token. Everything else needs the header `Authorization: Bearer <token>`.

`/users` has `GET /users?prefix=ab` for username completion and GET, PUT and DELETE on `/users/{id}`. You can only change or delete your own user.

`/media` has POST to add and GET to list everything, and `/media/{id}` has GET, PUT and DELETE. Same here, you can only change or delete your own entries.

## Decisions

There is no web framework, only the `HttpServer` from the JDK.

Login is `POST /sessions` because it creates a session and the password shouldn't be in a GET.

Passwords are hashed with bcrypt. That means the hash can't be compared in SQL, so it gets checked with `verifyPassword`.

Tokens are random and only stay in memory. They run out after 30 minutes without use.

All SQL uses `PreparedStatement` because of SQL injection.

The Manager writes to the DB first and then to the cache, so if the write fails the cache is still correct.

PostgreSQL runs in Docker with `compose.yaml` and the tables are in `init.sql`.
