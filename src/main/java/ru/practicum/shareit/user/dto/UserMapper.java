package ru.practicum.shareit.user.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.user.model.User;

/*@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUser(UserDto userDto, long id) {
        return new User(
                id,
                userDto.getName(),
                userDto.getEmail()
        );
    }
}*/
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);
    @Mapping(target = "id", source = "id")
    User toUser(UserDto userDto, Long id);
}
