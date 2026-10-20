package com.ga.controller;

import com.ga.dto.ProcessingSummary;
import com.ga.service.EmployeeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService){
        this.employeeService=employeeService;
    }

    @PostMapping("/process")
    public ProcessingSummary process(@RequestParam("calcDate") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate calcDate) throws Exception{
        return employeeService.process(calcDate);
    }

}