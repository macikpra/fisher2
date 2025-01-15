package edu.store.database.repositories;

import edu.store.database.entities.Pracownik;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PracownikRepository extends JpaRepository<Pracownik, Long> {
    @Query("SELECT r FROM Pracownik r ORDER BY r.nazwisko ASC")
    List<Pracownik> findAll();

    @Query("SELECT r FROM Pracownik r where r.pesel = ?1 ORDER BY r.id")
    List<Pracownik> findAllByPesel(String pesel);

    @Query("SELECT r FROM Pracownik r where r.imie = ?1 ORDER BY r.id")
    List<Pracownik> findAllByImie(String imie);

    @Query("SELECT r FROM Pracownik r where r.nazwisko = ?1  ORDER BY r.id")
    List<Pracownik> findAllByNazwisko(String nazwisko);

    @Query("SELECT r FROM Pracownik r where r.imie= ?1 and r.nazwisko = ?2  ORDER BY r.id")
    List<Pracownik> findAllByImieAndNazwisko(String imie, String nazwisko);

    List<Pracownik> findByImieContainingIgnoreCaseAndNazwiskoContainingIgnoreCase(String imie, String nazwisko);

    List<Pracownik> findByImieContainingIgnoreCase(String imie);

    List<Pracownik> findByNazwiskoContainingIgnoreCase(String nazwisko);
}
