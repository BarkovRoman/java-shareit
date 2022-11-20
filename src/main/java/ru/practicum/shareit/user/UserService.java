package ru.practicum.shareit.user;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<UserDto> getAll();

    UserDto getBuId(long id);

    UserDto add(UserDto userDto);

    UserDto update(UserDto userDto);

    void delete(long id);
}
