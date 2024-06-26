package sgtbigbird.gcc.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "datapoint")
public class DataPoint {
    @GeneratedValue
    @Id
    private int dpId;
    private int pidx;
    private String runId;
    private String respawnId;
    private String username;
    private int version;
    private String mapUuid;
    private String vehicleType;
    private String position;
    private String velocity;
    private float speed;
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
    private float slipDir;
    private float slipLeft;
    private int curGear;
    private String flGroundContactMaterial;
    private String frGroundContactMaterial;
    private String rlGroundContactMaterial;
    private String rrGroundContactMaterial;
    private float flIcing01;
    private float frIcing01;
    private float rlIcing01;
    private float rrIcing01;
    private String reactor;
    private long time;
    private float dt;
    @ManyToOne
    private RunKey runKey;
}