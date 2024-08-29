package com.revlearn.team1.service.user;

import java.util.List;
import java.util.Optional;

import com.revlearn.team1.model.User;

public interface UserService {
    Optional<User> findById(Long id);

    User save(User user);

    void deleteById(Long id);

    List<User> getAll();
}