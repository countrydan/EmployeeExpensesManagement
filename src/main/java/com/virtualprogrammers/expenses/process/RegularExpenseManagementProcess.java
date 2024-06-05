package com.virtualprogrammers.expenses.process;

import com.virtualprogrammers.expenses.ExpenseManagementSystem;
import com.virtualprogrammers.expenses.domain.Employee;
import com.virtualprogrammers.expenses.domain.ExpenseClaim;
import com.virtualprogrammers.expenses.domain.StaffEmployee;

import java.util.ArrayList;

public class RegularExpenseManagementProcess implements ExpenseManagementProcess {

    private ArrayList<ExpenseClaim> expenseClaims = new ArrayList<>();

    @Override
    public int registerExpenseClaim(ExpenseClaim claim) {
        expenseClaims.add(claim);
        return expenseClaims.indexOf(claim);
    }

    @Override
    public boolean approveClaim(int id, Employee approver) {
        double totalAmount = expenseClaims.get(id).getTotalAmount();
        if (totalAmount < 100) {
            return true;
        } else if (totalAmount > 100 && approver instanceof StaffEmployee) {
            return true;
        } else {
            return false;
        }

    }
}
