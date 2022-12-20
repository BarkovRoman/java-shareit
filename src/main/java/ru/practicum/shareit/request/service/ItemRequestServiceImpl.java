package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemRequestIdResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper mapper;
    private final ItemMapper itemMapper;

    @Override
    @Transactional
    public ItemRequestCreateDto add(ItemRequestDto itemRequestDto, Long userId) {
        User requestor = isExistsUserById(userId);
        ItemRequest itemRequest = itemRequestRepository.save(mapper.toItemRequest(itemRequestDto, requestor));
        log.info("Добавлен ItemRequest {}", itemRequest);
        return mapper.toItemRequestCreateDto(itemRequest);
    }

    @Override
    public List<ItemRequestResponseDto> getByUser(Long userId) {
        isExistsUserById(userId);
        List<ItemRequest> requests = itemRequestRepository.findByRequestorId(userId);
        Map<ItemRequest, List<Item>> items = itemRepository.findByRequestIn(requests)
                .stream()
                .collect(groupingBy(Item::getRequest, toList()));
        List<ItemRequestIdResponseDto> itemRequestId;
        List<ItemRequestResponseDto> itemRequestResponseDtos = new ArrayList<>();

        for (ItemRequest request : requests) {
            int itemSize = 0;
            if (items.containsKey(request)) {
                itemSize = items.get(request).size();
            }
            itemRequestId = itemSize == 0 ? new ArrayList<>() : itemMapper.mapItemOwner(items.get(request));
            itemRequestResponseDtos.add(mapper.toItemRequestResponseDto(request, itemRequestId));
        }
        return itemRequestResponseDtos;
    }

    @Override
    public ItemRequestResponseDto getById(Long requestId, Long userId) {
        isExistsUserById(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("ItemRequest id=%s не найден", requestId)));
        List<Item> items = itemRepository.findByRequest(itemRequest);
        List<ItemRequestIdResponseDto> itemOwner = itemMapper.mapItemOwner(items);
        return mapper.toItemRequestResponseDto(itemRequest, itemOwner);
    }

    @Override
    public List<ItemRequestResponseDto> getAll(Integer from, Integer size, Long userId) {
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(from, size, sortById);
        List<ItemRequestResponseDto> itemRequest = new ArrayList<>();
        do {
            Page<ItemRequest> requestPage = itemRequestRepository.findByRequestorIdNot(page, userId);
            Map<ItemRequest, List<Item>> items = itemRepository.findByRequestIn(requestPage.getContent())
                    .stream()
                    .collect(groupingBy(Item::getRequest, toList()));
            requestPage.getContent().forEach(request -> {
                List<ItemRequestIdResponseDto> itemOwner;
                int itemSize = 0;
                if (items.containsKey(request)) {
                    itemSize = items.get(request).size();
                }
                itemOwner = itemSize == 0 ? new ArrayList<>() : itemMapper.mapItemOwner(items.get(request));

                itemRequest.add(mapper.toItemRequestResponseDto(request, itemOwner));
            });
            if (requestPage.hasNext()) {
                page = PageRequest.of(requestPage.getNumber() + 1, requestPage.getSize(), requestPage.getSort());
            } else {
                page = null;
            }
        } while (page != null);
        return itemRequest;
    }

    private User isExistsUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User id=%s не найден", id)));
    }
}

    /*public List<ItemRequestResponseDto> getAll(Integer from, Integer size, Long userId) {
        // сначала создаём описание сортировки по полю id
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        // затем создаём описание первой "страницы" размером size элемента
        Pageable page = PageRequest.of(from, size, sortById);
        List<ItemRequestResponseDto> itemRequestResponseDtos = new ArrayList<>();

        do {
            // запрашиваем у базы данных страницу с данными
            Page<ItemRequest> requestPage = itemRequestRepository.findByRequestorIdNot(page, userId);
            // результат запроса получаем с помощью метода getContent()

            Map<ItemRequest, List<Item>> items = itemRepository.findByRequestIn(requestPage.getContent())
                    .stream()
                    .collect(groupingBy(Item::getRequest, toList()));




            requestPage.getContent().forEach(request -> {
                List<ItemOwnerResponseDto> itemOwner;
                int itemSize = 0;
                if(items.containsKey(request)) {
                    itemSize = items.get(request).size();
                }
                itemOwner = itemSize == 0 ? new ArrayList<>() : itemMapper.mapItemOwner(items.get(request));

                itemRequestResponseDtos.add(mapper.toItemRequestResponseDto(request, itemOwner));

                // проверяем пользователей
            });

            // для типа Page проверяем, существует ли следующая страница
            if(requestPage.hasNext()){
                // если следующая страница существует, создаём её описание, чтобы запросить на следующей итерации цикла
                page = PageRequest.of(requestPage.getNumber() + 1, requestPage.getSize(), requestPage.getSort()); // или для простоты -- userPage.nextOrLastPageable()
            } else {
                page = null;
            }
        } while (page != null);
        return itemRequestResponseDtos;
    }*/
