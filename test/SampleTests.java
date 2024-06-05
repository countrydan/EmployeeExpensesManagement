import static org.junit.jupiter.api.Assertions.*;

import com.virtualprogrammers.expenses.exceptions.InvalidEmployeeIdIdException;
import com.virtualprogrammers.expenses.utilities.EmployeeUtilities;
import org.junit.jupiter.api.Test;

public class SampleTests {
    @Test
    public void testSomething() {

        int a = 4;
        int b = 8;
        int total = a + b;

        assertEquals(12, total);
    }

    @Test
    public void testEmployeeIdNUmberIsConvertedCorrectly() throws InvalidEmployeeIdIdException {
        int result = EmployeeUtilities.validateEmployeeId("416");
        assertEquals(416, result);
    }

    @Test
    public void testThatExceptionIsThrownIfEmployeeIdIsNotAValidNumber() {
        assertThrows(InvalidEmployeeIdIdException.class, () -> {
            int result = EmployeeUtilities.validateEmployeeId("hello");
        });
    }
}
