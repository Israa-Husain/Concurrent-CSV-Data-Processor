package com.ga.model;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class Employee {
    private final int id;
    private final String name;
    private final double salary;
    private final LocalDate joinedDate;
    private final EmployeeRole role;
    private final double projectCompletion;

    public Employee(int id, String name, double salary, LocalDate joinedDate, EmployeeRole role, double projectCompletion) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.joinedDate = joinedDate;
        this.role = role;
        this.projectCompletion = projectCompletion;
    }
}
