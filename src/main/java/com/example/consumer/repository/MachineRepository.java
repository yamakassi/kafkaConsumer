package com.example.consumer.repository;

import com.example.consumer.model.Machine;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MachineRepository {
    Iterable<Machine> findAll();




    List<Machine> findCriticalDiffValueTemperatureByMachineId(Long machineId, int second);

    Optional<Machine> findById(String id);

    Machine save(Machine machine);


    List<Machine> findCriticalDiffValueTemperature(int second);
}
