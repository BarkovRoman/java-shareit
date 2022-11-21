package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public List<UserDto> getAll() {
        return userRepository.getAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getBuId(long id) {
        return UserMapper.toUserDto(userRepository.getBuId(id)
                .orElseThrow(() -> new NotFoundException(String.format("User id=%s не найден", id)))
        );
    }

    @Override
    public UserDto add(UserDto userDto) {
        User user = UserMapper.toUser(userDto, 0);
        userRepository.add(user);
        long id = user.getId();
        user = userRepository.getBuId(user.getId())
                .orElseThrow(() -> new NotFoundException(String.format("User id=%s не coздан", id)));
        log.debug("Добавлен user {}", user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto update(UserDto userDto, long id) {
        User user = UserMapper.toUser(userDto, id);
        if (!userRepository.update(user, id)) {
            throw new NotFoundException(String.format("User id = %s не найден", id));
        }
        user = userRepository.getBuId(id)
                .orElseThrow(() -> new NotFoundException(String.format("User id=%s не обновлен", id)));
        log.debug("Обновлен user {}", user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public void delete(long id) {
        if (userRepository.delete(id)) {
            log.debug("User id = {} удален", id);
        } else {
            throw new NotFoundException((String.format("User id = %s не найден", id)));
        }
    }
}
