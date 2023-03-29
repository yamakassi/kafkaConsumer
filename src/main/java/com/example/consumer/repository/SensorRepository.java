package com.example.consumer.repository;


import com.example.consumer.model.Sensor;

import java.util.Optional;

public interface SensorRepository {
    Iterable<Sensor> findAll();

    Optional<Sensor> findById(String id);

    Sensor save(Sensor sensor);
}
