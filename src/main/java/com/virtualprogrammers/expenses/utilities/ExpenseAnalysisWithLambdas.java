package com.virtualprogrammers.expenses.utilities;

import com.virtualprogrammers.expenses.domain.Employee;
import com.virtualprogrammers.expenses.domain.Employees;
import com.virtualprogrammers.expenses.domain.EmployeesInMemoryImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;

public class ExpenseAnalysisWithLambdas implements ExpenseAnalysis {

    private Employees employees;
    final private String homeFolder = System.getProperty("user.home");
    final private boolean writeReport;

    public ExpenseAnalysisWithLambdas(Employees employees, boolean writeReport) {
        this.employees = employees;
        this.writeReport = writeReport;
    }

    @Override
    public void printOutstandingClaims() {
        Path report = Paths.get(homeFolder + File.separator + "outstanding_claims.txt");
        employees.getListOfEmployees().forEach(
                employee -> {
                    employee.getClaims().values().stream()
                            .filter(expenseClaim -> !expenseClaim.isApproved())
                            .forEach(expenseClaim -> {
                                if (writeReport) {
                                    try {
                                        Files.writeString(report, expenseClaim + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                                    } catch (IOException e) {
                                        System.out.printf("Unable to write to file %s%n", report);
                                        ;
                                    }
                                }
                                System.out.println(expenseClaim);
                            });
                }
        );
    }

    @Override
    public void printPaidClaims(LocalDate from, LocalDate to) {
        employees.getListOfEmployees().forEach(
                employee -> {
                    employee.getClaims().values().stream()
                            .filter(expenseClaim -> expenseClaim.isPaid())
                            .filter(expenseClaim -> (expenseClaim.getDateOfClaim().isAfter(from) && expenseClaim.getDateOfClaim().isBefore(to)))
                            .forEach(expenseClaim -> System.out.println(expenseClaim));
                }
        );

    }

    @Override
    public void printClaimAbove(double above) {
        employees.getListOfEmployees().forEach(
                employee -> {
                    employee.getClaims().values().stream()
                            .filter(expenseClaim -> expenseClaim.getTotalAmount() >= above)
                            .forEach(expenseClaim -> System.out.println(expenseClaim));
                }
        );
    }
}
