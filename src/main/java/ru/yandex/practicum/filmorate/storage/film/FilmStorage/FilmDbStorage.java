package ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.FilmGenreRepository;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FilmDbStorage implements FilmStorage {
    private static final Logger logger = LoggerFactory.getLogger(FilmDbStorage.class);

    private final FilmRepository repository;
    private final FilmGenreRepository filmGenreRepository;
    private final GenreRepository genreRepository;

    public FilmDbStorage(FilmRepository repository,
                         FilmGenreRepository filmGenreRepository,
                         GenreRepository genreRepository) {
        this.repository = repository;
        this.filmGenreRepository = filmGenreRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public Collection<Film> getFilms() {
        logger.info("Получаем список всех фильмов");
        List<Film> films = repository.findAll();
        films.forEach(this::loadGenres);
        return films;
    }

    @Override
    public FilmDto addFilm(NewFilmRequest request) throws ValidationException {
        validateFilmRequest(request);

        Film film = FilmMapper.mapToFilm(request);

        validateMpaAndGenres(film);

        film = repository.save(film);

        List<Integer> genreIds = film.getGenres().stream()
                .map(Genre::getId)
                .distinct()
                .toList();

        filmGenreRepository.saveForFilm(film.getId(), genreIds);

        return getFilmById(film.getId());
    }

    private List<Integer> extractGenreIds(Film film) {
        return Optional.ofNullable(film.getGenres())
                .orElse(Collections.emptySet())
                .stream()
                .map(Genre::getId)
                .distinct()
                .toList();
    }

    @Override
    public FilmDto updateFilm(long id, UpdateFilmRequest request) throws ValidationException {
        logger.info("Переходим в метод обновления фильма");

        Film film = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("фильм не найден"));

        film = FilmMapper.updateFilm(film, request);

        validateFilmEntity(film);
        validateMpaAndGenres(film);

        film = repository.update(film);

        List<Integer> genreIds = film.getGenres() == null
                ? List.of()
                : film.getGenres().stream()
                .map(Genre::getId)
                .distinct()
                .toList();

        filmGenreRepository.saveForFilm(film.getId(), genreIds);

        return getFilmById(film.getId());
    }

    @Override
    public FilmDto getFilmById(long id) {
        Film film = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("фильм не найден"));

        loadGenres(film);
        return FilmMapper.mapToFilmDto(film);
    }

    private void loadGenres(Film film) {
        List<Integer> genreIds = filmGenreRepository.findGenreIdsByFilmId(film.getId());

        if (genreIds.isEmpty()) {
            film.setGenres(new LinkedHashSet<>());
        } else {
            Set<Genre> genres = genreRepository.findByIds(genreIds).stream()
                    .sorted(Comparator.comparing(Genre::getId))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            film.setGenres(genres);
        }
    }

    private void validateMpaAndGenres(Film film) throws ValidationException {
        if (film.getMpa() == null || film.getMpa().getId() == null) {
            throw new ValidationException("Укажите рейтинг MPA");
        }

        if (!repository.existsMpaById(film.getMpa().getId())) {
            throw new NotFoundException("MPA не найден");
        }

        Set<Genre> genres = film.getGenres() == null ? Set.of() : film.getGenres();

        List<Integer> ids = genres.stream()
                .map(Genre::getId)
                .distinct()
                .toList();

        if (!ids.isEmpty()) {
            Set<Genre> foundGenres = genreRepository.findByIds(ids);
            if (foundGenres.size() != ids.size()) {
                throw new NotFoundException("Один или несколько жанров не найдены");
            }
            film.setGenres(foundGenres.stream()
                    .sorted(Comparator.comparing(Genre::getId))
                    .collect(Collectors.toCollection(LinkedHashSet::new)));
        } else {
            film.setGenres(new LinkedHashSet<>());
        }
    }

    private void validateFilmRequest(NewFilmRequest request) throws ValidationException {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new ValidationException("Укажите имя фильма");
        }
        if (request.getDescription() == null || request.getDescription().isBlank()) {
            throw new ValidationException("Укажите описание фильма");
        }
        if (request.getDescription().length() > 200) {
            throw new ValidationException("Описание не может быть больше 200 символов");
        }
        if (request.getReleaseDate() == null) {
            throw new ValidationException("Укажите дату релиза");
        }
        if (request.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Укажите корректную дату");
        }
        if (request.getDuration() == null || request.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }

    private void validateFilmEntity(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Укажите имя фильма");
        }
        if (film.getDescription() == null || film.getDescription().isBlank()) {
            throw new ValidationException("Укажите описание фильма");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание не может быть больше 200 символов");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Укажите корректную дату");
        }
        if (film.getDuration() == null || film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}