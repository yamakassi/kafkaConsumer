package com.example.consumer;

import com.example.consumer.model.Machine;
import com.example.consumer.service.StatisticService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

@RestController @RequestMapping("/statistic") @CrossOrigin
public class StatisticController {
    private final StatisticService statisticService;
    public StatisticController(StatisticService statisticService){
        this.statisticService=statisticService;

    }
  /*  @GetMapping("")
    public ResponseEntity<HashMap<Long, Machine>> lastData(){
       return new ResponseEntity<>(statisticService.getLastData(), HttpStatus.OK);
    }*/
    @GetMapping("/test")
    public  ResponseEntity<List<String>> test(){
        return new ResponseEntity<>(Arrays.asList("11","22"),HttpStatus.OK);
    }
    @GetMapping("")
    public  ResponseEntity<Set<Machine>> find(){
        return new ResponseEntity<>(statisticService.test(),HttpStatus.OK);
    }

      @GetMapping("/diff/{seconds}")
    public  ResponseEntity<List<Machine>> getValueSensorSeconds(@PathVariable("seconds")  int seconds){


        return new ResponseEntity<>(statisticService.findCriticalDiffValueTemperature(seconds),HttpStatus.OK);
    }
   @GetMapping("/diff/")
    public  ResponseEntity<List<Machine>> getValueSensorMachineIdAndSeconds(@RequestParam(value = "machineid")Long machineId,
                                                                            @RequestParam(value="seconds")Integer seconds){
        if(machineId==null|| seconds==null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(statisticService.findMachineByDiffValueTemperature(machineId,seconds),HttpStatus.OK);
    }




}
