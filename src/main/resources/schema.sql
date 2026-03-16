DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id INTEGER PRIMARY KEY,
    email VARCHAR(255),
    login VARCHAR(255),
    name VARCHAR(255),
    birthday DATE,
    friendsStatus VARCHAR(255),
    friendsList INT,
    LickedFilms INT
);

CREATE TABLE IF NOT EXISTS films (
    id INTEGER Not NULL,
    name VARCHAR(255),
    description VARCHAR(255),
    releaseDate DATE,
    genre VARCHAR(255),
    MPA VARCHAR(255)
);
