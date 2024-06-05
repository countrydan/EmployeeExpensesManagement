package com.virtualprogrammers.expenses.process;

import com.virtualprogrammers.expenses.domain.Employee;
import com.virtualprogrammers.expenses.domain.ExpenseClaim;

public interface ExpenseManagementProcess {

    public int registerExpenseClaim(ExpenseClaim claim);
    public boolean approveClaim(int id, Employee approver);

}
