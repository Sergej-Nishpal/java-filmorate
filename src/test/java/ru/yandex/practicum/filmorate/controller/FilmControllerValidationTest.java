package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class FilmControllerValidationTest {
    private final static String FILM_NAME = "Film name";
    private final static LocalDate CINEMA_BORN_DATE = LocalDate.of(1895, 12, 28);
    private final static LocalDate CORRECT_RELEASE_DATE = CINEMA_BORN_DATE.plusYears(100);
    private final static LocalDate INCORRECT_RELEASE_DATE = CINEMA_BORN_DATE.minusDays(1);
    private final static int CORRECT_FILM_DURATION = 105;
    private final static int INCORRECT_FILM_DURATION = 0;
    private final static Mpa MPA = new Mpa(1L, "PG-13");

    private final FilmStorage filmStorage = new InMemoryFilmStorage();
    private Validator validator;

    @BeforeEach
    public void beforeEach() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @ParameterizedTest
    @MethodSource("filmFieldsValuesAndExpectedViolationSize")
    void testWithMultiArgMethodSource(final String name, final String description, final LocalDate releaseDate,
                                      final int duration, final Mpa mpa, final int expectedViolationsSize) {
        Film film = Film.builder()
                .name(name)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .mpa(mpa)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        final int actualViolationsSize = violations.size();
        assertEquals(expectedViolationsSize, actualViolationsSize,
                "Количество обнаруженных нарушений не соответствует ожидаемому!");
    }

    @Test
    void shouldCorrectValidateFilmWithTooOldReleaseDate() {
        Film film = Film.builder()
                .name(FILM_NAME)
                .description(getTextWith199Chars())
                .releaseDate(INCORRECT_RELEASE_DATE)
                .duration(CORRECT_FILM_DURATION)
                .mpa(MPA)
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> filmStorage.createFilm(film));
        final String expectedMessage = "Указана некорректная дата релиза фильма!";
        final String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage, "Сообщения об исключении не совпадают!");
    }

    static Stream<Arguments> filmFieldsValuesAndExpectedViolationSize() {
        return Stream.of(
                arguments("       ", getTextWith199Chars(), CORRECT_RELEASE_DATE,   CORRECT_FILM_DURATION,   MPA, 1),
                arguments(FILM_NAME, getTextWith201Chars(), CORRECT_RELEASE_DATE,   CORRECT_FILM_DURATION,   MPA, 1),
                arguments(FILM_NAME, getTextWith199Chars(), CORRECT_RELEASE_DATE,   INCORRECT_FILM_DURATION, MPA, 1)
        );
    }

    static String getTextWith199Chars() {
        return "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
                "when an unknown printer took a galley of type ";
    }

    static String getTextWith201Chars() {
        return "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
                "when an unknown printer took a galley of type a ";
    }
}
