package edu.store.database.repositories;

import edu.store.database.entities.Towar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TowarRepository extends JpaRepository<Towar, Long> {
    @Query("SELECT r FROM Towar r ORDER BY r.id ASC")
    List<Towar> findAll();
    @Query("SELECT r FROM Towar r WHERE r.sklepId = :sklepId ORDER BY r.id ASC")
    List<Towar> findBySklepId(@Param("sklepId") Long sklepId);

    List<Towar> findByNazwaContainingIgnoreCaseAndTypTowaruAndSklepId(String nazwa, Long typTowaru, Long sklepId);
    List<Towar> findByNazwaContainingIgnoreCaseAndSklepId(String nazwa, Long sklepId);
    List<Towar> findByTypTowaruAndSklepId(Long typTowaru, Long sklepId);
}
