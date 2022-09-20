package com.bridgelabz;

import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.bridgelabz.EmployeePayrollService.IOService.DB_IO;


public class EmployeePayrollServiceTest {

    @Test
    public void given3EmployeesWhenWrittenToFileShouldWatchEmployeeEntries(){
        EmployeePayrollData[] arrayOfEmps = new EmployeePayrollData[]{
                new EmployeePayrollData(1, "Devid", 100000.0),
                new EmployeePayrollData(2, "Prince", 200000.0),
                new EmployeePayrollData(3, "Peter", 300000.0)
        };
        EmployeePayrollService employeePayrollService;
        employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmps));
        employeePayrollService.writeEmployeePayrollData(EmployeePayrollService.IOService.FILE_IO);

        employeePayrollService.printData(EmployeePayrollService.IOService.FILE_IO);
        long entries = employeePayrollService.countEntries(EmployeePayrollService.IOService.FILE_IO);
        System.out.println(entries);
        Assert.assertEquals(3, entries);
    }

   // UC2
    @Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(DB_IO);
        Assert.assertEquals(3,employeePayrollData.size());
    }

    //UC3
    //UC4
    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB(){
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(DB_IO);
        employeePayrollService.updateEmployeeSalary("Terisa",3000000.00);
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
        Assert.assertTrue(result);
    }

    //UC5
    @Test
    public void givenDataRange_WhenRetrieved_ShouldMatchEmployeeCount(){
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(DB_IO);
        LocalDate startDate = LocalDate.of(2021, 8,30);
        LocalDate endDate = LocalDate.now();
        List<EmployeePayrollData> employeePayrollData =
                employeePayrollService.readEmployeePayrollForDateRange(DB_IO,startDate,endDate);
        Assert.assertEquals(3,employeePayrollData.size());
    }

    //UC6
    @Test
    public void givenPayrollData_WhenAverageSalaryRetrievedByGender_ShouldReturnProperValue(){
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(DB_IO);
        Map<String, Double>  averageSalaryByGender = employeePayrollService.readAverageSalaryByGender(DB_IO);
        Assert.assertTrue(averageSalaryByGender.get("M").equals(3000000.0) &&
                averageSalaryByGender.get("F").equals(3000000.0));
    }

    //UC7
    //UC8
    @Test
    public void givenNewEmployee_WhenAdded_ShouldSyncWithDB(){
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(DB_IO);
        employeePayrollService.addEmployeeToPayroll("Peter",7000000.00, LocalDate.now(),"M");
        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Peter");
        Assert.assertTrue(result);
    }


}