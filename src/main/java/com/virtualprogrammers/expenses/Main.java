package com.virtualprogrammers.expenses;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtualprogrammers.expenses.domain.Department;
import com.virtualprogrammers.expenses.domain.Employee;
import com.virtualprogrammers.expenses.domain.EmployeesInMemoryImpl;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws JsonProcessingException, ClassNotFoundException, SQLException {
        Employee employee1 = new Employee();
        employee1.setId(1);
        employee1.setTitle("Mr");
        employee1.setFirstName("Daniel");
        employee1.setSurname("Orszag");

        System.out.println(employee1.getMailingName());

        System.out.println(employee1.getMailingName(true));

        System.out.println(employee1.getMailingName(false));
        Employee employee2 = new Employee(2, "Manager");
        employee2.setTitle("Dr");
        employee2.setFirstName("ASD");
        employee2.setSurname("fd");
        EmployeesInMemoryImpl employees = new EmployeesInMemoryImpl();
        employees.addEmployee(employee1);
        employees.addEmployee(employee2);
        employees.addEmployee(new Employee(3, "Mrs", "Susan", "Brown", "Director", Department.MARKETING));

        employees.printEmployees();

        Employee notFound = employees.findBySurname("Asfdgdfgh");
        if (notFound == null) System.out.println("not found");
        Employee found = employees.findBySurname("Orszag");
        if (found != null) System.out.println("found");

        // Department financeDept = new Department("Finance", "Sally Green");

        // AlternativeDepartment personnelDept = new AlternativeDepartment("Personnel", "Bill Purple");
        System.out.println(employee1);

        Employee employee3 = new Employee();
        employee3.setId(1);
        employee3.setTitle("Mr");
        employee3.setFirstName("Daniel");
        employee3.setSurname("Orszag");

        System.out.println(employee1.equals(employee2));
        System.out.println(employee1.getClass());

        System.out.println(employee1);

        ObjectMapper objectMapper = new ObjectMapper();
        String employee1Json = objectMapper.writeValueAsString(employee1);
        System.out.println(employee1Json);

        Employee employeeFromJson = objectMapper.readValue(employee1Json, Employee.class);
        System.out.println(employeeFromJson);

        Class.forName("org.h2.Driver");
        try (Connection connection = DriverManager.getConnection("jdbc:h2:./customerdata", "sa", "");
        ) {
            Statement statement = connection.createStatement();
            // statement.executeUpdate("CREATE TABLE customer (id INTEGER, name VARCHAR(255), age INTEGER, PRIMARY KEY (id))");
            // statement.executeUpdate("INSERT INTO customer (id, name, age) VALUES (2, 'Massy', 22)");

            ResultSet rs = statement.executeQuery("SELECT * FROM customer");

            while (rs.next()) {
                System.out.println(rs.getInt("id"));
                System.out.println(rs.getString("name"));
            }
        }


    }
}
