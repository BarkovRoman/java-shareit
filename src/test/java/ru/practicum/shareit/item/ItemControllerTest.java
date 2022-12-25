package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    @Test
    public void add() {
        ItemDto itemDto = ItemDto.builder().id(1L)
                .available(true)
                .description("Description").name("Name")
                .requestId(null).build();
        Mockito.when(itemService.add(itemDto, 1L))
                .thenReturn(itemDto);

        ItemDto response = itemController.add(1L, itemDto);

        assertEquals("Объекты не совпадают", itemDto, response);
    }

    @Test
    public void addNoUser() {
        ItemDto itemDto = ItemDto.builder().id(1L)
                .available(true)
                .description("Description").name("Name")
                .requestId(null).build();
        Mockito.when(itemService.add(itemDto, 1L))
                .thenThrow(new NotFoundException("User не найден"));

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> itemController.add(1L, itemDto));

        assertEquals("Исключения не совпадают", "User не найден", exception.getMessage());
    }

    @Test
    public void update() {
        ItemDto itemDto = ItemDto.builder().id(1L)
                .available(true)
                .description("Description").name("Name")
                .requestId(1L).build();
        Mockito.when(itemService.update(1L, 1L, itemDto))
                .thenReturn(itemDto);

        ItemDto response = itemController.update(1L,  itemDto, 1L);

        assertEquals("Объекты не совпадают", itemDto, response);
    }

    @Test
    public void getByUser() {
        ItemBookingDto itemDto = new ItemBookingDto(1L, "Name",
                "Description", true,
                new LastNextItemResponseDto(1L, 1L),
                new LastNextItemResponseDto(2L, 2L),
                new ArrayList<>());
        List<ItemBookingDto> items = List.of(itemDto);
        Mockito.when(itemService.getByUser(1L, 0, 5))
                .thenReturn(items);

        List<ItemBookingDto> response = itemController.getByUser(1L, 0, 5);

        assertEquals("Объекты не совпадают", items, response);
    }

    @Test
    public void getById() {
        ItemBookingDto itemDto = new ItemBookingDto(1L, "Name",
                "Description", true,
                new LastNextItemResponseDto(1L, 1L),
                new LastNextItemResponseDto(2L, 2L),
                new ArrayList<>());
        Mockito.when(itemService.getById(1L, 1L))
                .thenReturn(itemDto);

        ItemBookingDto response = itemController.getById(1L, 1L);

        assertEquals("Объекты не совпадают", itemDto, response);
    }

    @Test
    public void searchItem() {
        ItemDto itemDto = ItemDto.builder().id(1L)
                .available(true)
                .description("Description").name("Name")
                .requestId(1L).build();
        List<ItemDto> items = List.of(itemDto);
        Mockito.when(itemService.search("Descrip",0, 5))
                .thenReturn(items);

        List<ItemDto> response = itemController.searchItem("Descrip",0, 5);

        assertEquals("Объекты не совпадают", items, response);
        assertEquals("Объекты не совпадают", itemDto, response.get(0));
    }

    @Test
    public void searchItemEmptyList() {
        List<ItemDto> items = Collections.emptyList();
        Mockito.when(itemService.search("q",0, 5))
                .thenReturn(items);

        List<ItemDto> response = itemController.searchItem("q",0, 5);

        assertEquals("Объекты не совпадают", items, response);
    }

    @Test
    public void addComments() {
        CommentResponseDto  comment = new CommentResponseDto();
        CommentDto commentDto = CommentDto.builder().id(1L).text("Комментарий").build();
        Mockito.when(itemService.addComments(1L, 1L,commentDto))
                .thenReturn(comment);

        CommentResponseDto response = itemController.addComments(1L, commentDto, 1L);

        assertEquals("Объекты не совпадают", comment, response);
    }
}
