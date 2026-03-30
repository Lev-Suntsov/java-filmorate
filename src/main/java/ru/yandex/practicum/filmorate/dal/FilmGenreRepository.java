package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FilmGenreRepository {
    private final JdbcTemplate jdbc;

    public FilmGenreRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void saveForFilm(long filmId, List<Integer> genreIds) {
        jdbc.update("DELETE FROM film_genres WHERE film_id = ?", filmId);
        if (genreIds == null || genreIds.isEmpty()) return;

        String sql = "INSERT INTO film_genres(film_id, genre_id) VALUES (?, ?)";
        for (Integer gid : genreIds) {
            jdbc.update(sql, filmId, gid);
        }
    }

    public List<Integer> findGenreIdsByFilmId(long filmId) {
        String sql = "SELECT genre_id FROM film_genres WHERE film_id = ? ORDER BY genre_id";
        return jdbc.queryForList(sql, Integer.class, filmId);
    }

}
