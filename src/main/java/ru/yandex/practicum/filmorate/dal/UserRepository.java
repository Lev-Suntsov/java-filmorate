package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository extends BasRepository {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String INSERT_QUERY = "INSERT INTO users(email, login, name, birthday, friendsStatus)" +
            "VALUES (?, ?, ?, ?, ?) returning id";
    private static final String FIND_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ?, friendsStatus = ?" +
            " WHERE id = ?";

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public Optional<User> getUserById(Long id) {
        return findOne(FIND_BY_ID, id);
    }

    public User saveUser(User user) {
        long id = insert(INSERT_QUERY, user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getFriendsStatus());
        user.setId(id);
        return  user;
    }

    public User updateUser(User user) {
        update(UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getFriendsStatus(),
                user.getId());
        return user;
    }

    public List<User> findAllUsers() {
        return findMany(FIND_ALL_QUERY);
    }
}
