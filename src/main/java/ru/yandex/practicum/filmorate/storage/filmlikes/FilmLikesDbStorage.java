package ru.yandex.practicum.filmorate.storage.filmlikes;

import java.sql.ResultSet;
import java.util.Collection;
import java.sql.SQLException;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.LikesNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.storage.filmgenres.FilmGenresStorage;

@Slf4j
@Component
public class FilmLikesDbStorage implements FilmLikesStorage {
    private final FilmGenresStorage filmGenresStorage;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmLikesDbStorage(FilmGenresStorage filmGenresStorage, JdbcTemplate jdbcTemplate) {
        this.filmGenresStorage = filmGenresStorage;
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

    public int getLikesCount(long filmId) {
        final String sqlQuery = "SELECT COUNT(USER_ID) AS COUNT FROM FILM_LIKES WHERE FILM_ID = ?";
        log.debug("Получаем количество лайков фильма с id = {}.", filmId);
        try {
            return Objects.requireNonNull(jdbcTemplate.query(sqlQuery, this::getCountFromResultSet, filmId));
        } catch (NullPointerException e) {
            log.error("У фильма с id {} лайки не найдены.", filmId);
            throw new LikesNotFoundException("У фильма с id = " + filmId + " лайки не найдены.");
        }
    }

    private int getCountFromResultSet(ResultSet rs) throws SQLException {
        return rs.next() ? rs.getInt("COUNT") : 0;
    }

    public Collection<Film> getPopularFilms(int count) {
        final String sqlQuery = "SELECT F.FILM_ID, F.FILM_NAME, F.DESCRIPTION, F.RELEASE_DATE, " +
                "F.DURATION, F.MPA_ID, M.MPA_NAME FROM FILMS AS F " +
                "LEFT OUTER JOIN MPAS M on M.MPA_ID = F.MPA_ID " +
                "LEFT OUTER JOIN FILM_LIKES AS FL ON FL.FILM_ID = F.FILM_ID " +
                "GROUP BY F.FILM_ID " +
                "ORDER BY COUNT(FL.FILM_ID) DESC LIMIT ?";
        log.debug("Запрашиваем наиболее популярные фильмы из БД в количестве {}.", count);
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getLong("FILM_ID"))
                .name(rs.getString("FILM_NAME"))
                .description(rs.getString("DESCRIPTION"))
                .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                .duration(rs.getInt("DURATION"))
                .mpa(new Mpa(rs.getLong("MPA_ID"), rs.getString("MPA_NAME")))
                .genres(filmGenresStorage.getFilmGenres(rs.getLong("FILM_ID")))
                .build();
    }
}
