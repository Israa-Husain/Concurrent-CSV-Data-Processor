package com.ga.csv;

import com.ga.model.Employee;
import com.ga.model.EmployeeRole;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {
    public List<Employee> read(Path path) throws IOException {
        List<Employee> employees = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            int lineNumber=0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if(line.trim().isEmpty()){
                    continue;
                }

                String[] data = line.split(",",-1);
                if(lineNumber==1 && data[0].trim().equalsIgnoreCase("id")){
                    continue;
                }

                try {
                    if(data.length != 6){
                        throw new IllegalArgumentException("Expected 6 columns");
                    }

                    int id = Integer.parseInt(data[0].trim());
                    String name = data[1].trim();
                    double salary = Double.parseDouble(data[2].trim());
                    LocalDate joinedDate = LocalDate.parse(data[3].trim());
                    EmployeeRole role = EmployeeRole.valueOf(data[4].trim());
                    double projectCompletion = Double.parseDouble(data[5].trim());

                    if(id<=0 || name.isEmpty() || !Double.isFinite(salary) || salary<0 || !Double.isFinite(projectCompletion) || projectCompletion<0 || projectCompletion>1){
                        throw new IllegalArgumentException("Invalid values");
                    }

                    employees.add(new Employee(id,name,salary,joinedDate,role,projectCompletion));
                } catch (RuntimeException e){
                    throw new IllegalArgumentException("Invalid csv row "+lineNumber+": "+e.getMessage(),e);
                }
            }
        }
        return employees;
    }
}
