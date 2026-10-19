package com.ga.concurrency;

import com.ga.dto.SalaryDetails;
import com.ga.model.Employee;
import com.ga.service.SalaryCalculation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class SalaryProcessor {
    private final SalaryCalculation calculation;
    private final int maxConcurrentTasks;

    public SalaryProcessor(SalaryCalculation calculation,@Value("${processor.max-active:3}") int maxConcurrentTasks){
        this.calculation=calculation;
        this.maxConcurrentTasks=maxConcurrentTasks;
    }

    public List<SalaryDetails> process(List<Employee> employees,LocalDate calcDate,AtomicInteger count) throws InterruptedException{

        ExecutorService executor=Executors.newFixedThreadPool(3);

        List<SalaryDetails> results=new ArrayList<>();
        ReentrantLock lock=new ReentrantLock();
        Semaphore semaphore=new Semaphore(maxConcurrentTasks);
        List<Future<?>> futures=new ArrayList<>();

        try{
            for(Employee employee:employees){
                futures.add(executor.submit(()->{
                    boolean acquired=false;

                    try{
                        semaphore.acquire();
                        acquired=true;

                        SalaryDetails result=calculation.calculate(employee,calcDate);

                        lock.lock();
                        try{
                            results.add(result);
                            count.incrementAndGet();
                        }finally{
                            lock.unlock();
                        }

                    }catch(InterruptedException e){
                        Thread.currentThread().interrupt();
                        throw new IllegalStateException("Worker interrupted",e);

                    }finally{
                        if(acquired){
                            semaphore.release();
                        }
                    }
                }));
            }
        }finally{
            executor.shutdown();
        }

        try{
            for(Future<?> future:futures){
                future.get();
            }
        }catch(ExecutionException e){
            for(Future<?> future:futures){
                future.cancel(true);
            }
            throw new IllegalArgumentException("Employee processing failed: "+e.getCause().getMessage(),e);

        }catch(InterruptedException e){
            for(Future<?> future:futures){
                future.cancel(true);
            }
            Thread.currentThread().interrupt();
            throw e;
        }

        for(int i=0; i<results.size(); i++){
            for(int j=i+1; j<results.size(); j++){
                if(results.get(i).getId()>results.get(j).getId()){
                    SalaryDetails temp=results.get(i);
                    results.set(i,results.get(j));
                    results.set(j,temp);
                }
            }
        }
        return results;
    }
}