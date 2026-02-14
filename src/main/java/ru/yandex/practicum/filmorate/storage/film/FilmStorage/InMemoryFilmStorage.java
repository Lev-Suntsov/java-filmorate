package ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

public class InMemoryFilmStorage implements FilmStorage {
    private static final Logger logger = LoggerFactory.getLogger(InMemoryFilmStorage.class);
    private final HashMap<Integer, Film> filmHashMap = new HashMap<>();

    @Override
    public Collection<Film> getFilms() {
        logger.info("Получаем список всех фильмов");
        return filmHashMap.values();
    }

    @Override
    public Film addFilm(Film film) throws ValidationException {
        logger.info("Добавляем новый фильм");

        if (film.getName() == null || film.getName().isBlank()) {
            logger.warn("Исключение, не указали название фильма");
            throw new RuntimeException("Укажите имя фильма");
        }

        if (film.getDescription() == null || film.getDescription().isBlank()) {
            logger.warn("Исключение - не указанно описание фильма");
            throw new RuntimeException("Укажите описание фильма");
        }

        if (film.getDescription().length() > 200) {
            logger.warn("Ошибка валидации. Описание слишком длинное");
            throw new ValidationException("описание не может быть больше 200 символов");
        }

        if (film.getReleaseDate() == null) {
            logger.warn("Исключение. Не указанна дата релиза");
            throw new RuntimeException("Укажите дату релиза");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            logger.warn("Исключение. Некорректная дата");
            throw new ValidationException("Укажите корректную дату");
        }

        if (film.getDuration() == null) {
            logger.warn("Не указана продолжительность фильма");
            throw new RuntimeException("Укажите продолжительность фильма");
        }

        if (film.getDuration() == null || film.getDuration().isNegative() || film.getDuration().isZero()) {
            logger.warn("Ошибка валидации. Отрицательная продолжительность фильма");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }

        film.setId(getNextId());
        filmHashMap.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException {
        logger.info("Переходим в метод обновления фильма");
        if (!filmHashMap.containsKey(film.getId())) {
            logger.error("Ошибка, фильм не найден");
            throw new NotFoundException("фильм не найден");
        }

        logger.info("Получаем фильм из таблицы");
        Film oldFilm = filmHashMap.get(film.getId());
        // Валидация + обновление полей
        if (film.getName() == null || film.getName().isBlank()) {
            logger.warn("Исключение - не указали название фильма");
            throw new RuntimeException("Укажите имя фильма");
        }

        if (film.getDescription() == null || film.getDescription().isBlank()) {
            logger.warn("Исключение возникшее из за отсутствия описания фильма");
            throw new RuntimeException("Укажите описание фильма");
        }

        if (film.getDescription().length() > 200) {
            logger.warn("Исключение, слишком большое описание");
            throw new ValidationException("описание не может быть больше 200 символов");
        }

        if (film.getReleaseDate() == null) {
            logger.warn("Исключение, не указана дата релиза");
            throw new RuntimeException("Укажите дату релиза");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            logger.warn("Исключение, введена некорректная дата");
            throw new RuntimeException("Укажите корректную дату");
        }

        if (film.getDuration() == null) {
            logger.warn("Исключение - не указанна продолжительность фильма");
            throw new RuntimeException("Укажите продолжительность фильма");
        }

        if (film.getDuration() == null || film.getDuration().isNegative() || film.getDuration().isZero()) {
            logger.warn("Исключение - некорректная продолжительность фильма");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }

        logger.info("Добавляем обновленные данные в таблицу");
        oldFilm.setName(film.getName());
        oldFilm.setDescription(film.getDescription());
        oldFilm.setReleaseDate(film.getReleaseDate());
        oldFilm.setDuration(film.getDuration());
        return oldFilm;
    }

    public Film getFilmById(int id) {
        logger.info("Получаем информацию о фильме по id");
        return filmHashMap.values().stream()
                .filter(film -> film.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Фильм с id: " + id + " не найден"));
    }

    private int getNextId() {
        int currentMaxId = filmHashMap.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
