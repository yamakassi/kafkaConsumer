package com.example.consumer.kafka;


import com.example.consumer.model.Machine;
import org.springframework.kafka.support.serializer.JsonDeserializer;

public class MachineDeserializer extends JsonDeserializer<Machine> {

    public MachineDeserializer() {
        super(Machine.class);
        
    }

}
