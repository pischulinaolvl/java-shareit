package ru.practicum.shareit.item.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemRowMapper implements RowMapper<Item> {
    @Override
    public Item mapRow(ResultSet currentRow, int rowNum) throws SQLException {

        return Item.builder()
                .id(currentRow.getLong("id"))
                .name(currentRow.getString("name"))
                .description(currentRow.getString("description"))
                .available(currentRow.getBoolean("available"))
                .owner(User.builder().id(currentRow.getLong("owner_id")).build())
                .itemRequest(ItemRequest.builder().id(currentRow.getLong("request_id")).build())
                .build();
    }
}

