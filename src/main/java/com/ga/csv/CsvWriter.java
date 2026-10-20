package com.ga.csv;

import com.ga.dto.SalaryDetails;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
@Component
public class CsvWriter {
    public void write(Path path, List<SalaryDetails> records) throws IOException{
        Files.createDirectories(path.toAbsolutePath().getParent());
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()))){
            writer.write("id,name,originalSalary,joinedDate,role,projectCompletion,increasePercentage,increaseAmount,newSalary");
            writer.newLine();
            for(SalaryDetails record:records){
                writer.write(record.getId()+","+record.getName()+","+record.getOriginalSalary()+","+record.getJoinedDate()+","+record.getRole()+","+record.getProjectCompletion()+","+record.getIncreasePercentage()+","+record.getIncreaseAmount()+","+record.getNewSalary());
                writer.newLine();
            }
        }
    }
}
