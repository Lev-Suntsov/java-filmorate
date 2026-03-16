CREATE TABLE IF NOT EXISTS users (
    id INT NOT NULL,
    email VARCHAR(255) NOT NULL,
    login VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    birthday DATE NOT NULL,
    friendsStatus VARCHAR(255) NOT NULL,
    friendsList INT NOT NULL,
    LickedFilms INT NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS films (
    id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    releaseDate DATE NOT NULL,
    genre VARCHAR(255) NOT NULL,
    MPA VARCHAR(255) NOT NULL,
    CONSTRAINT pk_film PRIMARY KEY (id)
);
