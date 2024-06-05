package com.virtualprogrammers.expenses.domain;

import java.util.Objects;

public class ExpenseItem {

    private int id;
    private int claimId;
    private ExpenseType expenseType;
    private String description;
    private double amount;

    public ExpenseItem(int id, int claimId, ExpenseType expenseType, String description, double amount) {
        this.id = id;
        this.claimId = claimId;
        this.expenseType = expenseType;
        this.description = description;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public int getClaimId() {
        return claimId;
    }

    public ExpenseType getExpenseType() {
        return expenseType;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpenseItem that = (ExpenseItem) o;
        return id == that.id && claimId == that.claimId && Double.compare(amount, that.amount) == 0 && Objects.equals(expenseType, that.expenseType) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, claimId, expenseType, description, amount);
    }

    @Override
    public String toString() {
        return "ExpenseItem{" +
                "id=" + id +
                ", claimId=" + claimId +
                ", expenseType='" + expenseType + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                '}';
    }
}
