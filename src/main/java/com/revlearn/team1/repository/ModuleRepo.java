package com.revlearn.team1.repository;

import com.revlearn.team1.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepo extends JpaRepository<Module, Long> {
}
