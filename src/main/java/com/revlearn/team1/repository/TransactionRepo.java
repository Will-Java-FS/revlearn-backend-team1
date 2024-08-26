package com.revlearn.team1.repository;

import com.revlearn.team1.model.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepo extends JpaRepository<Integer, TransactionModel>
{
}
