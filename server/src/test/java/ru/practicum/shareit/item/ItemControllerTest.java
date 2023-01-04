package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemControllerTest {

    @MockBean
    private ItemService itemService;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private ItemDto itemDto;
    private ItemBookingDto itemBookingDto;

    @BeforeEach
    public void setUp() {
        itemDto = ItemDto.builder().id(1L)
                .available(true)
                .description("Description").name("Name")
                .requestId(null).build();

        itemBookingDto = new ItemBookingDto(1L, "Name", "Description", true,
                new LastNextItemResponseDto(1L, 1L),
                new LastNextItemResponseDto(1L, 1L),
                new ArrayList<>());
    }

    @Test
    public void add() throws Exception {
    // given
        long userId = 1L;

        when(itemService.add(any(), anyLong())).thenReturn(itemDto);

        // when + then
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()));
    }

    @Test
    public void update() throws Exception {
        // given
        long userId = 1L;

        when(itemService.update(anyLong(), anyLong(), any())).thenReturn(itemDto);

        // when + then
        mockMvc.perform(patch("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()));
    }

    @Test
    public void getByUser() throws Exception {
        // given
        long userId = 1L;
        List<ItemBookingDto> items = List.of(itemBookingDto);

        when(itemService.getByUser(anyLong(), anyInt(), anyInt())).thenReturn(items);
        // when + then
        mockMvc.perform(get("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getById() throws Exception {
        // given
        long userId = 1L;

        when(itemService.getById(anyLong(), anyLong())).thenReturn(itemBookingDto);
        // when + then
        mockMvc.perform(get("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()));
    }

    @Test
    public void searchItem() throws Exception {
        // given
        long userId = 1L;
        List<ItemDto> items = List.of(itemDto);

        when(itemService.search(anyString(), anyInt(), anyInt())).thenReturn(items);
        // when + then
        mockMvc.perform(get("/items/search?text=desc")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(items.get(0).getId()));
    }

    @Test
    public void addComments() throws Exception {
        // given
        long userId = 1L;
        CommentResponseDto commentResponseDto = new CommentResponseDto(1L, "text", "Name", LocalDateTime.now());
        CommentDto commentDto = CommentDto.builder().id(1L).text("text").build();

        when(itemService.addComments(anyLong(), anyLong(), any())).thenReturn(commentResponseDto);

        // when + then
        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(commentDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()));
    }
}
