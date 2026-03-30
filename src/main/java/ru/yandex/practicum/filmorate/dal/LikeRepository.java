package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LikeRepository {

    private final JdbcTemplate jdbc;

    private static final String INSERT_QUERY =
            "INSERT INTO likes (user_id, film_id) VALUES (?, ?)";

    private static final String DELETE_QUERY =
            "DELETE FROM likes WHERE user_id = ? AND film_id = ?";

    private static final String EXISTS_QUERY =
            "SELECT COUNT(*) FROM likes WHERE user_id = ? AND film_id = ?";

    private static final String COUNT_BY_FILM_QUERY =
            "SELECT COUNT(*) FROM likes WHERE film_id = ?";

    private static final String FIND_FILM_IDS_BY_USER_QUERY =
            "SELECT film_id FROM likes WHERE user_id = ?";

    public LikeRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void addLike(Long userId, Long filmId) {
        jdbc.update(INSERT_QUERY, userId, filmId);
    }

    public void removeLike(Long userId, Long filmId) {
        jdbc.update(DELETE_QUERY, userId, filmId);
    }

    public boolean exists(Long userId, Long filmId) {
        Integer count = jdbc.queryForObject(EXISTS_QUERY, Integer.class, userId, filmId);
        return count != null && count > 0;
    }

    public long countLikesByFilmId(Long filmId) {
        Integer count = jdbc.queryForObject(COUNT_BY_FILM_QUERY, Integer.class, filmId);
        return count == null ? 0 : count;
    }

    public List<Long> findFilmIdsByUserId(Long userId) {
        return jdbc.queryForList(FIND_FILM_IDS_BY_USER_QUERY, Long.class, userId);
    }
}


