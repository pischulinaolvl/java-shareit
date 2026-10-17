package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item createItem(Item item);

    int removeItem(Long id);

    Item updateItem(Item item);

    Item findItemById(Long id);

    List<Item> getItems();

    List<Item> findItemByOwner(Long id);

    List<Item> getAvailableItems();

    List<Item> findBySearchText(String text);
}
