package ru.yandex.practicum.filmorate.model;

import lombok.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {
    private Long id;

    @NotNull
    @NotEmpty
    @Email(message = "Указан некорректный email!")
    private String email;

    @NotNull
    @NotEmpty
    private String login;

    private String name;

    @PastOrPresent(message = "Дата рождения не должна быть в будущем!")
    private LocalDate birthday;

    private final Map<Long, Boolean> friendshipStatus = new HashMap<>();

    public Map<String, Object> toMap() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", email);
        userMap.put("login", login);
        userMap.put("user_name", name);
        userMap.put("birthday", birthday);
        return userMap;
    }
}
