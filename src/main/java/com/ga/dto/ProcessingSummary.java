package com.ga.dto;

import java.time.LocalDate;
import java.util.List;

public class ProcessingSummary {
    private final int totalEmployees;
    private final int processedEmployees;
    private final int employeesWithRaise;
    private final int employeesWithoutRaise;
    private final LocalDate calcDate;
    private final double totalOldSalary;
    private final double totalNewSalary;
    private final String outFile;
    private final List<SalaryDetails> results;
    private final long processingTime;

    public ProcessingSummary(int totalEmployees, int processedEmployees, int employeesWithRaise, int employeesWithoutRaise, LocalDate calcDate, double totalOldSalary, double totalNewSalary, String outFile, List<SalaryDetails> results, long processingTime) {
        this.totalEmployees = totalEmployees;
        this.processedEmployees = processedEmployees;
        this.employeesWithRaise = employeesWithRaise;
        this.employeesWithoutRaise = employeesWithoutRaise;
        this.calcDate = calcDate;
        this.totalOldSalary = totalOldSalary;
        this.totalNewSalary = totalNewSalary;
        this.outFile = outFile;
        this.results = results;
        this.processingTime = processingTime;
    }

    public int getTotalEmployees() {
        return totalEmployees;
    }

    public int getProcessedEmployees() {
        return processedEmployees;
    }

    public int getEmployeesWithRaise() {
        return employeesWithRaise;
    }

    public int getEmployeesWithoutRaise() {
        return employeesWithoutRaise;
    }

    public LocalDate getCalcDate() {
        return calcDate;
    }

    public double getTotalOldSalary() {
        return totalOldSalary;
    }

    public double getTotalNewSalary() {
        return totalNewSalary;
    }

    public String getOutFile() {
        return outFile;
    }

    public List<SalaryDetails> getResults() {
        return results;
    }

    public long getProcessingTime() {
        return processingTime;
    }
}
