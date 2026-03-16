package ru.yandex.practicum.filmorate.storage.user.UserService;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.storage.user.UserStorage.UserDbStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class UserService {

    UserDbStorage userStorage;
    HashMap<UserDto, List<UserDto>> friends = new HashMap<>();

    public UserService(UserDbStorage userStorage) {
        this.userStorage = userStorage;
    }

    public ArrayList<UserDto> addFriend(int userId, int friendId) {
        UserDto user = userStorage.getUserById(userId);
        UserDto friend = userStorage.getUserById(friendId);

        if (!friends.containsKey(user)) {
            friends.put(user, new ArrayList<>());
        }

        friends.get(user).add(friend);

        if (!friends.containsKey(friend)) {
            friends.put(friend, new ArrayList<>());
        }

        friends.get(friend).add(user);
        return new ArrayList<>(friends.get(user));
    }

    public List<UserDto> deleteUserFromFriendsList(int userId, int friendId) {
        UserDto user = userStorage.getUserById(userId);
        UserDto friend = userStorage.getUserById(friendId);

        if (!friends.containsKey(user)) {
            throw  new NotFoundException("у пользователя " + userId + "нет друзей");
        }

        if (friends.get(user).isEmpty() && !friends.get(user).contains(friend)) {
            throw  new NotFoundException("пользователь с айди " + friendId + "не добавлен в друзья к " + userId);
        }

        friends.get(user).remove(friend);
        return new ArrayList<>(friends.get(user));
    }

    public List<UserDto> getTogetherFriends(int id, int otherId) {
        List<UserDto> userFriends = friends.get(userStorage.getUserById(id));
        List<UserDto> otherFriends = friends.get(userStorage.getUserById(otherId));

        return userFriends.stream()
                .filter(otherFriends::contains).toList();
    }

}
