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
@Table(name = "towar", schema = "public")
@NamedQuery(name = "Towar.findAll", query = "SELECT u FROM Towar u")
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@ToString(exclude = {})
@Getter
@Setter
public class Towar implements Serializable {
    @Serial
    private static final long serialVersionUID = -4779485869092445937L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TowarGen")
    @SequenceGenerator(name = "TowarGen", sequenceName = "towar_id_seq", schema = "public", allocationSize = 1)
    @Column(name = "id")
    private Long id;
    @Column(name = "sklep_id")
    private Long sklepId;
    @Column(name = "nazwa")
    private String nazwa;
    @Column(name = "typ_towaru")
    private Long typTowaru;
    @Column(name = "ilosc")
    private Long ilosc;
    @Column(name = "cena")
    private Float cena;
}
