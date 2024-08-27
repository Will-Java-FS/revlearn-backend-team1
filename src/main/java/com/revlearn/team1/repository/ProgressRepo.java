package com.revlearn.team1.repository;

import com.revlearn.team1.model.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgressRepo extends JpaRepository<Long, Progress> {
}
