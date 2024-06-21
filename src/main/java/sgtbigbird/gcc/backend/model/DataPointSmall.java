package sgtbigbird.gcc.backend.model;

import lombok.Data;

@Data
public class DataPointSmall {
    public DataPointSmall(DataPoint dp) {
        this.pidx = dp.getPidx();
        this.runId = dp.getRunId();
        this.respawnId = dp.getRespawnId();
        this.username = dp.getUsername();
        this.version = dp.getVersion();
        this.vehicleType = dp.getVehicleType();
        this.speed = dp.getSpeed();
        this.frontSpeed = dp.getFrontSpeed();
        this.inputSteer = dp.getInputSteer();
        this.inputBrake = dp.isInputBrake();
        this.inputGas = dp.getInputGas();
        this.slipDir = dp.getSlipDir();
        this.slipLeft = dp.getSlipLeft();
        this.curGear = dp.getCurGear();
        this.flGroundContactMaterial = dp.getFlGroundContactMaterial();
        this.frGroundContactMaterial = dp.getFrGroundContactMaterial();
        this.rlGroundContactMaterial = dp.getRlGroundContactMaterial();
        this.rrGroundContactMaterial = dp.getRrGroundContactMaterial();
        this.flIcing01 = dp.getFlIcing01();
        this.frIcing01 = dp.getFrIcing01();
        this.rlIcing01 = dp.getRlIcing01();
        this.rrIcing01 = dp.getRrIcing01();
        this.reactor = dp.getReactor();
        this.time = dp.getTime();
        this.dt = dp.getDt();
    }
    private int pidx;
    private String runId;
    private String respawnId;
    private String username;
    private int version;
    private String vehicleType;
    private float speed;
    private float frontSpeed;
    private float inputSteer;
    private boolean inputBrake;
    private float inputGas;
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
}
