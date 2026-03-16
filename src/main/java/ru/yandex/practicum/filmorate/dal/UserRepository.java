package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository

public class UserRepository extends BasRepository<User> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String INSERT_QUERY =
            "INSERT INTO users(id, email, login, name, birthday, friendsStatus, friendsList, LickedFilms) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String UPDATE_QUERY =
            "UPDATE users SET email = ?, login = ?, name = ?, birthday = ?, friendsStatus = ? " +
                    "WHERE id = ?";

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public Optional<User> getUserById(Long id) {
        return findOne(FIND_BY_ID, id);
    }

    public User saveUser(User user) {
        user.setId(1L);

        update(
                INSERT_QUERY,
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getFriendsStatus(),
                0,          // friendsList
                0           // LickedFilms
        );
        return user;
    }

    public User updateUser(User user) {
        update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getFriendsStatus(),
                user.getId()
        );
        return user;
    }

    public List<User> findAllUsers() {
        return findMany(FIND_ALL_QUERY);
    }
}
