package com.virtualprogrammers.expenses.domain;

import java.util.*;

public class EmployeesInMemoryImpl implements Employees {

    private Map<Integer, Employee> employees = new HashMap<>();

    @Override
    public ArrayList<Employee> getListOfEmployees() {
        return new ArrayList<Employee>(employees.values());
    }

    @Override
    public void addEmployee(Employee employee) {
        employees.put(employee.getId(), employee);
    }

    @Override
    public void printEmployees() {
        ArrayList<Employee> employeesList = new ArrayList<>(employees.values());
        Collections.sort(employeesList);
        for (Employee e : employeesList) {
            System.out.println(e);
            for (ExpenseClaim claim : e.getClaims().values()) {
                System.out.println(claim);
                claim.printExpenseItems();
                System.out.println("Total value of claims: " + claim.getTotalAmount());
            }
        }
    }

    @Override
    public Employee findBySurname(String surname) {
        for (Employee e : employees.values()) {
            if (e.getSurname().equals(surname)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public Employee findById(int id) {
        return employees.get(id);
//        for (Integer employeeId : employees.keySet()) {
//            if (employeeId == id) {
//                return employees.get(id);
//            }
//        }
//        return null;
    }

    @Override
    public boolean employeeExists(int employeeId) {
        return employees.containsKey(employeeId);
        //        for (Employee employee : employees) {
//            if (employee.getId() == employeeId) {
//                return true;
//            }
//        }
//        return false;
    }

    @Override
    public void addExpenseClaimToEmployee(ExpenseClaim expenseClaim) throws NoSuchElementException {
        Employee employee = employees.get(expenseClaim.getEmployeeId());
        if (employee != null) {
            employee.getClaims().put(expenseClaim.getId(), expenseClaim);
        } else {
            throw new NoSuchElementException();
        }
//        for (Employee employee : employees) {
//            if (employee.getId() == expenseClaim.getEmployeeId()) {
//                employee.getClaims().add(expenseClaim);
//                return;
//            }
//        }
//        throw new NoSuchElementException();

    }
}

