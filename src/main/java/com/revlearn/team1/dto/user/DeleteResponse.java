package com.revlearn.team1.dto.user;

import com.revlearn.team1.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteResponse {
    private User user;
    private String message;
}
