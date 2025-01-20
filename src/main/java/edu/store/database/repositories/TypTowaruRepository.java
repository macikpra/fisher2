package edu.store.database.repositories;

import edu.store.database.entities.Towar;
import edu.store.database.entities.TypTowaru;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypTowaruRepository  extends JpaRepository<TypTowaru, Long> {
    @Query("SELECT r FROM TypTowaru r ORDER BY r.id ASC")
    List<TypTowaru> findAll();
}
