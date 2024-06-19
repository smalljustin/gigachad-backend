package sgtbigbird.gcc.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "maptag")
public class MapTag {
    @GeneratedValue
    @Id
    private int mtId;
    private String mapUuid;
    private String tag;
    private String username;

}
