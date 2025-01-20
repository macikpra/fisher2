package edu.store.database.repositories;


import edu.store.database.entities.TypSprzetu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypSprzetuRepository  extends JpaRepository<TypSprzetu, Long> {
    @Query("SELECT r FROM TypSprzetu r ORDER BY r.id ASC")
    List<TypSprzetu> findAll();
}
