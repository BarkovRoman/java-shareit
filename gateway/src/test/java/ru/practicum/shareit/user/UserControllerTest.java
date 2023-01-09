package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserRequestDto;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerTest {
    @MockBean
    private UserClient userClient;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    public void add() throws Exception {
        // given
        long userId = 1L;
        UserRequestDto userDto = new UserRequestDto(1L, "Name", "name@email.ru");

        // when + then
        mockMvc.perform(post("/users")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(userClient).add(userDto);
    }

    @Test
    public void getById() throws Exception {
        long userId = 1L;

        // when + then
        mockMvc.perform(get("/users/{userId}", userId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(userClient).getById(userId);
    }

    @Test
    public void getAll() throws Exception {
        long userId = 1L;

        // when + then
        mockMvc.perform(get("/users", userId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(userClient).getAll();
    }

    @Test
    public void update() throws Exception {
        // given
        long userId = 1L;
        UserRequestDto userDto = new UserRequestDto(1L, "Name", "name@email.ru");
        // when + then
        mockMvc.perform(patch("/users/{userId}", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(userClient).update(userDto, userId);
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
        verify(userClient).remove(userId);
    }
}
