package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public ItemDto createItem(long ownerId, ItemDto itemDto) {
        User user = userRepository.findUserById(ownerId);
        if (user == null) {
            throw new NotFoundException("Нельзя добавить вещь для несуществующего пользователя");
        } else if (itemDto.getAvailable() == null) {
            throw new ConditionsNotMetException("Нет статуса доступности вещи");
        } else if (itemDto.getName() == null || itemDto.getName().isBlank() || itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            throw new ConditionsNotMetException("У вещи должно быть заполнено название и описание");
        }

        Item item = ItemMapper.mapToItem(itemDto);
        item.setOwner(User.builder().id(ownerId).build());
        item = itemRepository.createItem(item);
        return ItemMapper.mapToItemDto(item);
    }

    public ItemDto patchItem(long ownerId, long id, Map<String,Object> updates) {
        Item item = itemRepository.findItemById(id);

        if (item == null) {
            throw new NotFoundException("Вещь не найдена с ID: " + id);
        }
        if (!item.getOwner().getId().equals(ownerId)) {
            throw new NotFoundException("Пользователь может изменять только свои вещи");
        }

        if (updates.containsKey("name") && updates.get("name") != null) {
            item.setName((String) updates.get("name"));
        }

        if (updates.containsKey("description") && updates.get("description") != null) {
            item.setDescription((String) updates.get("description"));
        }

        if (updates.containsKey("available") && updates.get("available") != null) {
            item.setAvailable((Boolean) updates.get("available"));
        }

        item = itemRepository.updateItem(item);
        return ItemMapper.mapToItemDto(item);
    }

    public ItemDto getItemById(long itemid) {
        Item item = itemRepository.findItemById(itemid);
        if (item == null) {
            throw new NotFoundException("Вещь не найдена с ID: " + itemid);
        }

        return ItemMapper.mapToItemDto(item);
    }

    public void removeItem(long ownerId, long itemid) {
        Item item = itemRepository.findItemById(itemid);
        if (item == null) {
            throw new NotFoundException("Вещь не найдена с ID: " + itemid);
        }
        if (!item.getOwner().getId().equals(ownerId)) {
            throw new NotFoundException("Пользователь может удалять только свои вещи");
        }
        itemRepository.removeItem(itemid);
    }

    public List<ItemDto> getItems(long ownerId) {
        return ItemMapper.mapToItemDtoList(itemRepository.findItemByOwner(ownerId));
    }

    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        List<Item> items = itemRepository.findBySearchText(text);

        return items.stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }
}
