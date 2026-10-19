package com.ga.concurrency;

import com.ga.dto.SalaryDetails;
import com.ga.model.Employee;
import com.ga.service.SalaryCalculation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class SalaryProcessor {
    private final ExecutorService executor;
    private final SalaryCalculation calculation;
    private final int maxConcurrentTasks;

    public SalaryProcessor(ExecutorService executor, SalaryCalculation calculation, int maxConcurrentTasks) {
        this.executor = executor;
        this.calculation = calculation;
        this.maxConcurrentTasks = maxConcurrentTasks;
    }

    public List<SalaryDetails> process(List<Employee> employees, LocalDate calcDate, AtomicInteger count) throws InterruptedException {
        List<SalaryDetails> results = new ArrayList<>();
        ReentrantLock lock = new ReentrantLock();
        Semaphore semaphore = new Semaphore(maxConcurrentTasks);
        List<Future<?>> futures = new ArrayList<>();

        for(Employee employee:employees){
            futures.add(executor.submit(()->{
                boolean acquired = false;

                try {
                    semaphore.acquire();
                    acquired = true;

                    SalaryDetails result = calculation.calculate(employee,calcDate);
                    lock.lock();

                    try {
                        results.add(result);
                        count.incrementAndGet();
                    } finally {
                        lock.unlock();
                    }
                } catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("Worker interrupted",e);
                } finally {
                    if(acquired){
                        semaphore.release();
                    }
                }
            }));
        }
        try {
            for(Future<?> future:futures){
                future.get();
            }
        } catch (ExecutionException e){
            for(Future<?> future:futures){
                future.cancel(true);
            }
            throw new IllegalArgumentException("Employee processing failed: "+e.getCause().getMessage(),e);
        } catch (InterruptedException e){
            for(Future<?> future:futures){
                future.cancel(true);
            }
            throw e;
        }


        for(int i=0; i<results.size(); i++){
            for(int j=i+1; j<results.size(); j++){
                if(results.get(i).getId()> results.get(j).getId()){
                    SalaryDetails temp = results.get(i);
                    results.set(i,results.get(j));
                    results.set(j,temp);
                }
            }
        }
        return results;
    }
}
