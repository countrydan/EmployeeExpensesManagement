package com.virtualprogrammers.expenses.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class ExpenseClaim {
    private int id;
    private int employeeId;
    private LocalDate dateOfClaim;
    private boolean approved;
    private boolean paid;
    private ArrayList<ExpenseItem> expenseItems;

    public ExpenseClaim(int id, int employeeId, LocalDate dateOfClaim) {
        this.id = id;
        this.employeeId = employeeId;
        this.dateOfClaim = dateOfClaim;
        this.approved = false;
        this.paid = false;
        this.expenseItems = new ArrayList<>();
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public void addExpenseItem(ExpenseItem expenseItem) {
        this.expenseItems.add(expenseItem);
    }

    public void setPaid(boolean paid) {
        if (paid && !this.approved) {
            System.out.println("This item cannot be paid as it has not yet been approved.");
        } else {
            this.paid = paid;
        }
    }

    public int getId() {
        return id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public LocalDate getDateOfClaim() {
        return dateOfClaim;
    }

    public double getTotalAmount() {
        double totalAmount = 0.0;
        for (ExpenseItem e : expenseItems) {
            totalAmount += e.getAmount();
        }
        return totalAmount;
    }

    public boolean isApproved() {
        return approved;
    }

    public boolean isPaid() {
        return paid;
    }

    public void printExpenseItems() {
        for (ExpenseItem e : expenseItems) {
            System.out.println(e);
        }
    }

    public ArrayList<ExpenseItem> getExpenseItems() {
        return expenseItems;
    }

    @Override
    public String toString() {
        return "ExpenseClaim{" +
                "id=" + id +
                ", employeeId=" + employeeId +
                ", dateOfClaim=" + dateOfClaim +
                ", approved=" + approved +
                ", paid=" + paid +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpenseClaim that = (ExpenseClaim) o;
        return id == that.id && employeeId == that.employeeId && approved == that.approved && paid == that.paid && Objects.equals(dateOfClaim, that.dateOfClaim) && Objects.equals(expenseItems, that.expenseItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, employeeId, dateOfClaim, approved, paid, expenseItems);
    }
}
