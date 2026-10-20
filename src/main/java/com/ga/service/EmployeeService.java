package com.ga.service;

import com.ga.concurrency.SalaryProcessor;
import com.ga.csv.CsvReader;
import com.ga.csv.CsvWriter;
import com.ga.dto.ProcessingSummary;
import com.ga.dto.SalaryDetails;
import com.ga.model.Employee;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class EmployeeService {

    private final CsvReader csvReader;
    private final CsvWriter csvWriter;
    private final SalaryProcessor salaryProcessor;
    private final String inPath;
    private final String outPath;

    public EmployeeService(CsvReader csvReader,CsvWriter csvWriter,SalaryProcessor salaryProcessor, @Value("${csv.input-path}") String inPath, @Value("${csv.output-path}") String outPath){
        this.csvReader=csvReader;
        this.csvWriter=csvWriter;
        this.salaryProcessor=salaryProcessor;
        this.inPath=inPath;
        this.outPath=outPath;
    }

    public ProcessingSummary process(LocalDate calcDate) throws Exception{
        long startTime=System.currentTimeMillis();
        List<Employee> employees=csvReader.read(Path.of(inPath));
        AtomicInteger count=new AtomicInteger(0);
        List<SalaryDetails> results=salaryProcessor.process(employees,calcDate,count);
        csvWriter.write(Path.of(outPath),results);

        int employeesWithRaise=0;
        int employeesWithoutRaise=0;
        double totalOldSalary=0;
        double totalNewSalary=0;

        for(SalaryDetails result:results){
            totalOldSalary+=result.getOriginalSalary();
            totalNewSalary+=result.getNewSalary();

            if(result.getNewSalary()>result.getOriginalSalary()){
                employeesWithRaise++;
            }else{
                employeesWithoutRaise++;
            }
        }

        long processingTime=System.currentTimeMillis()-startTime;

        return new ProcessingSummary(
                employees.size(),
                count.get(),
                employeesWithRaise,
                employeesWithoutRaise,
                calcDate,
                totalOldSalary,
                totalNewSalary,
                outPath,
                results,
                processingTime
        );
    }
}