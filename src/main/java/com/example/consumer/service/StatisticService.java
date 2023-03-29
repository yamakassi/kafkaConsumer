package com.example.consumer.service;

import com.example.consumer.model.Machine;
import com.example.consumer.repository.JdbcMachineRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class StatisticService {
    private final JdbcMachineRepository jdbcMachineRepository;

    public StatisticService(JdbcMachineRepository jdbcMachineRepository) {
        this.jdbcMachineRepository = jdbcMachineRepository;
    }

    public Set<Machine> test() {
        return jdbcMachineRepository.findAll();
    }

    public List<Machine> findCriticalDiffValueTemperature(int seconds) {

        return jdbcMachineRepository.findCriticalDiffValueTemperature(seconds);
    }

    public List<Machine> findMachineByDiffValueTemperature(Long machineId,int second){
        return jdbcMachineRepository.findCriticalDiffValueTemperatureByMachineId(machineId,second);
    }
}
