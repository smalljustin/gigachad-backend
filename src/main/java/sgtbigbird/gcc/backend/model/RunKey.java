package sgtbigbird.gcc.backend.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "runkey")
public class RunKey {
    @GeneratedValue
    @Id
    private int rkId;
    private String name;
    private int mode;
}
