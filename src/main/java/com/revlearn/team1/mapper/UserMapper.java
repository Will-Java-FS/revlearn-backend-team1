package com.revlearn.team1.mapper;

import com.revlearn.team1.dto.user.UserResDTO;
import com.revlearn.team1.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResDTO toResDTO(User user) {
        return new UserResDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getFirstName(),
                user.getLastName());
    }
}
