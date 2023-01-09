package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import static org.springframework.data.domain.Sort.Direction.DESC;

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
        List<ItemRequest> requests = itemRequestRepository.findByRequestorId(userId, Sort.by(DESC, "created"));
        Map<ItemRequest, List<Item>> items = itemRepository.findByRequestIn(requests)
                .stream()
                .collect(groupingBy(Item::getRequest, toList()));
        List<ItemRequestIdResponseDto> itemRequestId;
        List<ItemRequestResponseDto> itemRequests = new ArrayList<>();

        for (ItemRequest request : requests) {
            long itemSize = 0;
            if (items.containsKey(request)) {
                itemSize = items.get(request).size();
            }
            itemRequestId = itemSize == 0 ? new ArrayList<>() : itemMapper.mapItemOwner(items.get(request));
            itemRequests.add(mapper.toItemRequestResponseDto(request, itemRequestId));
        }
        return itemRequests;
    }

    @Override
    public ItemRequestResponseDto getById(Long requestId, Long userId) {
        isExistsUserById(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("ItemRequest id=%s не найден", requestId)));
        List<Item> items = itemRepository.findByRequest(itemRequest);
        List<ItemRequestIdResponseDto> itemRequests = itemMapper.mapItemOwner(items);
        return mapper.toItemRequestResponseDto(itemRequest, itemRequests);
    }

    @Override
    public List<ItemRequestResponseDto> getAll(Integer from, Integer size, Long userId) {
        Sort sortById = Sort.by(DESC, "created");
        final PageRequest page = PageRequest.of(from, size, sortById);
        List<ItemRequestResponseDto> itemRequestId = new ArrayList<>();
        Page<ItemRequest> requestPage = itemRequestRepository.findByRequestorIdNot(page, userId);
        Map<ItemRequest, List<Item>> items = itemRepository.findByRequestIn(requestPage.getContent())
                .stream()
                .collect(groupingBy(Item::getRequest, toList()));
        requestPage.getContent().forEach(request -> {
            List<ItemRequestIdResponseDto> itemRequests;
            int itemSize = 0;
            if (items.containsKey(request)) {
                itemSize = items.get(request).size();
            }
            itemRequests = itemSize == 0 ? new ArrayList<>() : itemMapper.mapItemOwner(items.get(request));

            itemRequestId.add(mapper.toItemRequestResponseDto(request, itemRequests));
        });
        return itemRequestId;
    }

    private User isExistsUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User id=%s не найден", id)));
    }
}
