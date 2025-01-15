package edu.store.database.repositories;

import edu.store.database.entities.Pracownik;
import edu.store.database.entities.PracownikRola;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PracownikRolaRepository extends JpaRepository<PracownikRola, Long> {
    PracownikRola findByPracownikId(Long id);
}
