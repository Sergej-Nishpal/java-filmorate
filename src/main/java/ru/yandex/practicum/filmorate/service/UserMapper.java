package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {

    private UserMapper() {
    }

    public static User mapRowToUser(ResultSet resultSet) throws SQLException {

        return User.builder()
                .id(resultSet.getLong("USER_ID"))
                .email(resultSet.getString("EMAIL"))
                .login(resultSet.getString("LOGIN"))
                .name(resultSet.getString("USER_NAME"))
                .birthday(resultSet.getDate("BIRTHDAY").toLocalDate())
                .build();
    }
}
