package ru.yandex.practicum.filmorate.storage.Mpa;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;


@Service
public class MpaService {
    private final MpaRepository repository;

    public MpaService(MpaRepository repository, MpaRowMapper rowMapper) {
        this.repository = repository;
    }

    public List<Mpa> getAll() {

        return repository.findAll();
    }

    public Optional<Mpa> getById(int id) {
        return repository.findById(id);

    }
}
