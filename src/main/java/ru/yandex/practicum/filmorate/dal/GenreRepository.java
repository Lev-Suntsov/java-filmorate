package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class GenreRepository extends BasRepository<Genre> {
    private static final String FIND_ALL_QUERY = "SELECT id, name FROM genres ORDER BY id";

    public GenreRepository(JdbcTemplate jdbc) {
        super(jdbc, (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getInt("id"));
            genre.setName(rs.getString("name"));
            return genre;
        });
    }

    public List<Genre> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Set<Genre> findByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return new LinkedHashSet<>();
        }

        String placeholders = ids.stream()
                .map(i -> "?")
                .collect(Collectors.joining(", "));

        String sql = "SELECT id, name FROM genres WHERE id IN (" + placeholders + ") ORDER BY id";

        return findMany(sql, ids.toArray()).stream()
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Optional<Genre> findById(int id) {
        String sql = "SELECT id, name FROM genres WHERE id = ?";
        return findOne(sql, id);
    }
}