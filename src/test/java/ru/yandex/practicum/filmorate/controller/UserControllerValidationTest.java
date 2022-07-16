package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
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

class UserControllerValidationTest {
    private final static LocalDate CORRECT_BIRTHDAY = LocalDate.of(1975, 2, 14);
    private final static LocalDate INCORRECT_BIRTHDAY = LocalDate.now().plusDays(1);
    private final static String USER_NAME = "Sergej Nishpal";
    private final static String EMPTY_USER_NAME = "";
    private final static String CORRECT_LOGIN = "Sergej_N";
    private final static String INCORRECT_LOGIN = "Sergej N";
    private final static String EMPTY_LOGIN = "";
    private final static String CORRECT_EMAIL = "sergej.nishpal@yandex.ru";
    private final static String EMPTY_EMAIL = "";
    private final static String INCORRECT_EMAIL = "sergej.nishpal_yandex.ru";

    private final UserStorage userStorage = new InMemoryUserStorage();
    private Validator validator;

    @BeforeEach
    public void beforeEach() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @ParameterizedTest
    @MethodSource("userFieldsValuesAndExpectedViolationSize")
    void testWithMultiArgMethodSource(final String email, final String login, final String name,
                                      final LocalDate birthday, final int expectedViolationsSize) {
        User user = User.builder()
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        final int actualViolationsSize = violations.size();
        assertEquals(expectedViolationsSize, actualViolationsSize,
                "Количество обнаруженных нарушений не соответствует ожидаемому!");

    }

    static Stream<Arguments> userFieldsValuesAndExpectedViolationSize() {
        return Stream.of(
                arguments(EMPTY_EMAIL,     CORRECT_LOGIN, USER_NAME, CORRECT_BIRTHDAY,   1),
                arguments(INCORRECT_EMAIL, CORRECT_LOGIN, USER_NAME, CORRECT_BIRTHDAY,   1),
                arguments(CORRECT_EMAIL,   EMPTY_LOGIN,   USER_NAME, CORRECT_BIRTHDAY,   1),
                arguments(CORRECT_EMAIL,   CORRECT_LOGIN, USER_NAME, INCORRECT_BIRTHDAY, 1)
        );
    }

    @Test
    void shouldCorrectValidateLoginContainingSpaces() {
        User user = User.builder()
                .email(CORRECT_EMAIL)
                .login(INCORRECT_LOGIN)
                .name(USER_NAME)
                .birthday(CORRECT_BIRTHDAY)
                .build();

        ValidationException exception = assertThrows(ValidationException.class, () -> userStorage.createUser(user));
        final String expectedMessage = "Логин не должен содержать пробелы!";
        final String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage, "Сообщения об исключении не совпадают!");
    }

    @Test
    void shouldSetLoginAsNameIfNameIsEmpty() {
        User user = User.builder()
                .email(CORRECT_EMAIL)
                .login(CORRECT_LOGIN)
                .name(EMPTY_USER_NAME)
                .birthday(CORRECT_BIRTHDAY)
                .build();

        User createdUser = userStorage.createUser(user);
        final String expectedName = "Sergej_N";
        final String actualName = createdUser.getName();
        assertEquals(expectedName, actualName, "Актуальное имя пользователя не соответствует ожидаемому!");
    }
}
