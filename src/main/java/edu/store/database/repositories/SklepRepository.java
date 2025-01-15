package edu.store.database.repositories;

import edu.store.database.entities.Sklep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SklepRepository  extends JpaRepository<Sklep, Long> {
    @Query("SELECT r FROM Sklep r ORDER BY r.id ASC")
    List<Sklep> findAll();

    List<Sklep> findByAdresContainingIgnoreCase(String adres);
}
