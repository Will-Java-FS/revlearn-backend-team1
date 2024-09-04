package com.revlearn.team1.mapper;

import com.revlearn.team1.dto.transaction.TransactionRequestDTO;
import com.revlearn.team1.dto.transaction.TransactionResponseDTO;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.TransactionModel;
import com.revlearn.team1.model.User;
import com.revlearn.team1.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionMapper 
{
    private final UserService userService;

    // TODO: @Mapper annotation can automatically generate these methods. Implement
    // later if needed
    public TransactionResponseDTO toDTO(String url, String message)
    {
        return new TransactionResponseDTO(
                url,
                message
        );
    }

    public TransactionModel fromDTO(TransactionRequestDTO transactionDTO) 
    {
        return new TransactionModel(
                0L, // transaction ID
                transactionDTO.name(),
                transactionDTO.description(),
                transactionDTO.price(),
                transactionDTO.quantity(),
                new Course(),
                new User(),
                userService.findById((int) transactionDTO.id()).orElse(new User())
        );
    }
}