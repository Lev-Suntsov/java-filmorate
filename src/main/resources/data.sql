DELETE FROM film_genres;
DELETE FROM likes;
DELETE FROM films;
DELETE FROM genres;
DELETE FROM mpa_rating;

INSERT INTO mpa_rating(rating_id, name)
VALUES (1, '0+'),
       (2, '6+'),
       (3, '12+'),
       (4, '16+'),
       (5, '18+');

INSERT INTO genres(genre_id, name)
VALUES (1, 'Комедия'),
       (2, 'Драма'),
       (3, 'Мультфильм'),
       (4, 'Триллер'),
       (5, 'Документальный'),
       (6, 'Боевик');
