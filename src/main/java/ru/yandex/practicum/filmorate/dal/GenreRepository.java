package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class GenreRepository extends BasRepository<Genre> {
    private static final String FIND_ALL_QUERY = "SELECT id, name FROM genres ORDER BY id";
    private static final String FIND_BY_ID_QUERY = "SELECT id, name FROM genres WHERE id = ?";

    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public List<Genre> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<Genre> findById(int id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public Set<Genre> findByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }

        String placeholders = ids.stream()
                .map(i -> "?")
                .collect(Collectors.joining(", "));

        String sql = "SELECT id, name FROM genres WHERE id IN (" + placeholders + ") ORDER BY id";

        return new HashSet<>(findMany(sql, ids.toArray()));
    }
}