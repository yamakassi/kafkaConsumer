package com.example.consumer.repository;

import com.example.consumer.model.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class JdbcMachineRepository implements MachineRepository {
    private JdbcTemplate jdbcTemplate;
    private SensorRepository sensorRepository;
    private final Set<Long> loadIdMachine;

    public JdbcMachineRepository(JdbcTemplate jdbcTemplate, SensorRepository sensorRepository, Set<Long> loadIdMachine) {
        this.jdbcTemplate = jdbcTemplate;
        this.sensorRepository = sensorRepository;
        this.loadIdMachine = loadIdMachine;
    }

    @Override
    public Set<Machine> findAll() {
        return jdbcTemplate.query("SELECT m.*, s.*,s.id as sensor_id FROM machine m LEFT JOIN sensor s ON m.id = s.machine_id  ORDER BY s.id DESC limit((select count(*) from machine )*4)",
                new RowMapper<Machine>() {
                    private Long currentMachineId;
                    private Machine machine;
                    @Override
                    public Machine mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Long machineId = rs.getLong("id");
                        if (currentMachineId == null || !currentMachineId.equals(machineId)) {
                            currentMachineId = machineId;
                            machine = new Machine();
                            machine.setId(machineId);
                            machine.setMachineName(rs.getString("machine_name"));
                            machine.setMachineStatus(StatusMachine.valueOf(rs.getString("machine_status")));
                            machine.setMachineServiceDate(rs.getDate("machine_service_date"));
                            machine.setSensors(new ArrayList<>());
                        }

                        Long sensorId = rs.getLong("sensor_id");
                        if (sensorId != null) {
                            Sensor sensor = new Sensor();
                            sensor.setId(sensorId);
                            sensor.setMachineId(machineId);
                            sensor.setSensorType(SensorType.valueOf(rs.getString("sensor_type")));
                            sensor.setValue(rs.getDouble("value"));
                            sensor.setDateValue(rs.getTimestamp("date_value"));
                            machine.getSensors().add(sensor);
                        }

                        return machine;
                    }
                }).stream().collect(Collectors.toSet());

    }


    @Override
    public List<Machine> findCriticalDiffValueTemperature(int second) {
      String sql = "SELECT m.*, s.*,s.id as sensor_id FROM machine m LEFT JOIN sensor s ON m.id = s.machine_id limit((select count(*) from machine )*4*2*?)";

        List<Machine> collect = jdbcTemplate.query((PreparedStatementCreator) connection -> {

            // Here you can get the connection object to create a PreparedStatement object to perform more complex SQL operations.
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,second);
            return ps;
        },
        new RowMapper<Machine>() {
                    private Long currentMachineId;
                    private Machine machine;

                    @Override
                    public Machine mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Long machineId = rs.getLong("id");
                        if (currentMachineId == null || !currentMachineId.equals(machineId)) {
                            currentMachineId = machineId;
                            machine = new Machine();
                            machine.setId(machineId);
                            machine.setMachineName(rs.getString("machine_name"));
                            machine.setMachineStatus(StatusMachine.valueOf(rs.getString("machine_status")));
                            machine.setMachineServiceDate(rs.getDate("machine_service_date"));
                            machine.setSensors(new ArrayList<>());
                        }

                        Long sensorId = rs.getLong("sensor_id");
                        if (sensorId != null) {
                            Sensor sensor = new Sensor();
                            sensor.setId(sensorId);
                            sensor.setMachineId(machineId);
                            sensor.setSensorType(SensorType.valueOf(rs.getString("sensor_type")));
                            sensor.setValue(rs.getDouble("value"));
                            sensor.setDateValue(rs.getTimestamp("date_value"));
                            machine.getSensors().add(sensor);
                        }

                        return machine;
                    }
                }).stream().toList();
        return collect;


    }
     @Override
    public List<Machine> findCriticalDiffValueTemperatureByMachineId(Long machineId, int second) {
      String sql = "SELECT m.*, s.*,s.id as sensor_id FROM machine m LEFT JOIN sensor s ON m.id = s.machine_id where m.id=? limit((select count(*) from machine )*4*2*?)";

        List<Machine> collect = jdbcTemplate.query((PreparedStatementCreator) connection -> {

            // Here you can get the connection object to create a PreparedStatement object to perform more complex SQL operations.
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1,machineId);
            ps.setInt(2,second);
            return ps;
        },
        new RowMapper<Machine>() {
                    private Long currentMachineId;
                    private Machine machine;

                    @Override
                    public Machine mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Long machineId = rs.getLong("id");
                        if (currentMachineId == null || !currentMachineId.equals(machineId)) {
                            currentMachineId = machineId;
                            machine = new Machine();
                            machine.setId(machineId);
                            machine.setMachineName(rs.getString("machine_name"));
                            machine.setMachineStatus(StatusMachine.valueOf(rs.getString("machine_status")));
                            machine.setMachineServiceDate(rs.getDate("machine_service_date"));
                            machine.setSensors(new ArrayList<>());
                        }

                        Long sensorId = rs.getLong("sensor_id");
                        if (sensorId != null) {
                            Sensor sensor = new Sensor();
                            sensor.setId(sensorId);
                            sensor.setMachineId(machineId);
                            sensor.setSensorType(SensorType.valueOf(rs.getString("sensor_type")));
                            sensor.setValue(rs.getDouble("value"));
                            sensor.setDateValue(rs.getTimestamp("date_value"));
                            machine.getSensors().add(sensor);
                        }

                        return machine;
                    }
                }).stream().toList();
        return collect;


    }




    @Override
    public Optional<Machine> findById(String id) {
        return Optional.empty();
    }

    @Override
    @Transactional
    public Machine save(Machine machine) {

        if(loadIdMachine.contains(machine.getId())){
            if(machine.getMachineStatus().equals(StatusMachine.work)){
                jdbcTemplate.update(
                        "update machine set machine_status =? where id = ?",
                        machine.getMachineStatus().toString(),machine.getId()
                );


            }
        }else {
            loadIdMachine.add(machine.getId());

            jdbcTemplate.update(
                    "INSERT INTO Machine (id, machine_name, machine_status,machine_service_date) VALUES (?, ?,?,?)",
                    machine.getId(), machine.getMachineName(), machine.getMachineStatus().name(), machine.getMachineServiceDate()
            );

        }




        List<Sensor> sensors = machine.getSensors();

        for (Sensor sensor : sensors) {
            sensor.setMachineId(machine.getId());
            sensorRepository.save(sensor);
        }

        return machine;
    }




}
