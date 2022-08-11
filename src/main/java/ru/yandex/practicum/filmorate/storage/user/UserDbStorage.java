package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Slf4j
@Component
public class UserDbStorage implements UserStorage {
    private static final String SPACE_CHARACTER = " ";
    private static final String INCORRECT_PARAMETER = "Некорректный параметр %s = %d";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<User> findAllUsers() {
        final String sqlQuery = "SELECT * FROM USERS";
        log.debug("Запрашиваем всех пользователей из БД.");
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User createUser(User user) {
        validate(user);

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        SimpleJdbcInsert insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("USER_ID");
        final long userId = insertUser.executeAndReturnKey(user.toMap()).longValue();
        user.setId(userId);
        log.debug("Добавлен пользователь с логином \"{}\" (новый id: {})", user.getLogin(), userId);
        return user;
    }

    @Override
    public User updateUser(User user) {
        try {
            getUserById(user.getId());
        } catch (EmptyResultDataAccessException e) {
            log.error("Пользователь с id {} не найден.", user.getId());
            throw new UserNotFoundException("Пользователь с id = " + user.getId()
                    + " не найден и не может быть обновлён!");
        }

        validate(user);
        final String sqlQuery = "UPDATE USERS SET EMAIL = ?, LOGIN = ?, USER_NAME = ?, BIRTHDAY = ? WHERE USER_ID = ?";
        final int recordsUpdated =
                jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        log.debug("Пользователей обновлено: {}", recordsUpdated);
        return user;
    }

    @Override
    public User getUserById(long id) {
        if (id <= 0) {
            log.error("Передан некорректный id = {}.", id);
            throw new UserNotFoundException(String.format(INCORRECT_PARAMETER, "id", id));
        }

        final String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = ?";
        log.debug("Запрашиваем пользователя с id {}.", id);
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
    }

    private void validate(User user) {
        if (user.getLogin().contains(SPACE_CHARACTER)) {
            log.error("Пользователь с логином \"{}\" не прошёл валидацию.", user.getLogin());
            throw new ValidationException("Логин не должен содержать пробелы!");
        }
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("USER_ID"))
                .email(resultSet.getString("EMAIL"))
                .login(resultSet.getString("LOGIN"))
                .name(resultSet.getString("USER_NAME"))
                .birthday(resultSet.getDate("BIRTHDAY").toLocalDate())
                .build();
    }
}
