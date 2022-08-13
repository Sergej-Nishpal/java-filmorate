package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.filmlikes.FilmLikesStorage;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage, FilmLikesStorage {
    private static final LocalDate CINEMA_BORN_DATE = LocalDate.of(1895, 12, 28);
    private static final String INCORRECT_PARAMETER = "Некорректный параметр %s = %d.";

    private final Map<Long, Film> films = new HashMap<>();
    private final FilmIdGenerator filmIdGenerator = new FilmIdGenerator();

    @Override
    public Collection<Film> getAllFilms() {
        log.debug("Текущее количество сохранённых фильмов: {}", films.size());
        return films.values();
    }

    @Override
    public Film createFilm(Film film) {
        validate(film);
        film.setId(filmIdGenerator.generateId());
        films.put(film.getId(), film);
        log.debug("Фильм {} добавлен.", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("Фильм с id {} не найден.", film.getId());
            throw new FilmNotFoundException("Фильм с id = " + film.getId()
                    + " не найден и не может быть обновлён!");
        }

        validate(film);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getFilmById(long id) {
        if (id <= 0) {
            throw new FilmNotFoundException(String.format(INCORRECT_PARAMETER, "id", id));
        }
        return films.get(id);
    }

    @Override
    public void addLike(long filmId, long userId) {
          getFilmById(filmId).getLikes().add(userId);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        getFilmById(filmId).getLikes().remove(userId);
    }

    @Override
    public int getLikesCount(long filmId) {
        return 0; // TODO Что тут вернуть?
    }

    public Collection<Film> getPopularFilms(int count) {
        return getAllFilms().stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(CINEMA_BORN_DATE)) {
            log.error("Фильм \"{}\" не прошёл валидацию!", film.getName());
            throw new ValidationException("Указана некорректная дата релиза фильма!");
        }
    }
}
