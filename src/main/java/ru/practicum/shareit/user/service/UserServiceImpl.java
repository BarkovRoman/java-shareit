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
    private final UserMapper mapper;

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(mapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getById(long id) {
        return mapper.toUserDto(userRepository.getById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User id=%s не найден", id)))
        );
    }

    @Override
    public UserDto add(UserDto userDto) {
        User user = userRepository.save(mapper.toUser(userDto, 0));
        log.debug("Добавлен user {}", user);
        return mapper.toUserDto(user);
    }

    @Override
    public UserDto update(UserDto userDto, long id) {
        isExistsUserById(id);
        User user = userRepository.update(mapper.toUser(userDto, id));
        log.debug("Обновлен user {}", user);
        return mapper.toUserDto(user);
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
