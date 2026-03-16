package ru.yandex.practicum.filmorate.storage.user.UserService;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.storage.user.UserStorage.UserDbStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final UserDbStorage userStorage;
    private final Map<UserDto, List<UserDto>> friends = new HashMap<>();

    public UserService(UserDbStorage userStorage) {
        this.userStorage = userStorage;
    }

    // Добавить в друзья (двусторонняя дружба)
    public List<UserDto> addFriend(int userId, int friendId) {
        UserDto user = userStorage.getUserById(userId);
        UserDto friend = userStorage.getUserById(friendId);

        friends.computeIfAbsent(user, u -> new ArrayList<>());
        friends.computeIfAbsent(friend, u -> new ArrayList<>());

        if (!friends.get(user).contains(friend)) {
            friends.get(user).add(friend);
        }
        if (!friends.get(friend).contains(user)) {
            friends.get(friend).add(user);
        }

        return new ArrayList<>(friends.get(user));
    }

    // Удалить из друзей (двустороннее удаление)
    public List<UserDto> deleteUserFromFriendsList(int userId, int friendId) {
        UserDto user = userStorage.getUserById(userId);
        UserDto friend = userStorage.getUserById(friendId);

        if (!friends.containsKey(user) || !friends.get(user).contains(friend)) {
            throw new NotFoundException(
                    "пользователь с айди " + friendId + " не добавлен в друзья к " + userId
            );
        }

        friends.get(user).remove(friend);
        if (friends.containsKey(friend)) {
            friends.get(friend).remove(user);
        }

        return new ArrayList<>(friends.get(user));
    }

    // Список друзей пользователя
    public List<UserDto> getFriends(int userId) {
        UserDto user = userStorage.getUserById(userId);
        return new ArrayList<>(friends.getOrDefault(user, List.of()));
    }

    // Общие друзья двух пользователей
    public List<UserDto> getTogetherFriends(int id, int otherId) {
        UserDto user = userStorage.getUserById(id);
        UserDto other = userStorage.getUserById(otherId);

        List<UserDto> userFriends = friends.getOrDefault(user, List.of());
        List<UserDto> otherFriends = friends.getOrDefault(other, List.of());

        return userFriends.stream()
                .filter(otherFriends::contains)
                .toList();
    }
}

