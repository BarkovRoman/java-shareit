package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerTest {

    @Mock
    private ItemRequestService itemRequestService;

    @InjectMocks
    private ItemRequestController itemRequestController;

    @Test
    public void add() {
        ItemRequestCreateDto requestCreateDtoDto = new ItemRequestCreateDto();
        ItemRequestDto itemRequestDto = ItemRequestDto.builder().id(1L).description("Description").build();
        Mockito
                .when(itemRequestService.add(itemRequestDto, 1L))
                .thenReturn(requestCreateDtoDto);

        ItemRequestCreateDto response = itemRequestController.add(1L, itemRequestDto);

        assertEquals("Объекты не совпадают", requestCreateDtoDto, response);
    }

    @Test
    public void getById() {
        ItemRequestResponseDto itemRequestDto = new ItemRequestResponseDto();
        Mockito
                .when(itemRequestService.getById(1L, 1L))
                .thenReturn(itemRequestDto);

        ItemRequestResponseDto response = itemRequestController.getById(1L, 1L);

        assertEquals("Объекты не совпадают", itemRequestDto, response);
    }

    @Test
    public void getByUser() {
        List<ItemRequestResponseDto> itemRequestDto = new ArrayList<>();
        Mockito
                .when(itemRequestService.getByUser(1L))
                .thenReturn(itemRequestDto);

        List<ItemRequestResponseDto> response = itemRequestController.getByUser(1L);

        assertEquals("Объекты не совпадают", itemRequestDto, response);
    }

    @Test
    public void getAll() {
        List<ItemRequestResponseDto> itemRequestDto = new ArrayList<>();
        Mockito
                .when(itemRequestService.getAll(0,5,1L))
                .thenReturn(itemRequestDto);

        List<ItemRequestResponseDto> response = itemRequestController.getAll(1L, 0,5);

        assertEquals("Объекты не совпадают", itemRequestDto, response);
    }

    @Test
    public void addNoUser() {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder().id(1L).description("Description").build();
        Mockito.when(itemRequestService.add(itemRequestDto, 1L))
                .thenThrow(new NotFoundException("Request не найден"));

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> itemRequestController.add(1L, itemRequestDto));

        assertEquals("Исключения не совпадают", "Request не найден", exception.getMessage());
    }
}
