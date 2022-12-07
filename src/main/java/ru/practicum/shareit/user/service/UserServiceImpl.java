package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import org.springframework.transaction.annotation.Transactional;
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
    public UserDto getById(Long id) {
        return mapper.toUserDto(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User id=%s не найден", id)))
        );
    }

    @Override
    @Transactional
    public UserDto add(UserDto userDto) {
        if (userRepository.findByEmailContainingIgnoreCase(userDto.getEmail()).size() != 0) {
            throw new RuntimeException(String.format("User с email = %s уже существует", userDto.getEmail()));
        }
        User user = userRepository.save(mapper.toUser(userDto, 0L));
        log.debug("Добавлен user {}", user);
        return mapper.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDto, Long id) {
        isExistsUserById(id);
        User user = mapper.toUser(userDto, id);
        User userUpdate = userRepository.update(user);
        log.debug("Обновлен user {}", userUpdate);
        return mapper.toUserDto(userUpdate);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        isExistsUserById(id);
        userRepository.deleteById(id);
        log.debug("User id = {} удален", id);
    }

    private void isExistsUserById(long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User id=%s не найден", id)));
    }
}
