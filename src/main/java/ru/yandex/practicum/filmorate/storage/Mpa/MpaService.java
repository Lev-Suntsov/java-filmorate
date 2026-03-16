package ru.yandex.practicum.filmorate.storage.Mpa;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.MpaDto;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MpaService {
    public List<MpaDto> getAll() {
        return Arrays.stream(Mpa.values()).map(mpa -> new MpaDto(mpa.getId(), mpa.getName())).collect(Collectors.toList());
    }

    public MpaDto getById(int id) {
        return Arrays.stream(Mpa.values()).filter(mpa -> mpa.getId() == id).findFirst()
                .map(mpa -> new MpaDto(mpa.getId(), mpa.getName())).orElseThrow(() -> new NotFoundException("Рейтинг с id ="
                        + id + " не найден"));

    }
}
