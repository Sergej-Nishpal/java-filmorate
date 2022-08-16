package ru.yandex.practicum.filmorate.storage.friendship;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;

@Slf4j
@Component
public class FriendshipDbStorage implements FriendshipStorage {

    private final UserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendshipDbStorage(@Qualifier("userDbStorage") UserStorage userStorage, JdbcTemplate jdbcTemplate) {
        this.userStorage = userStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createFriendship(long userId, long friendId) {
        checkIfUserExists(userId);
        checkIfUserExists(friendId);
        final String sqlQuery = "INSERT INTO FRIENDSHIPS (USER_ID, FRIEND_ID, STATUS_ID) VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId, 1);
    }

    @Override
    public void confirmFriendship(long userId, long friendId) {
        final String sqlQuery = "INSERT INTO FRIENDSHIPS (USER_ID, FRIEND_ID, STATUS_ID) VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId, 2);
    }

    @Override
    public void deleteFriendship(long userId, long friendId) {
        final String sqlQuery = "DELETE FROM FRIENDSHIPS WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public Collection<User> getFriends(long userId) {
        checkIfUserExists(userId);
        final String sqlQuery = "SELECT * FROM USERS AS U " +
                "LEFT OUTER JOIN FRIENDSHIPS AS F ON U.USER_ID = F.FRIEND_ID " +
                "WHERE F.USER_ID = ?";
        log.debug("Запрашиваем друзей пользователя из БД.");
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId);
    }

    @Override
    public Collection<User> getCommonFriends(long userId, long otherId) {
        checkIfUserExists(userId);
        checkIfUserExists(otherId);
        final String sqlQuery = "SELECT * FROM USERS WHERE USER_ID  = " +
                "(SELECT FRIEND_ID FROM FRIENDSHIPS WHERE USER_ID = ? " +
                "INTERSECT " +
                "SELECT FRIEND_ID FROM FRIENDSHIPS WHERE USER_ID = ?)";

        log.debug("Запрашиваем общих друзей двух пользователей из БД.");
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId, otherId);
    }

    public User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {

        return User.builder()
                .id(resultSet.getLong("USER_ID"))
                .email(resultSet.getString("EMAIL"))
                .login(resultSet.getString("LOGIN"))
                .name(resultSet.getString("USER_NAME"))
                .birthday(resultSet.getDate("BIRTHDAY").toLocalDate())
                .build();
    }

    private void checkIfUserExists(long userId) {
        try {
            userStorage.getUserById(userId);
        } catch (EmptyResultDataAccessException e) {
            log.error("Пользователь с id {} не найден.", userId);
            throw new UserNotFoundException("Пользователь с id = " + userId + " не найден.");
        }
    }
}