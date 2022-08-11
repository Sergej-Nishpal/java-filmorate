package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@Component
public class FilmDbStorage implements FilmStorage {
    private static final LocalDate CINEMA_BORN_DATE = LocalDate.of(1895, 12, 28);
    private static final String INCORRECT_PARAMETER = "Некорректный параметр %s = %d.";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Film> getAllFilms() {
        final String sqlQuery = "SELECT * FROM FILMS LEFT JOIN MPAS M on M.MPA_ID = FILMS.MPA_ID";
        log.debug("Запрашиваем все фильмы из БД.");
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film createFilm(Film film) {
        validate(film);
        SimpleJdbcInsert insertFilm = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("FILM_ID");
        final long filmId = insertFilm.executeAndReturnKey(film.toMap()).longValue();
        film.setId(filmId);
        log.debug("Добавлен фильм \"{}\" (новый id: {})", film.getName(), filmId);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        try {
            getFilmById(film.getId());
        } catch (EmptyResultDataAccessException e) {
            log.error("Фильм с id {} не найден.", film.getId());
            throw new UserNotFoundException("Фильм с id = " + film.getId()
                    + " не найден и не может быть обновлён!");
        }

        validate(film);
        final String sqlQuery =
                "UPDATE films SET " +
                        "FILM_NAME = ?, " +
                        "DESCRIPTION = ?, " +
                        "RELEASE_DATE = ?, " +
                        "DURATION = ?, " +
                        "MPA_ID = ? WHERE FILM_ID = ?";
        final int recordsUpdated =
                jdbcTemplate.update(sqlQuery,
                        film.getName(),
                        film.getDescription(),
                        film.getReleaseDate(),
                        film.getDuration(),
                        film.getMpa().getId(),
                        film.getId());
        log.debug("Фильмов обновлено: {}", recordsUpdated);
        return film;
    }

    @Override
    public Film getFilmById(long id) {
        if (id <= 0) {
            log.error("Передан некорректный id = {}.", id);
            throw new FilmNotFoundException(String.format(INCORRECT_PARAMETER, "id", id));
        }

        final String sqlQuery = "SELECT * FROM FILMS LEFT JOIN MPAS M on M.MPA_ID = FILMS.MPA_ID WHERE FILM_ID = ?";
        log.debug("Запрашиваем фильм с id {}.", id);
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(CINEMA_BORN_DATE)) {
            log.error("Фильм \"{}\" не прошёл валидацию!", film.getName());
            throw new ValidationException("Указана некорректная дата релиза фильма!");
        }
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("FILM_ID"))
                .name(resultSet.getString("FILM_NAME"))
                .description(resultSet.getString("DESCRIPTION"))
                .releaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate())
                .duration(resultSet.getInt("DURATION"))
                .mpa(new Mpa(resultSet.getLong("MPA_ID"), resultSet.getString("mpa_name")))
                .build();
    }
}
