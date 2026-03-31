package ru.yandex.practicum.filmorate.dal;

import jakarta.persistence.EntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FilmGenreRepository {
    private final JdbcTemplate jdbc;
    private final EntityManager entityManager;

    public FilmGenreRepository(JdbcTemplate jdbc, EntityManager entityManager) {
        this.jdbc = jdbc;
        this.entityManager = entityManager;
    }

    public void saveForFilm(long filmId, List<Integer> genreIds) {
        jdbc.update("DELETE FROM film_genres WHERE film_id = ?", filmId);

        if (genreIds == null || genreIds.isEmpty()) {
            return;
        }

        List<Integer> uniqueGenreIds = genreIds.stream()
                .distinct()
                .toList();

        String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";

        jdbc.batchUpdate(sql, uniqueGenreIds, uniqueGenreIds.size(),
                (ps, genreId) -> {
                    ps.setLong(1, filmId);
                    ps.setInt(2, genreId);
                });
    }

    public List<Integer> findGenreIdsByFilmId(long filmId) {
        String sql = "SELECT genre_id FROM film_genres WHERE film_id = ? ORDER BY genre_id";
        return jdbc.queryForList(sql, Integer.class, filmId);
    }

}
