package com.virtualprogrammers.expenses.domain;

import com.virtualprogrammers.expenses.exceptions.EmployeeNotExists;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Function;

public class EmployeesDatabaseImpl implements Employees {
    public EmployeesDatabaseImpl() throws ClassNotFoundException {
        Class.forName("org.h2.Driver");
    }

    @Override
    public ArrayList<Employee> getListOfEmployees() {
        ArrayList<Employee> employeesList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:h2:./expenses", "sa", "");) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM employees");
            while (rs.next()) {
                employeesList.add(createEmployeeFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("There was a problem connecting to the database");
            throw new RuntimeException(e);
        }
        return employeesList;
    }

    private Employee createEmployeeFromResultSet(ResultSet rs) throws SQLException {
        Employee employee = new Employee(rs.getInt("id"), rs.getString("title"), rs.getString("firstName"), rs.getString("surname"), rs.getString("jobTitle"), Department.valueOf(rs.getString("department").toUpperCase()));
        ArrayList<ExpenseClaim> expenseClaims = getEmployeeExpenseClaims(employee.getId());
        expenseClaims.forEach( expenseClaim -> employee.addClaim(expenseClaim) );
        return employee;
    }

    @Override
    public void addEmployee(Employee employee) {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:./expenses", "sa", "");) {
            Statement statement = connection.createStatement();
            String sql = """
                    INSERT INTO employees
                     (id, title, firstName,
                     surname,  jobTitle, department)
                     VALUES (%s, '%s', '%s', '%s', '%s', '%s')
                    """.formatted(employee.getId(), employee.getTitle(), employee.getFirstName(), employee.getSurname(), employee.getJobTitle(), employee.getDepartment());
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("There was a problem connecting to the database");
            throw new RuntimeException(e);
        }
    }

    private ArrayList<ExpenseClaim> getEmployeeExpenseClaims(int employeeId) {
        ArrayList<ExpenseClaim> expenseClaims = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:h2:./expenses", "sa", "");) {
            Statement statement = connection.createStatement();
            ResultSet expenseClaimsRs = statement.executeQuery("SELECT * FROM expenseclaims WHERE employeeId = %s".formatted(employeeId));
            while (expenseClaimsRs.next()) {
                ExpenseClaim expenseClaim = new ExpenseClaim(
                        expenseClaimsRs.getInt("id"),
                        expenseClaimsRs.getInt("employeeId"),
                        LocalDate.parse(expenseClaimsRs.getString("dateOfClaim"))
                );
                expenseClaim.setApproved(expenseClaimsRs.getBoolean("approved"));
                expenseClaim.setPaid(expenseClaimsRs.getBoolean("paid"));
                Statement statementExpenseItems = connection.createStatement();
                ResultSet expenseItemsRs = statementExpenseItems.executeQuery("SELECT * FROM expenseitems WHERE claimId = %s".formatted(expenseClaimsRs.getInt("id")));
                while (expenseItemsRs.next()) {
                    expenseClaim.addExpenseItem(
                            new ExpenseItem(
                                    expenseItemsRs.getInt("id"),
                                    expenseItemsRs.getInt("claimId"),
                                    ExpenseType.valueOf(expenseItemsRs.getString("expenseType")),
                                    expenseItemsRs.getString("description"),
                                    expenseItemsRs.getDouble("amount")
                            )
                    );
                }
                expenseClaims.add(expenseClaim);
            }
        } catch (SQLException e) {
            System.out.println("There was a problem connecting to the database");
            throw new RuntimeException(e);
        }
        return expenseClaims;
    }

    @Override
    public void printEmployees() {
        ArrayList<Employee> employees = getListOfEmployees();
        Collections.sort(employees);
        for (Employee e : employees) {
            System.out.println(e);
            for (ExpenseClaim claim : e.getClaims().values()) {
                System.out.println(claim);
                claim.printExpenseItems();
                System.out.println("Total value of claims: " + claim.getTotalAmount());
            }
        }
    }

//    private Employee findEmployeeBy(BiFunction function) throws EmployeeNotExists {
//        try (Connection connection = DriverManager.getConnection("jdbc:h2:./expenses", "sa", "");) {
//            Statement statement = connection.createStatement();
//            ResultSet rs = function.apply(statement, );
//            if (rs.next()) {
//                return createEmployeeFromResultSet(rs);
//            } else {
//                throw new EmployeeNotExists();
//            }
//        } catch (SQLException e) {
//            System.out.println("There was a problem connecting to the database");
//            throw new RuntimeException(e);
//        }
//    }
//
//    private ResultSet getResultSet(Statement statement, String columnName, String value) throws SQLException {
//        return statement.executeQuery("SELECT * from employees WHERE %s = '%s'".formatted(columnName, value));
//    }
//    private ResultSet getResultSet(Statement statement, String columnName, int value) throws SQLException {
//        return statement.executeQuery("SELECT * from employees WHERE %s = %s".formatted(columnName, value));
//    }

    @Override
    public Employee findBySurname(String surname) throws EmployeeNotExists {

        try (Connection connection = DriverManager.getConnection("jdbc:h2:./expenses", "sa", "");) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * from employees WHERE surname = '%s'".formatted(surname));
            if (rs.next()) {
                return createEmployeeFromResultSet(rs);
            } else {
                throw new EmployeeNotExists();
            }
        } catch (SQLException e) {
            System.out.println("There was a problem connecting to the database");
            throw new RuntimeException(e);
        }
    }

    @Override
    public Employee findById(int id) throws EmployeeNotExists {
//        BiFunction<Statement, Integer, ResultSet> rs = (Statement statement, Integer value) -> {
//            return statement.executeQuery("SELECT * from employees WHERE id = %s".formatted(value));
//        };
//        Employee employee = findEmployeeBy(rs);
        try (Connection connection = DriverManager.getConnection("jdbc:h2:./expenses", "sa", "");) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * from employees WHERE id = %s".formatted(id));
            if (rs.next()) {
                return createEmployeeFromResultSet(rs);
            } else {
                throw new EmployeeNotExists();
            }
        } catch (SQLException e) {
            System.out.println("There was a problem connecting to the database");
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean employeeExists(int employeeId) {
        try {
            findById(employeeId);
        } catch (EmployeeNotExists e) {
            return false;
        }
        return true;
    }

    @Override
    public void addExpenseClaimToEmployee(ExpenseClaim expenseClaim) throws NoSuchElementException {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:./expenses", "sa", "");) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO expenseclaims (id, employeeId, dateOfClaim, approved, paid) VALUES (%s, %s, '%s', %s, %s)"
                    .formatted(expenseClaim.getId(), expenseClaim.getEmployeeId(), expenseClaim.getDateOfClaim(), expenseClaim.isApproved(), expenseClaim.isPaid())
            );
            for (ExpenseItem item : expenseClaim.getExpenseItems()) {
                statement.executeUpdate("INSERT INTO expenseitems (id, claimId, expenseType, description, amount) VALUES (%s, %s, '%s', '%s', %s)"
                        .formatted(item.getId(), item.getClaimId(), item.getExpenseType(), item.getDescription(), item.getAmount())
                );
            }
        } catch (SQLException e) {
            System.out.println("There was a problem connecting to the database");
            throw new RuntimeException(e);
        }
    }
}
