package ru.yandex.practicum.filmorate.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("id"));
        film.setName(rs.getString("name"));
        film.setMpa(Mpa.fromId(rs.getInt("mpaId")));
        film.setDescription(rs.getString("description"));
        film.setDuration(Duration.ofHours(rs.getTime("duration").getHours()).plusSeconds(rs.getTime("duration").getSeconds())
                .plusMinutes(rs.getTime("duration").getMinutes()));
        film.setReleaseDate(rs.getDate("releaseDate").toLocalDate());
        film.setGenre(Genre.fromId(rs.getInt("genreIds")));
        return film;
    }
}
