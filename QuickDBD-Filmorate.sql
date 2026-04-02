-- Exported from QuickDBD: https://www.quickdatabasediagrams.com/
-- Link to schema: https://app.quickdatabasediagrams.com/#/d/0zIdmk
-- NOTE! If you have used non-SQL datatypes in your design, you will have to change these here.


CREATE TABLE "User" (
    "id" Int   NOT NULL,
    "email" varchar   NOT NULL,
    "login" varchar   NOT NULL,
    "name" varchar   NOT NULL,
    "birthday" date   NOT NULL,
    "friendsStatus" varchar   NOT NULL,
    "friendsList" integer   NOT NULL,
    "LickedFilms" integer   NOT NULL,
    CONSTRAINT "pk_User" PRIMARY KEY (
        "id"
     )
);

CREATE TABLE "film" (
    "id" integer   NOT NULL,
    "name" varchar   NOT NULL,
    "description" varchar   NOT NULL,
    "releaseDate" date   NOT NULL,
    "genre" varchar   NOT NULL,
    "MPA" varchar   NOT NULL,
    CONSTRAINT "pk_film" PRIMARY KEY (
        "id"
     )
);

