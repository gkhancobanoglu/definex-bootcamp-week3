package dev.patika.definexjavaspringbootbootcamp2025.hw3.service;

import dev.patika.definexjavaspringbootbootcamp2025.hw3.dto.User;
import dev.patika.definexjavaspringbootbootcamp2025.hw3.exception.NoSuchUserException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final Map<UUID, User> users = new HashMap<>();

    @Override
    public List<User> list() {
        return List.copyOf(users.values());
    }

    @Override
    public User create(User user) {
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        users.put(userId, user);
        return user;
    }

    @Override
    public User getUserProfile(UUID userId) throws NoSuchUserException {
        return Optional.ofNullable(users.get(userId))
                .orElseThrow(NoSuchUserException::new);
    }

    @Override
    public User update(User user) throws NoSuchUserException {
        UUID userId = user.getId();
        if (!users.containsKey(userId)) {
            throw new NoSuchUserException();
        }
        users.put(userId, user);
        return user;
    }
}
