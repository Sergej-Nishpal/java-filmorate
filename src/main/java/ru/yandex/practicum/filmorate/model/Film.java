package ru.yandex.practicum.filmorate.model;

import lombok.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.time.LocalDate;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Film {
    private static final int MAX_DESCRIPTION_LENGTH = 200;

    private Long id;

    @NotNull
    @NotBlank
    private String name;

    @Size(max = MAX_DESCRIPTION_LENGTH,
            message = "Максимальная длина описания ограничена: " + MAX_DESCRIPTION_LENGTH + " символов!")
    private String description;

    @NotNull
    private LocalDate releaseDate;

    @NotNull
    @Positive(message = "Длительность фильма должна быть больше нуля!")
    private int duration;

    private final Set<Long> likes = new HashSet<>();

    private Set<Genre> genres = new HashSet<>();

    @NotNull
    private Mpa mpa;

    public Map<String, Object> toMap() {
        Map<String, Object> filmMap = new HashMap<>();
        filmMap.put("FILM_NAME", name);
        filmMap.put("DESCRIPTION", description);
        filmMap.put("RELEASE_DATE", releaseDate);
        filmMap.put("DURATION", duration);
        filmMap.put("MPA_ID", mpa.getId());
        return filmMap;
    }
}
