package edu.store.database.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "sprzet", schema = "public")
@NamedQuery(name = "Sprzet.findAll", query = "SELECT u FROM Sklep u")
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@ToString(exclude = {})
@Getter
@Setter
public class Sprzet implements Serializable {
    @Serial
    private static final long serialVersionUID = -4779485869092445937L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SprzetGen")
    @SequenceGenerator(name = "SprzetGen", sequenceName = "sprzet_id_seq", schema = "public", allocationSize = 1)
    @Column(name = "id")
    private Long id;
    @Column(name = "sklep_id")
    private Long sklepId;
    @Column(name = "nazwa")
    private String nazwa;
    @Column(name = "typ_sprzetu")
    private Long typSprzetu;
    @Column(name = "cena")
    private Float cena;
}
