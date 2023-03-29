package com.example.consumer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@Data @AllArgsConstructor @NoArgsConstructor
public class Sensor {
    @JsonIgnoreProperties
    private  Long id;
    @JsonIgnoreProperties
    private Long machineId;
    private SensorType sensorType;
    private double value;

    private Timestamp dateValue;


}
