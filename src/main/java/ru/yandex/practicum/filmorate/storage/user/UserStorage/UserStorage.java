package ru.yandex.practicum.filmorate.storage.user.UserStorage;

import ru.yandex.practicum.filmorate.model.User;

import javax.xml.bind.ValidationException;
import java.util.Collection;

public interface UserStorage {
    public User addUser(User user) throws ValidationException;

    public Collection<User> getUsers();

    public User updateUser(User user) throws ValidationException;

    public User getUserById(int id);
}
