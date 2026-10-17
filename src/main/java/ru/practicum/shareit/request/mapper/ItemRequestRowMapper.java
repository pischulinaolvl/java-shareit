package ru.practicum.shareit.request.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.practicum.shareit.request.dto.ItemRequestRow;
import ru.practicum.shareit.request.model.ItemRequest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ItemRequestRowMapper implements RowMapper<ItemRequestRow> {
    @Override
    public ItemRequestRow mapRow(ResultSet rs, int rowNum) throws SQLException {
        ItemRequestRow itemRequest = new ItemRequestRow();
        itemRequest.setId(rs.getLong("id"));
        itemRequest.setDescription(rs.getString("description"));
        itemRequest.setRequestor(rs.getLong("requestor"));

        Timestamp ts = rs.getTimestamp("created");
        LocalDateTime ldt = ts.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        itemRequest.setCreated(ldt);

        return itemRequest;
    }
}
