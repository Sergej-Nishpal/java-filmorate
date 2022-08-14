package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MpaDbStorageTest {
    private final MpaDbStorage mpaStorage;

    @Test
    @Order(1)
    void testGetAllMpas() {
        final Collection<Mpa> mpas = mpaStorage.getAllMpas();
        final int expectedMpaSize = 5; // 5 рейтингов созданы в data.sql
        assertThat(mpas)
                .isNotEmpty()
                .hasSize(expectedMpaSize);
    }

    @Test
    @Order(2)
    void testGetMpaById() {
        final Optional<Mpa> optionalMpa = Optional.ofNullable(mpaStorage.getMpaById(4));
        final long expectedMpaId = 4L;
        final String expectedMpaName = "R";
        assertThat(optionalMpa)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", expectedMpaId)
                ).hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", expectedMpaName)
                );
    }
}