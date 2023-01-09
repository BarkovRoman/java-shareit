package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.RequestDto;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestControllerTest {
    @MockBean
    private ItemRequestClient requestClient;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    public void addItem() throws Exception {
        // given
        long userId = 1L;
        RequestDto requestDto = new RequestDto("Description");

        // when + then
        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(requestClient).add(requestDto, userId);
    }

    @Test
    public void getById() throws Exception {
        // given
        long userId = 1L;
        long requestId = 1L;

        // when + then
        mockMvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(requestClient).getById(requestId, userId);
    }

    @Test
    public void getByUser() throws Exception {
        // given
        long userId = 1L;

        // when + then
        mockMvc.perform(get("/requests", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(requestClient).getByUser(userId);
    }

    @Test
    public void getAll() throws Exception {
        // given
        long userId = 1L;

        // when + then
        mockMvc.perform(get("/requests/all?from=0&size=10", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(requestClient).getAll(0, 10, userId);
    }
}
