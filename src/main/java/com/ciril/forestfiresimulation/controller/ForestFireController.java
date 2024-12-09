package com.ciril.forestfiresimulation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ciril.forestfiresimulation.model.Forest;
import com.ciril.forestfiresimulation.service.FireSimulationService;

@RestController
@RequestMapping("/api/simulation")
public class ForestFireController {

    @Autowired
    private FireSimulationService fireSimulationService;

    @GetMapping("/simulate")
    public List<Forest> simulateFire() {
        return fireSimulationService.runSimulation();
    }
}
