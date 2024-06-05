package com.virtualprogrammers.expenses;

import com.virtualprogrammers.expenses.domain.*;
import com.virtualprogrammers.expenses.exceptions.EmployeeNotExists;
import com.virtualprogrammers.expenses.process.ExpenseManagementProcess;
import com.virtualprogrammers.expenses.process.ExpressExpenseManagementProcess;
import com.virtualprogrammers.expenses.process.RegularExpenseManagementProcess;
import com.virtualprogrammers.expenses.ui.UIFunctions;
import com.virtualprogrammers.expenses.utilities.ExpenseAnalysis;
import com.virtualprogrammers.expenses.utilities.ExpenseAnalysisTempImpl;
import com.virtualprogrammers.expenses.utilities.ExpenseAnalysisWithLambdas;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ExpenseManagementSystem {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        Employees employees = new EmployeesDatabaseImpl();
        RegularExpenseManagementProcess regularExpenseManagementProcess = new RegularExpenseManagementProcess();
        ExpressExpenseManagementProcess expressExpenseManagementProcess = new ExpressExpenseManagementProcess();
        ExpenseAnalysis expenseAnalysis = new ExpenseAnalysisTempImpl();
        ExpenseAnalysis expenseAnalysisWithLambdas = new ExpenseAnalysisWithLambdas(employees, true);

        boolean readyToExit = false;
        while (!readyToExit) {
            System.out.println("""
                    x  -> Register new employee
                    c  -> Register new expense claim
                    e  -> Expense approval
                    r1 -> outstanding expense claims
                    r2 -> paid expense claims
                    r3 -> expense claims above specified amount
                    p  -> Print all employees
                    q  -> Exit""");
            String command = scanner.nextLine();
            switch (command) {
                case "x" -> {
                    Employee employee = UIFunctions.registerNewEmployee();
                    employees.addEmployee(employee);
                }
                case "c" -> {
                    ExpenseClaim expenseClaim = UIFunctions.registerNewExpenseClaim();
                    try {
                        employees.addExpenseClaimToEmployee(expenseClaim);
                    } catch (NoSuchElementException e) {
                        System.out.println("Employee with id " + expenseClaim.getEmployeeId() + " does not exist.");
                    }
                    expressExpenseManagementProcess.registerExpenseClaim(expenseClaim);
                    System.out.println("Expense claim ID: " + regularExpenseManagementProcess.registerExpenseClaim(expenseClaim));
                }
                case "p" -> employees.printEmployees();
                case "e" -> {
                    System.out.println("Enter claim ID");
                    int claimId = scanner.nextInt();
                    scanner.nextLine();
                    boolean validApprover = false;
                    Employee approver = null;
                    while (!validApprover) {
                        System.out.println("Enter ID of approver");
                        int approverId = scanner.nextInt();
                        scanner.nextLine();
                        try {
                            approver = employees.findById(approverId);
                            validApprover = true;
                        } catch (EmployeeNotExists e) {
                            System.out.println("Employee with ID " + approverId + " does not exist.");
                        }
                    }
                    System.out.println("Enter r for regular, or e for express");
                    String claimType = scanner.nextLine();
                    ExpenseManagementProcess requestedProcess;
                    if (claimType.equals("r")) {
                        requestedProcess = regularExpenseManagementProcess;

                    } else {
                        requestedProcess = expressExpenseManagementProcess;
                    }
                    boolean isApproved = requestedProcess.approveClaim(claimId, approver);

                    if (isApproved) System.out.printf("Claim with ID %s is approved%n", claimId);
                    else System.out.printf("Claim with ID %s is not approved%n", claimId);
                }
                case "r1" -> expenseAnalysisWithLambdas.printOutstandingClaims();
                case "r2" -> {
                    System.out.println("Enter date from");
                    String dateFrom = scanner.nextLine();
                    System.out.println("Enter date to");
                    String dateTo = scanner.nextLine();
                    expenseAnalysis.printPaidClaims(LocalDate.parse(dateFrom), LocalDate.parse(dateTo));
                }
                case "r3" -> {
                    System.out.println("Enter amount");
                    String amount = scanner.nextLine();
                    expenseAnalysis.printClaimAbove(Double.parseDouble(amount));
                }
                case "q" -> readyToExit = true;
                default -> System.out.println("Invalid command");
            }
        }
    }
}
