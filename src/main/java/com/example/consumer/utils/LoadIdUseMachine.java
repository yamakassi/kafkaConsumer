package com.example.consumer.utils;

import com.example.consumer.Consumer;
import com.example.consumer.model.LoadEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.stream.Collectors;
@Component
public class LoadIdUseMachine {
    private JdbcTemplate jdbcTemplate;
    private final Logger logger = LoggerFactory.getLogger(LoadIdUseMachine.class);

    public LoadIdUseMachine(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }
    @EventListener(ApplicationReadyEvent.class) @Bean
    public Set<Long> loadIdMachine() {

        return jdbcTemplate.queryForStream(
                "select id from machine",
                this::mapRowToLoadEntity).collect(Collectors.toSet());


    }

    private Long mapRowToLoadEntity(ResultSet row, int rowNum)
            throws SQLException {
        return row.getLong("id");

    }
}
