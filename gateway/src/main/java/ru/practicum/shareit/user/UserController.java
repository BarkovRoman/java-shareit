package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.Create;
import ru.practicum.shareit.user.dto.Update;
import ru.practicum.shareit.user.dto.UserRequestDto;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("Get user All");
        return userClient.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        log.info("Get user {}", id);
        return userClient.getById(id);
    }

    @PostMapping
    public ResponseEntity<Object> add(@Validated(Create.class) @RequestBody UserRequestDto userRequestDto) {
        log.info("Creating user {}", userRequestDto);
        return userClient.add(userRequestDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@Validated(Update.class) @RequestBody UserRequestDto userRequestDto,
                                         @PathVariable Long id) {
        log.info("Update user {}, userId={}", userRequestDto, id);
        return userClient.update(userRequestDto, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        log.info("Delete userId={}", id);
        return userClient.remove(id);
    }
}
