package se.vgcs.congestion_calculator.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import se.vgcs.congestion_calculator.util.CongestionTaxCalculator;

import java.util.Date;

// The idea is that the URL would look something like
//http://localhost:8080/congestion/tax/calculate?vehicleType=car&dates=2023-07-10T08:00:00Z&dates=2023-07-11T09:00:00Z&dates=2023-07-12T10:00:00Z&dates=2023-07-13T11:00:00Z
@Log4j2
@RestController
@RequestMapping("/congestion/tax")
public class CalculationController {

    @GetMapping(value = "/calculate")
    @ResponseBody
    //todo given more time would have nicer proper ResponseEntity and handle potential error codes
    public int calculateVehicleTax(@RequestParam String vehicleType, @RequestParam Date... dates) {
        return CongestionTaxCalculator.getTax(vehicleType, dates);
    }

}
