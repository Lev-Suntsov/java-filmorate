package ru.yandex.practicum.filmorate.storage.user.UserStorage;

import ru.yandex.practicum.filmorate.dto.NewUserReqest;
import ru.yandex.practicum.filmorate.dto.UpdateUserReqest;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

import javax.xml.bind.ValidationException;
import java.util.Collection;

public interface UserStorage {
    public UserDto addUser(NewUserReqest request) throws ValidationException;

    public Collection<User> getUsers();

    public UserDto updateUser(long id, UpdateUserReqest request) throws ValidationException;

    public UserDto getUserById(long id);
}
