package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerTest {
    @MockBean
    private UserService userService;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        userDto = UserDto.builder().id(1L).name("Name").email("Name@email.ru").build();
    }

    @Test
    public void add() throws Exception {
        // given
        long userId = 1L;

        when(userService.add(any())).thenReturn(userDto);
        // when + then
        mockMvc.perform(post("/users")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()));
    }

    @Test
    public void getById() throws Exception {
        long userId = 1L;

        when(userService.getById(anyLong())).thenReturn(userDto);
        // when + then
        mockMvc.perform(get("/users/{userId}", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()));
    }

    @Test
    public void getAll() throws Exception {
        long userId = 1L;
        List<UserDto> users = List.of(userDto);

        when(userService.getAll()).thenReturn(users);
        // when + then
        mockMvc.perform(get("/users", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(users.get(0).getId()));
    }

    @Test
    public void update() throws Exception {
        // given
        long userId = 1L;
        // when + then
        when(userService.update(any(), anyLong())).thenReturn(userDto);
        mockMvc.perform(patch("/users/{userId}", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()));
    }

    @Test
    public void deleteById() throws Exception {
        // given
        long userId = 1L;
        // when + then
        mockMvc.perform(delete("/users/{userId}", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
