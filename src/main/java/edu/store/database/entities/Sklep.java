package edu.store.database.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "sklep", schema = "public")
@NamedQuery(name = "Sklep.findAll", query = "SELECT u FROM Sklep u")
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@ToString(exclude = {})
@Getter
@Setter
public class Sklep implements Serializable {
    @Serial
    private static final long serialVersionUID = -4779485869092445937L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SklepGen")
    @SequenceGenerator(name = "SklepGen", sequenceName = "sklep_id_seq", schema = "public", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "adres")
    @Size(min = 1, max = 255, message = " must be longer than 1 and less than 255 characters")
    private String adres;
}
