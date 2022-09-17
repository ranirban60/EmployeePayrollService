package com.bridgelabz;

import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;

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
    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB(){
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(DB_IO);
        employeePayrollService.updateEmployeeSalary("Terisa",3000000.00);
        boolean result = employeePayrollService.checkEmployeePayrollINSyncWithDB("Terisa");
        Assert.assertTrue(result);
    }

}