package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

@Repository
public class FilmRepository extends BasRepository<Film> {

    private static final String FIND_BY_ID_QUERY = """
        SELECT f.id,
               f.name,
               f.description,
               f.releaseDate,
               f.duration,
               f.mpa AS mpa_id,
               m.name AS mpa_name
        FROM films f
        LEFT JOIN mpa m ON f.mpa = m.id
        WHERE f.id = ?
        """;

    private static final String FIND_ALL_QUERY = """
        SELECT f.id,
               f.name,
               f.description,
               f.releaseDate,
               f.duration,
               f.mpa AS mpa_id,
               m.name AS mpa_name
        FROM films f
        LEFT JOIN mpa m ON f.mpa = m.id
        ORDER BY f.id
        """;

    private static final String INSERT_QUERY = """
        INSERT INTO films(name, description, releaseDate, mpa, duration)
        VALUES (?, ?, ?, ?, ?)
        """;

    private static final String UPDATE_QUERY = """
        UPDATE films
        SET name = ?, description = ?, releaseDate = ?, mpa = ?, duration = ?
        WHERE id = ?
        """;

    public FilmRepository(JdbcTemplate jdbc) {
        super(jdbc, (rs, rowNum) -> {
            Film film = new Film();
            film.setId(rs.getInt("id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("releaseDate").toLocalDate());
            film.setDuration(rs.getInt("duration"));

            int mpaId = rs.getInt("mpa_id");
            if (!rs.wasNull()) {
                Mpa mpa = new Mpa();
                mpa.setId(mpaId);
                mpa.setName(rs.getString("mpa_name"));
                film.setMpa(mpa);
            }

            film.setGenres(new LinkedHashSet<>());
            return film;
        });
    }

    public Optional<Film> findById(long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public List<Film> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Film save(Film film) {
        Integer id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getMpa().getId(),
                film.getDuration()
        );
        film.setId(id);
        return film;
    }

    public Film update(Film film) {
        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getMpa().getId(),
                film.getDuration(),
                film.getId()
        );
        return film;
    }

    public boolean existsMpaById(int id) {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM mpa WHERE id = ?",
                Integer.class,
                id
        );
        return count != null && count > 0;
    }
}
