package com.example.consumer.repository;

import com.example.consumer.Consumer;
import com.example.consumer.model.Sensor;
import com.example.consumer.model.SensorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcSensorRepository implements SensorRepository {
    private JdbcTemplate jdbcTemplate;
    private final Logger logger = LoggerFactory.getLogger(JdbcSensorRepository.class);

    public JdbcSensorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Iterable<Sensor> findAll() {
        return jdbcTemplate.query("select id, machine_id, sensor_type,value, date_value from Sensor", this::mapRowToSensor);
    }


    @Override
    public Optional<Sensor> findById(String id) {
        List<Sensor> results = jdbcTemplate.query("select id, machine_id, sensor_type,value, date_value from Sensor where id=?", this::mapRowToSensor, id);
        return results.size() == 0 ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public Sensor save(Sensor sensor) {

        logger.info(sensor.toString());
        jdbcTemplate.update("insert into Sensor (machine_id, sensor_type, value, date_value) values (?, ?, ?,?)",

                sensor.getMachineId(), sensor.getSensorType().toString(), sensor.getValue(), sensor.getDateValue());
        return sensor;
    }


    private Sensor mapRowToSensor(ResultSet row, int rowNum) throws SQLException {
        return new Sensor(row.getLong("id"), row.getLong("machine_id"), SensorType.valueOf(row.getString("sensor_type")), row.getDouble("value"), row.getTimestamp("date_value"));
    }

}
