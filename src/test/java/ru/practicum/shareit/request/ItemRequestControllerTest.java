package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestControllerTest {

    @MockBean
    private ItemRequestService itemRequestService;

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private ItemRequestResponseDto itemRequestResponseDto;

    @BeforeEach
    public void setUp() {
        itemRequestResponseDto = new ItemRequestResponseDto(1L, "Description", LocalDateTime.now(), new ArrayList<>());
    }

    @Test
    public void addItem() throws Exception {
        // given
        long userId = 1L;
        ItemRequestCreateDto itemRequestCreateDto = new ItemRequestCreateDto(1L, "Description", LocalDateTime.now());

        when(itemRequestService.add(any(), anyLong())).thenReturn(itemRequestCreateDto);
        // when + then
        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemRequestCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestCreateDto.getId()));
    }

    @Test
    public void getById() throws Exception {
        // given
        long userId = 1L;

        when(itemRequestService.getById(anyLong(), anyLong())).thenReturn(itemRequestResponseDto);
        // when + then
        mockMvc.perform(get("/requests/{requestId}", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestResponseDto.getId()));
    }

    @Test
    public void getByUser() throws Exception {
        // given
        long userId = 1L;
        List<ItemRequestResponseDto> items = List.of(itemRequestResponseDto);

        when(itemRequestService.getByUser(anyLong())).thenReturn(items);
        // when + then
        mockMvc.perform(get("/requests", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(items.get(0).getId()));
    }

    @Test
    public void getAll() throws Exception {
        // given
        long userId = 1L;
        List<ItemRequestResponseDto> items = List.of(itemRequestResponseDto);

        when(itemRequestService.getAll(anyInt(), anyInt(), anyLong())).thenReturn(items);
        // when + then
        mockMvc.perform(get("/requests/all", 1L)
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(items.get(0).getId()));
    }
}
