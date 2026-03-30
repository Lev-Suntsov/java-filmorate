package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
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

    private final NamedParameterJdbcTemplate namedJdbc;

    public LikeRepository(JdbcTemplate jdbc, NamedParameterJdbcTemplate namedJdbc) {
        this.jdbc = jdbc;
        this.namedJdbc = namedJdbc;
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

    public List<Object[]> countLikesByFilmIds(List<Integer> filmIds) {
        if (filmIds.isEmpty()) {
            return Collections.emptyList();
        }

        // В SQL используем именованный параметр :ids
        String sql = "SELECT film_id, COUNT(user_id) FROM likes WHERE film_id IN (:ids) GROUP BY film_id";

        MapSqlParameterSource parameters = new MapSqlParameterSource("ids", filmIds);

        return namedJdbc.query(sql, parameters, (rs, rowNum) -> new Object[] {
                rs.getLong(1), // film_id
                rs.getLong(2)  // count
        });
    }



    public List<Long> findFilmIdsByUserId(Long userId) {
        return jdbc.queryForList(FIND_FILM_IDS_BY_USER_QUERY, Long.class, userId);
    }
}


