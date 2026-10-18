package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDto mapToUserDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto dto = new UserDto();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());

        return dto;
    }

       public static User mapToUser(UserDto dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        // user.setId(dto.getId()); <-- Эту строку НЕ пишем!

        return user;
    }

    public static List<UserDto> mapToUserDtoList(List<User> users) {
        if (users == null) {
            return List.of(); // Возвращаем пустой неизменяемый список вместо null
        }

        return users.stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }
}
