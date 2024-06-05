package com.virtualprogrammers.expenses.process;

import com.virtualprogrammers.expenses.domain.Employee;
import com.virtualprogrammers.expenses.domain.ExpenseClaim;

public class ExpressExpenseManagementProcess implements ExpenseManagementProcess {

    private ExpenseClaim expenseClaim;

    @Override
    public int registerExpenseClaim(ExpenseClaim claim) {
        this.expenseClaim = claim;
        return -1;
    }

    @Override
    public boolean approveClaim(int id, Employee approver) {
        return expenseClaim.getTotalAmount() < 50;
    }
}
