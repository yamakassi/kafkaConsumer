package com.example.consumer;

import com.example.consumer.model.Machine;
import com.example.consumer.repository.MachineRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@ConditionalOnProperty(value = "example.kafka.consumer-enabled", havingValue = "true")
public class Consumer {


    private final Logger logger = LoggerFactory.getLogger(Consumer.class);
    private final ObjectMapper objectMapper;
    private  final MachineRepository machineRepository;
    public Consumer( ObjectMapper objectMapper, MachineRepository machineRepository) {

        this.objectMapper = objectMapper;
        this.machineRepository = machineRepository;
    }

    @KafkaListener(topics = {"${config.topic.name}" })
    public void consume(final  String machineJson,
                        final @Header(KafkaHeaders.OFFSET) Integer offset,
                        final @Header(KafkaHeaders.RECEIVED_KEY) String key,
                        final @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                        final @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                        final @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts,
                        final Acknowledgment acknowledgment
    ) throws JsonProcessingException {

        TypeReference ref = new TypeReference<List<Machine>>() { };
        List<Machine> machinesList = (List<Machine>) objectMapper.readValue(machineJson,ref);
       logger.info("saved machine");
        logger.info(machinesList.toString());
        logger.info("json");
        logger.info(machineJson);

        for (Machine machine : machinesList) {
            machineRepository.save(machine);
        }


        logger.info(String.format("#### -> Consumed message -> TIMESTAMP: %d\n%s\noffset: %d\nkey: %s\npartition: %d\ntopic: %s", ts, machinesList, offset, key, partition, topic));
        acknowledgment.acknowledge();
    }




}
