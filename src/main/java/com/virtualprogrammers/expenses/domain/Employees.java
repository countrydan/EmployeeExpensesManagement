package com.virtualprogrammers.expenses.domain;

import com.virtualprogrammers.expenses.exceptions.EmployeeNotExists;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public interface Employees {
    ArrayList<Employee> getListOfEmployees();

    void addEmployee(Employee employee);

    void printEmployees();

    Employee findBySurname(String surname) throws EmployeeNotExists;

    Employee findById(int id) throws EmployeeNotExists;

    boolean employeeExists(int employeeId);

    void addExpenseClaimToEmployee(ExpenseClaim expenseClaim) throws NoSuchElementException;
}
