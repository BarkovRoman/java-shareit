package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemControllerTest {
    @MockBean
    private ItemClient itemClient;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    public void add() throws Exception {
        // given
        long userId = 1L;
        ItemRequestDto requestDto = new ItemRequestDto("Name", "Description", true, 1L);

        // when + then
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(itemClient).add(requestDto, userId);
    }

    @Test
    public void update() throws Exception {
        // given
        long userId = 1L;
        long itemId = 1L;

        ItemRequestDto requestDto = new ItemRequestDto("Name", "Description", true, 1L);

        // when + then
        mockMvc.perform(patch("/items/{itemId}", userId)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(itemClient).update(userId, itemId, requestDto);
    }

    @Test
    public void getByUser() throws Exception {
        // given
        long userId = 1L;

        // when + then
        mockMvc.perform(get("/items/{itemId}?from=0&size=10", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getById() throws Exception {
        // given
        long userId = 1L;
        long itemId = 1L;

        // when + then
        mockMvc.perform(get("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(itemClient).getById(itemId, userId);
    }

    @Test
    public void searchItem() throws Exception {
        // given
        long userId = 1L;

        // when + then
        mockMvc.perform(get("/items/search?text=text&from=0&size=10")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(itemClient).search("text", 0, 10, userId);
    }

    @Test
    public void addComments() throws Exception {
        // given
        long userId = 1L;
        long itemId = 1L;
        CommentRequestDto commentRequestDto = new CommentRequestDto("text");

        // when + then
        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(commentRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(itemClient).addComments(userId, itemId, commentRequestDto);
    }
}
