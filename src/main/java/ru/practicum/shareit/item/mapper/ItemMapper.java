package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

public final class ItemMapper {

    public static ItemDto mapToItemDto(Item item) {
        if (item == null) {
            return null;
        }

        ItemDto dto = new ItemDto();

        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.isAvailable());

        return dto;
    }

    public static Item mapToItem(ItemDto dto) {
        if (dto == null) {
            return null;
        }

        Item item = new Item();
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());

        return item;
    }

    public static List<ItemDto> mapToItemDtoList(List<Item> items) {
        if (items == null) {
            return List.of();
        }

        return items.stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }
}