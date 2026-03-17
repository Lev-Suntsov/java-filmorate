package ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;

import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.util.Collection;

@Repository
public class FilmDbStorage implements FilmStorage {
    private static final Logger logger = LoggerFactory.getLogger(FilmDbStorage.class);
    FilmRepository repository;

    public FilmDbStorage(FilmRepository repository) {
        this.repository = repository;
    }

    @Override
    public Collection<Film> getFilms() {
        logger.info("Получаем список всех фильмов");
        return repository.findAll();
    }

    @Override
    public FilmDto addFilm(NewFilmRequest request) throws ValidationException {
        if (request.getName() == null || request.getName().isBlank()) {
            logger.warn("Исключение, не указали название фильма");
            throw new ValidationException("Укажите имя фильма");
        }

        if (request.getDescription() == null || request.getDescription().isBlank()) {
            logger.warn("Исключение - не указанно описание фильма");
            throw new RuntimeException("Укажите описание фильма");
        }

        if (request.getDescription().length() > 200) {
            logger.warn("Ошибка валидации. Описание слишком длинное");
            throw new ValidationException("описание не может быть больше 200 символов");
        }

        if (request.getReleaseDate() == null) {
            logger.warn("Исключение. Не указанна дата релиза");
            throw new RuntimeException("Укажите дату релиза");
        }

        if (request.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            logger.warn("Исключение. Некорректная дата");
            throw new ValidationException("Укажите корректную дату");
        }

        if (request.getDuration() == null) {
            logger.warn("Не указана продолжительность фильма");
            throw new RuntimeException("Укажите продолжительность фильма");
        }

        if (request.getDuration() < 0) {
            logger.warn("Ошибка валидации. Отрицательная продолжительность фильма");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }

        Film film = FilmMapper.mapToFilm(request);
        try {
            film = repository.save(film);
        } catch (Exception e) {
            logger.error("Ошибка при сохранении фильма", e);
            throw e;
        }
        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public FilmDto updateFilm(long id,UpdateFilmRequest request) throws ValidationException {
        logger.info("Переходим в метод обновления фильма");
        Film film = repository.findById(id).map(film1 -> {
            try {
                return FilmMapper.updateFilm(film1, request);
            } catch (ValidationException e) {
                throw new RuntimeException(e);
            }
        }).orElseThrow(() -> new NotFoundException(
                "фильм не найден"
        ));
        film = repository.update(film);
        return FilmMapper.mapToFilmDto(film);
    }

    @Override
    public FilmDto getFilmById(long id) {
        return repository.findById(id).map(FilmMapper::mapToFilmDto).orElseThrow(() -> new NotFoundException("фильм с данным id не найден"));
    }
}
