package com.virtualprogrammers.expenses.utilities;

import com.virtualprogrammers.expenses.exceptions.InvalidEmployeeIdIdException;
import com.virtualprogrammers.expenses.exceptions.NameTooShortException;

public class EmployeeUtilities {

    public static int validateEmployeeId(String employeeId) throws InvalidEmployeeIdIdException {
        int integerId;
        try {
            integerId = Integer.parseInt(employeeId);
        } catch (NumberFormatException e) {
            throw new InvalidEmployeeIdIdException();
        }
        return integerId;
    }

    public static void validateEmployeeName(String firstName, String surname) throws NameTooShortException {
        if (firstName.length() + surname.length() < 6) {
            throw new NameTooShortException();
        }
    }

}
