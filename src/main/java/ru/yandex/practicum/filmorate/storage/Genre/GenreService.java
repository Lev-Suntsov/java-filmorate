package ru.yandex.practicum.filmorate.storage.Genre;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class GenreService {
    private final GenreRepository genreRepository;

    public List<GenreDto> findAll() {
        return genreRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public GenreDto getGenreById(int id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Жанр не найден"));
        return toDto(genre);
    }

    public Set<Genre> getGenresByIds(List<Integer> ids) {
        return genreRepository.findByIds(ids);
    }

    private GenreDto toDto(Genre genre) {
        GenreDto dto = new GenreDto();
        dto.setId(genre.getId());
        dto.setName(genre.getName());
        return dto;
    }
}