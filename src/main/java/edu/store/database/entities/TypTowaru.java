package edu.store.database.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "typ_towaru", schema = "public")
@NamedQuery(name = "TowarType.findAll", query = "SELECT u FROM TypTowaru u")
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@ToString(exclude = {})
@Getter
public class TypTowaru implements Serializable {
    @Serial
    private static final long serialVersionUID = -4779485869092445937L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TypTowaruGen")
    @SequenceGenerator(name = "TypTowaruGen", sequenceName = "typ_towaru_id_seq", schema = "public", allocationSize = 1)
    @Column(name = "id")
    private Long id;
    @Column(name = "nazwa")
    String nazwa;
    public TypTowaru(String nazwa, Long id) {
        this.nazwa = nazwa;
        this.id = id;
    }
}
