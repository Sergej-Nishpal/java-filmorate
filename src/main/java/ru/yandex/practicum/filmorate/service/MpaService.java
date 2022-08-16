package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.Collection;

@Slf4j
@Service
public class MpaService {
    private static final String INCORRECT_PARAMETER = "Некорректный параметр %s = %d";
    private final MpaStorage mpaStorage;

    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public Collection<Mpa> getAllMpas() {
        return mpaStorage.getAllMpas();
    }

    public Mpa getMpaById(Long mpaId) {
        if (mpaId <= 0) {
            log.error("Передан некорректный id = {}.", mpaId);
            throw new MpaNotFoundException(String.format(INCORRECT_PARAMETER, "id", mpaId));
        }
        return mpaStorage.getMpaById(mpaId);
    }
}