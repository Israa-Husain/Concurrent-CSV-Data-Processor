package com.ga.service;


import com.ga.dto.SalaryDetails;
import com.ga.model.Employee;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
@Service
public class SalaryCalculation {
    public SalaryDetails calculate(Employee employee, LocalDate calcDate){
        if(employee.getJoinedDate().isAfter(calcDate)){
            throw new IllegalArgumentException("Joining date can not be in the future");
        }

        int years = calcDate.getYear() - employee.getJoinedDate().getYear();
        LocalDate workAnniversaryDate = employee.getJoinedDate().plusYears(years);
        if(workAnniversaryDate.isAfter(calcDate)){
            years--;
        }

        double rate = 0;
        if(employee.getProjectCompletion()>=0.6){
            if(employee.getRole().name().equalsIgnoreCase("Director")){
                rate = 5;
            } else if (employee.getRole().name().equalsIgnoreCase("Manager")) {
                rate = 2;
            } else {
                rate = 1;
            }

            if(years>=1){
                rate+= (years*2);
            }

            if(employee.getProjectCompletion()>0.8){
                rate*=1.5;
            }
        }
        double oldSalary = employee.getSalary();
        double increase = oldSalary * rate /100;
        double newSalary = oldSalary+increase;
        return new SalaryDetails(employee.getId(),employee.getName(),oldSalary,rate,increase,newSalary,employee.getJoinedDate(),employee.getRole(),employee.getProjectCompletion());
    }
}
