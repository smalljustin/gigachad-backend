package sgtbigbird.gcc.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "datapoint")
public class DataPoint {
    @GeneratedValue
    @Id
    private int id;
    private int version;
    private String position;
    private String velocity;
    private float frontSpeed;
    private float inputSteer;
    private boolean inputBrake;
    private float inputGas;
    private float flDamperLen;
    private float frDamperLen;
    private float rlDamperLen;
    private float rrDamperLen;
    private String vecVel;
    private String vecDir;
    private String vecLeft;
    private String vecUp;
    private int curGear;
    private String flGroundContactMaterial;
    private String frGroundContactMaterial;
    private String rlGroundContactMaterial;
    private String rrGroundContactMaterial;
    private String reactor;
    private long time;
    private float dt;
}