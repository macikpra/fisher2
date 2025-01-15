package edu.store.database.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "pracownik", schema = "public")
@NamedQuery(name = "Pracownik.findAll", query = "SELECT u FROM Pracownik u")
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@ToString(exclude = {})
@Getter
@Setter
public class Pracownik implements Serializable {

    /** */
    @Serial
    private static final long serialVersionUID = -4779485869092445937L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PracownikGen")
    @SequenceGenerator(name = "PracownikGen", sequenceName = "pracownik_id_seq", schema = "public", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "imie")
    @Size(min = 1, max = 255, message = " must be longer than 1 and less than 255 characters")
    private String imie;

    @Column(name = "nazwisko")
    @Size(min = 1, max = 255, message = " must be longer than 1 and less than 255 characters")
    private String nazwisko;

    @Column(name = "pesel")
    private BigDecimal pesel;

    @Column(name = "telefon")
    private String telefon;

    @Column(name = "haslo_hash")
    private String hasloHash;
}
