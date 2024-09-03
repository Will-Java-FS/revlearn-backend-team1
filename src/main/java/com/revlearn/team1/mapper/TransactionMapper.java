package com.revlearn.team1.mapper;

import org.springframework.stereotype.Component;

import com.revlearn.team1.dto.transaction.TransactionRequestDTO;
import com.revlearn.team1.dto.transaction.TransactionResponseDTO;
import com.revlearn.team1.model.TransactionModel;
import com.revlearn.team1.model.User;
import com.revlearn.team1.service.user.UserService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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

    public TransactionModel fromDTO(TransactionRequestDTO transactionDTO) {
        return new TransactionModel(
                transactionDTO.transactionItem().getId(),
                transactionDTO.transactionItem().getUserId(),
                transactionDTO.transactionItem().getCourseId(),
                transactionDTO.transactionItem().getName(),
                transactionDTO.transactionItem().getPrice(),
                transactionDTO.transactionItem().getQuantity(),
                transactionDTO.transactionItem().getCourse(),
                transactionDTO.transactionItem().getFromUser(),
                transactionDTO.transactionItem().getToUser()
        );
    }
}
