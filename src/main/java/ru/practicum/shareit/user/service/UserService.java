package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.*;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto createUser(UserDto userDto) {
        if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }

        if (!userDto.getEmail().contains("@")) {
            throw new ConditionsNotMetException("Имейл должен содержать символ @");
        }

        User alreadyExistUser = userRepository.findUserByEmail(userDto.getEmail());
        if (alreadyExistUser != null) {
            throw new ConflictException("Данный email уже используется");
        }

        User user = UserMapper.mapToUser(userDto);

        user = userRepository.createUser(user);

        return UserMapper.mapToUserDto(user);
    }

    public UserDto getUserById(long userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден с ID: " + userId);
        }

        return UserMapper.mapToUserDto(user);
    }

    public void removeUser(long userId) {
        if (userRepository.removeUser(userId) == 0) {
            throw new NotFoundException("Пользователь не найден с ID: " + userId);
        }
    }

    public List<UserDto> getUsers() {
        return UserMapper.mapToUserDtoList(userRepository.getUsers());
    }

    public UserDto updateUser(UserDto userDto) {
        User updatedUser = userRepository.findUserById(userDto.getId());
        if (updatedUser == null) {
            throw new NotFoundException("Пользователь не найден с ID: " + userDto.getId());
        }
        updatedUser = UserMapper.mapToUser(userDto);
        updatedUser = userRepository.updateUser(updatedUser);
        return UserMapper.mapToUserDto(updatedUser);
    }

    public UserDto patchUser(Long id, Map<String, Object> updates) {
        User user = userRepository.findUserById(id);

        if (user == null) {
            throw new NotFoundException("Пользователь не найден с ID: " + id);
        }

        if (updates.containsKey("name") && updates.get("name") != null) {
            user.setName((String) updates.get("name"));
        }

        if (updates.containsKey("email") && updates.get("email") != null) {
            user.setEmail((String) updates.get("email"));
        }

        user = userRepository.updateUser(user);
        return UserMapper.mapToUserDto(user);
    }
}
