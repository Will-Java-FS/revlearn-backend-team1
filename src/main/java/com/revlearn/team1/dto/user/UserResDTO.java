package com.revlearn.team1.dto.user;

import com.revlearn.team1.enums.Roles;

public record UserResDTO(int id, String username, String email, Roles role, String firstName, String lastName) {
}
