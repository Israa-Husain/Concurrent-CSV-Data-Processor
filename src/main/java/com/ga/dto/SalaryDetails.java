package com.ga.dto;

import com.ga.model.EmployeeRole;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SalaryDetails {
    private final int id;
    private final String name;
    private final double originalSalary;
    private final double increasePercentage;
    private final double increaseAmount;
    private final double newSalary;
    private final LocalDate joinedDate;
    private final EmployeeRole role;
    private final double projectCompletion;

    public SalaryDetails(int id, String name, double originalSalary, double increasePercentage, double increaseAmount, double newSalary, LocalDate joinedDate, EmployeeRole role, double projectCompletion) {
        this.id = id;
        this.name = name;
        this.originalSalary = originalSalary;
        this.increasePercentage = increasePercentage;
        this.increaseAmount = increaseAmount;
        this.newSalary = newSalary;
        this.joinedDate = joinedDate;
        this.role = role;
        this.projectCompletion = projectCompletion;
    }
}
