package com.revlearn.team1.repository;

import com.revlearn.team1.model.ModulePage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageRepo extends JpaRepository<ModulePage, Long> {
}
