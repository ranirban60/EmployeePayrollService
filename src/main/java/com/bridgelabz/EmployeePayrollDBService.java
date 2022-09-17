package com.bridgelabz;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDBService {
    private PreparedStatement employeePayrollDataStatement;
    private  static EmployeePayrollDBService employeePayrollDBService;

    private EmployeePayrollDBService(){

    }

    public static  EmployeePayrollDBService getInstance(){
        if (employeePayrollDBService == null )
            employeePayrollDBService = new EmployeePayrollDBService();
        return employeePayrollDBService;
    }
    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
        String userName = "root";
        String password = "sreehari";
        Connection con;
        System.out.println("Connecting to database " +jdbcURL);
        con = DriverManager.getConnection(jdbcURL,userName,password);
        System.out.println("Connection is successful !!!!! " +con);
        return con;
    }
    public List<EmployeePayrollData> readData() {
        String sql = "SELECT * FROM employee_payroll;";
        return this.getEmployeePayrollDataUsingDB(sql);
    }

//
//    public List<EmployeePayrollData> getEmployeePayrollForDataRange(LocalDate startDate, LocalDate endDate) {
//        String sql = String.format("SELECT * FROM employee_payroll WHERE START BETWEEN '%s' AND '%s'; ",
//                Date.valueOf(startDate), Date.valueOf(endDate));
//        return this.getEmployeePayrollDataUsingDB(sql);
//    }
    private List<EmployeePayrollData> getEmployeePayrollDataUsingDB(String sql) {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try {
            Connection connection = this.getConnection();
            Statement statement =  connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) {
        List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
        try{
            while (resultSet.next()) {
                int  id = resultSet.getInt(1);
                String  name = resultSet.getString(2);
                double  salary = resultSet.getDouble(3);
                LocalDate startDate = resultSet.getDate(4).toLocalDate();
                employeePayrollList.add(new EmployeePayrollData(id, name, salary,startDate));
            }
        }
     catch (SQLException e) {
        e.printStackTrace();
    }
        return  employeePayrollList;
    }
    public List<EmployeePayrollData> getEmployeePayrollData(String name){
        List<EmployeePayrollData> employeePayrollList = null;
        if(this.employeePayrollDataStatement == null )
            this.prepareStatementForEmployeeData();
        try{
            employeePayrollDataStatement.setString(1,name);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            employeePayrollList = this.getEmployeePayrollData(resultSet);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return employeePayrollList;
    }

    public int updateEmployeeData(String name, double salary) {
        return this.updateEmployeeDataUsingStatement(name, salary);
    }


    private void prepareStatementForEmployeeData() {
       try{
           Connection connection = this.getConnection();
           String sql = "SELECT * FROM employee_payroll WHERE name = ?";
           employeePayrollDataStatement = connection.prepareStatement(sql);
       }catch(SQLException e){
           e.printStackTrace();
       }

    }

    private int updateEmployeeDataUsingStatement(String name, double salary) {
        String sql = String.format("update employee_payroll set salary = %.2f where name = '%s';", salary, name);
       try {
           Connection connection = this.getConnection();
           Statement statement = connection.createStatement();
           return statement.executeUpdate(sql);
       }catch (SQLException e){
           e.printStackTrace();
        }
       return 0;
    }


}
