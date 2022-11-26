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
    public UserDto getById(long id) {
        return UserMapper.toUserDto(userRepository.getById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User id=%s не найден", id)))
        );
    }

    @Override
    public UserDto add(UserDto userDto) {
        User user = UserMapper.toUser(userDto, 0);
        User userNew = userRepository.add(user);
        log.debug("Добавлен user {}", userNew);
        return UserMapper.toUserDto(userNew);
    }

    @Override
    public UserDto update(UserDto userDto, long id) {
        isExistsUserById(id);
        User user = UserMapper.toUser(userDto, id);
        userDto = UserMapper.toUserDto(userRepository.update(user, id));
        log.debug("Обновлен user {}", user);
        return userDto;
    }

    @Override
    public void delete(long id) {
        isExistsUserById(id);
        userRepository.delete(id);
        log.debug("User id = {} удален", id);
    }

    private void isExistsUserById(long id) {
        userRepository.getById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User id=%s не найден", id)));
    }
}
