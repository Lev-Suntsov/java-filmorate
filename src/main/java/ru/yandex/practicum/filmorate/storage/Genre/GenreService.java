package ru.yandex.practicum.filmorate.storage.Genre;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreService {
    public List<GenreDto> getGenres() {
       return Arrays.stream(Genre.values()).map(genre -> new GenreDto(genre.getId(), genre.getName())).collect(Collectors.toList());
    }

    public GenreDto getGenreById(int id) {
        return Arrays.stream(Genre.values()).filter(genre -> genre.getId() == id).findFirst().map(genre ->
                new GenreDto(genre.getId(), genre.getName())).orElseThrow(() -> new NotFoundException("Жанр с id ="
                + id + " не найден"));
    }
}
