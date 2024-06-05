package com.virtualprogrammers.expenses.ui;

import com.virtualprogrammers.expenses.domain.*;
import com.virtualprogrammers.expenses.exceptions.InvalidEmployeeIdIdException;
import com.virtualprogrammers.expenses.exceptions.NameTooShortException;
import com.virtualprogrammers.expenses.utilities.EmployeeUtilities;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;

public class UIFunctions {

    public static Employee registerNewEmployee() {
        Scanner scanner = new Scanner(System.in);
        Employee employee = new Employee();

        boolean isIdValid = false;
        while (!isIdValid) {
            System.out.println("Enter the ID:");
            String employeeIdStr = scanner.nextLine();
            try {
                int employeeIdInt = EmployeeUtilities.validateEmployeeId(employeeIdStr);
                employee.setId(employeeIdInt);
                isIdValid = true;
            } catch (InvalidEmployeeIdIdException e) {
                System.out.println("Invalid ID number, please give a positive integer value.");
            }
        }


        System.out.println("Enter the title");
        String title = scanner.nextLine();
        employee.setTitle(title);

        System.out.println("Enter the job title");
        String jobTitle = scanner.nextLine();
        employee.setJobTitle(jobTitle);

        boolean nameIsValid = false;
        while (!nameIsValid) {
            System.out.println("Enter the first name:");
            String firstName = scanner.nextLine();

            System.out.println("Enter the surname:");
            String surname = scanner.nextLine();

            try {
                EmployeeUtilities.validateEmployeeName(firstName, surname);
                employee.setSurname(surname);
                employee.setFirstName(firstName);
                nameIsValid = true;
            } catch (NameTooShortException e) {
                System.out.println("Surname and first name combined cannot be less than 6 characters.");
            }
        }

        boolean departmentIsValid = false;
        while (!departmentIsValid) {
            System.out.println("Enter the department");
            String d = scanner.nextLine();
            try {
                Department department = Department.valueOf(d.toUpperCase());
                employee.setDepartment(department);
                departmentIsValid = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Unknown department, please enter a valid department: " + Arrays.toString(Department.values()));
            }
        }
        System.out.println("Are you a staff member? (y/n)");
        String isStaffEmployee = scanner.nextLine();
        if (isStaffEmployee.equals("n")) {
            return employee;
        }
        System.out.println("Enter username");
        String username = scanner.nextLine();
        System.out.println("Enter password");
        String password = scanner.nextLine();
        return new StaffEmployee(employee, username, password);

    }

    public static ExpenseClaim registerNewExpenseClaim() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter expense ID");
        String expenseId = scanner.nextLine();
        System.out.println("Enter employee ID");
        String employeeId = scanner.nextLine();

        ExpenseClaim expenseClaim = new ExpenseClaim(
                Integer.parseInt(expenseId),
                Integer.parseInt(employeeId),
                LocalDate.now());

        boolean addExpenseItemsDone = false;

        while (!addExpenseItemsDone) {
            System.out.println("Enter expense item ID");
            String expenseItemId = scanner.nextLine();
            boolean expenseTypeValid = false;
            ExpenseType expenseType = null;
            while (!expenseTypeValid) {
                System.out.println("Enter expense type");
                String expenseTypeString = scanner.nextLine();
                try {
                    expenseType = ExpenseType.valueOf(expenseTypeString.toUpperCase());
                    expenseTypeValid = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Unknown expense type, please enter a valid type: " + Arrays.toString(ExpenseType.values()));
                }
            }
            System.out.println("Enter the description");
            String expenseDescription = scanner.nextLine();
            System.out.println("Enter amount");
            String expenseAmount = scanner.nextLine();
            expenseClaim.addExpenseItem(new ExpenseItem(Integer.parseInt(expenseItemId), Integer.parseInt(expenseId), expenseType, expenseDescription, Double.parseDouble(expenseAmount)));
            System.out.println("Enter another expense item? (Y/N)");
            String anotherExpense = scanner.nextLine();
            if (anotherExpense.equalsIgnoreCase("n")) addExpenseItemsDone = true;
        }

        return expenseClaim;
    }

}
