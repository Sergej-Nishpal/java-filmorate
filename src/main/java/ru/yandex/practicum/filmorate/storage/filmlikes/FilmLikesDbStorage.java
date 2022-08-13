package ru.yandex.practicum.filmorate.storage.filmlikes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.LikesNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.OptionalInt;

@Slf4j
@Component
public class FilmLikesDbStorage implements FilmLikesStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmLikesDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(long filmId, long userId) {
        final String sqlQuery = "INSERT INTO FILM_LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        log.debug("Добавляем фильму с id = {} лайк пользователя с id = {}.", filmId, userId);
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        final String sqlQuery = "DELETE FROM FILM_LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        log.debug("Удаляем у фильма с id = {} лайк пользователя с id = {}.", filmId, userId);
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public int getLikesCount(long filmId) {
        final String sqlQuery = "SELECT COUNT(USER_ID) AS COUNT FROM FILM_LIKES WHERE FILM_ID = ?";
        log.debug("Получаем количество лайков фильма с id = {}.", filmId);
        try {
            return Objects.requireNonNull(jdbcTemplate.query(sqlQuery, this::getCountFromResultSet));
        } catch (NullPointerException e) {
            log.error("У фильма с id {} лайки не найдены.", filmId);
            throw new LikesNotFoundException("У фильма с id = " + filmId + " лайки не найдены.");
        }
    }

    private int getCountFromResultSet(ResultSet rs) throws SQLException {
        return rs.getInt("COUNT");
    }
}
