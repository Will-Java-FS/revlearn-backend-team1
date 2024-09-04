package com.revlearn.team1.mapper;

import com.revlearn.team1.dto.TransactionDTO;
import com.revlearn.team1.dto.transaction.TransactionRequestDTO;
import com.revlearn.team1.dto.transaction.TransactionResponseDTO;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.TransactionModel;
import com.revlearn.team1.model.User;
import com.revlearn.team1.repository.UserRepository;
import com.revlearn.team1.service.user.UserService;
import com.revlearn.team1.service.user.UserServiceImp;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TransactionMapper 
{
    @Autowired
    private final UserServiceImp userService;

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