package com.example.consumer.model;



import lombok.*;

import java.util.Date;
import java.util.List;
@Data
public class Machine {

    private Long id;
    private String machineName;

    private StatusMachine machineStatus;
    private Date machineServiceDate;
    private List<Sensor> sensors;

}
