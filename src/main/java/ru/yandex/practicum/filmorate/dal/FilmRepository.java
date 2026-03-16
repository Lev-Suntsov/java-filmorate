package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

@Repository
public class FilmRepository extends BasRepository {
    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, releaseDate, genre, mpa, duration)" +
            "VALUES (?, ?, ?, ?, ?, ?) returning id";
    private static final String FIND_BY_ID = "SELECT * FROM films WHERE id = ?";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, releaseDate = ?, genre = ?, " +
            "mpa = ?, duration = ? WHERE id = ?";

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public List<Film> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<Film> findById(Long id) {
        return findOne(FIND_BY_ID, id);
    }

    public Film save(Film film) {
        int id = insert(INSERT_QUERY, film.getDescription(),
                film.getName(),
                film.getReleaseDate(),
                film.getGenre(),
                film.getMpa(),
                film.getDuration());
        film.setId(id);
        return film;
    }

    public Film update(Film film) {
        update(UPDATE_QUERY,
                film.getDuration(),
                film.getName(),
                film.getDescription(),
                film.getGenre(),
                film.getReleaseDate(),
                film.getMpa());
        return film;
    }
}
