package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDbStorageTest {
    private static final String USER_EMAIL = "sergej.nishpal@yandex.ru";
    private static final String USER_LOGIN = "Sergej_N";
    private static final String USER_NAME = "Sergej Nishpal";
    private static final String NEW_USER_NAME = "Sergej Vladimirovich Nishpal";
    private static final LocalDate USER_BIRTHDAY = LocalDate.of(1975, 2, 14);
    private final UserDbStorage userStorage;

    @Test
    @Order(1)
    void testCreateUser() { // добавляем четвёртого пользователя
        final User newUser = User.builder()
                .email(USER_EMAIL)
                .login(USER_LOGIN)
                .name(USER_NAME)
                .birthday(USER_BIRTHDAY)
                .build();

        final Optional<User> optionalUser = Optional.ofNullable(userStorage.createUser(newUser));
        assertThat(optionalUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", USER_NAME)
                );
    }

    @Test
    @Order(2)
    void testFindAllUsers() {
        final Collection<User> users = userStorage.findAllUsers();
        assertThat(users).isNotEmpty();
    }

    @Test
    @Order(3)
    void testGetUserById() {
        final Optional<User> optionalUser = Optional.ofNullable(userStorage.getUserById(1));
        final long expectedUserId = 1L;
        assertThat(optionalUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", expectedUserId)
                );
    }

    @Test
    @Order(4)
    void testUpdateUser() {
        User userForUpdate = userStorage.getUserById(4); // добавленный четвёртый пользователь
        assertThat(userForUpdate)
                .hasFieldOrPropertyWithValue("name", USER_NAME);

        userForUpdate.setName(NEW_USER_NAME);
        final User updatedUser = userStorage.updateUser(userForUpdate);
        assertThat(updatedUser)
                .hasFieldOrPropertyWithValue("name", NEW_USER_NAME);
    }
}