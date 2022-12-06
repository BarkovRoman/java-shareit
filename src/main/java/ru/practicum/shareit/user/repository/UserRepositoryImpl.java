package ru.practicum.shareit.user.repository;

import org.springframework.context.annotation.Lazy;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

public class UserRepositoryImpl implements UserRepositoryCustom {
    private final UserRepository userRepository;

    public UserRepositoryImpl(@Lazy UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User update(User user) {
        User userUpdate = userRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException(String.format("User id=%s не найден", user.getId())));
        if (user.getEmail() != null) {
            userUpdate.setEmail(user.getEmail());
        }
        if (user.getName() != null && !user.getName().isBlank()) {
            userUpdate.setName(user.getName());
        }
        userRepository.save(userUpdate);
        return userUpdate;
    }
}
