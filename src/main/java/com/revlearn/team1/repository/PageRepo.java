package com.revlearn.team1.repository;

import com.revlearn.team1.model.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageRepo extends JpaRepository<Page, Long> {
}
