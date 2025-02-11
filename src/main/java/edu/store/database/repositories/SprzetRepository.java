package edu.store.database.repositories;

import edu.store.database.entities.Sprzet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SprzetRepository extends JpaRepository<Sprzet, Long> {
    @Query("SELECT r FROM Sprzet r ORDER BY r.id ASC")
    List<Sprzet> findAll();
    @Query("SELECT r FROM Sprzet r WHERE r.sklepId = :sklepId ORDER BY r.id ASC")
    List<Sprzet> findBySklepId(@Param("sklepId") Long sklepId);

    List<Sprzet> findByNazwaContainingIgnoreCaseAndTypSprzetuAndSklepId(String nazwa, Long typSprzetu,Long sklepId);
    List<Sprzet> findByNazwaContainingIgnoreCaseAndSklepId(String nazwa, Long sklepId);
    List<Sprzet> findByTypSprzetuAndSklepId(Long typSprzetu, Long sklepId);

}
