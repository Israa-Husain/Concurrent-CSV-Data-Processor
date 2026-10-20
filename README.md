# Concurrent CSV Data Processor

## Project Overview

Concurrent CSV Data Processor is a Java Spring Boot application that reads employee records from a CSV file, calculates salary increases based on employee roles, years of service, and project completion, and generates a new CSV file containing the updated salaries.

## Project Features

- Read employee information from a CSV file.
- Validate employee data before processing.
- Calculate salary increases based on business rules.
- Process multiple employee records concurrently.
- Limit concurrent processing using a Semaphore.
- Protect shared results using a ReentrantLock.
- Count processed employees using AtomicInteger.
- Wait for submitted tasks to finish using Future.
- Sort salary results by employee ID.
- Generate an updated CSV file.
- Return a processing summary through a REST API.

## Salary Calculation Rules

Salary increases depend on: employee role, completed years of service, and project completion.

### 1. Project Completion

| Project Completion | Rule                                  |
|--------------------|---------------------------------------|
| Below 60%          | No salary increase                    |
| 60%–80%            | Normal calculated increase            |
| Above 80%          | Total increase rate multiplied by 1.5 |

### 2. Role-Based Increase

| Employee Role | Increase |
|---------------|----------|
| Director      | 5%       |
| Manager       | 2%       |
| Employee      | 1%       |

### 3. Years of Service

Employees receive an additional **2%** for each completed year of service.
Only completed years are counted. If an employee has not reached their next work anniversary, that year is not included.

### 4. Salary Calculation Formula

```
Base Increase Rate = Role Rate + (Completed Years × 2%)

If Project Completion > 80%:
    Final Increase Rate = Base Increase Rate × 1.5
Else:
    Final Increase Rate = Base Increase Rate

Salary Increase = Original Salary × Final Increase Rate / 100

New Salary = Original Salary + Salary Increase
```

Employees with project completion below 60% receive no salary increase regardless of role or years of service.

## Example Calculation

Employee information:

```
14,Nate,72000.0,2017-11-03,Manager,0.9
```

Using October 20, 2026, as the calculation date:

- Original salary: 72,000
- Role: Manager (2%)
- Completed years of service: 8 years
- Service increase: 8 × 2% = 16%
- Base increase rate: 2% + 16% = 18%
- Project completion: 90%, so the multiplier is 1.5
- Final increase rate: 18% × 1.5 = 27%
- Salary increase: 72,000 × 27% = 19,440
- **New salary: 91,440**

## Concurrency Implementation

The application uses the following Java concurrency concepts:

| Concept         | Implementation                                             |
|-----------------|------------------------------------------------------------|
| ExecutorService | Creates a fixed thread pool of 3 worker threads            |
| Semaphore       | Limits the number of tasks allowed to process concurrently |
| ReentrantLock   | Protects the shared salary results list                    |
| AtomicInteger   | Safely counts successfully processed employees             |
| Future          | Waits for employee processing tasks to complete            |

### How Concurrency Works

1. The application reads employee records from the input CSV.
2. `SalaryProcessor` creates a fixed thread pool.
3. Each employee is submitted as a separate task.
4. A Semaphore controls how many tasks can process simultaneously.
5. `SalaryCalculation` calculates the salary increase.
6. A ReentrantLock protects the shared results list when adding calculated salaries.
7. AtomicInteger updates the processed employee count.
8. The semaphore permit is released after processing.
9. Future ensures all submitted tasks finish.
10. Results are sorted by employee ID before being written to the output CSV.


## Classes

| Class                   | Responsibility                                                   |
|-------------------------|------------------------------------------------------------------|
| `ConcurrentApplication` | Starts the Spring Boot application                               |
| `EmployeeController`    | Receives API requests to process employee salaries               |
| `EmployeeService`       | Coordinates reading, processing, writing, and summary generation |
| `SalaryProcessor`       | Manages concurrent employee processing                           |
| `SalaryCalculation`     | Applies the salary calculation rules                             |
| `CsvReader`             | Reads and validates input CSV records                            |
| `CsvWriter`             | Writes calculated salary results to a new CSV file               |
| `Employee`              | Represents an employee's original data                           |
| `EmployeeRole`          | Defines the employee roles                                       |
| `SalaryDetails`         | Stores the calculated salary details for an employee             |
| `ProcessingSummary`     | Stores the overall processing results and statistics             |


## REST API

### Process Employee Salaries

**Method:** POST

```
POST http://localhost:8090/api/employees/process?calcDate=2026-10-20
```

**Parameter:**

| Parameter  | Type      | Description                                                      |
|------------|-----------|------------------------------------------------------------------|
| `calcDate` | LocalDate | Date used to calculate completed years of service (`yyyy-MM-dd`) |


A successful request returns HTTP *200 OK* with a processing summary containing information such as:

- Total employees
- Successfully processed employees
- Employees with salary increases
- Employees without salary increases
- Calculation date
- Total original salaries
- Total updated salaries
- Output file path
- Individual salary calculation results
- Processing time

## CSV Output

After successful processing, the application generates output file contains the calculated salary details for the employees.

```
src/main/java/com/ga/data/updated_salaries.csv
```