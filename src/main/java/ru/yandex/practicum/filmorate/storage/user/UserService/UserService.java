package ru.yandex.practicum.filmorate.storage.user.UserService;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class UserService {

    InMemoryUserStorage userStorage = new InMemoryUserStorage();
    HashMap<User, List<User>> friends = new HashMap<>();

    public ArrayList<User> addFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

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

    public List<User> deleteUserFromFriendsList(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        if (!friends.containsKey(user)) {
            throw  new NotFoundException("у пользователя " + userId + "нет друзей");
        }

        if (friends.get(user).isEmpty() && !friends.get(user).contains(friend)) {
            throw  new NotFoundException("пользователь с айди " + friendId + "не добавлен в друзья к " + userId);
        }

        friends.get(user).remove(friend);
        return new ArrayList<>(friends.get(user));
    }

    public List<User> getTogetherFriends(int id, int otherId) {
        List<User> userFriends = friends.get(userStorage.getUserById(id));
        List<User> otherFriends = friends.get(userStorage.getUserById(otherId));

        return userFriends.stream()
                .filter(otherFriends::contains).toList();
    }

}
