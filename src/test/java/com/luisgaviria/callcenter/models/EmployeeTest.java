package com.luisgaviria.callcenter.models;

import com.luisgaviria.callcenter.utilities.EmployeesParams;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * Class to test the model Employee
 *
 * @author Luis Hernando Gaviria Roa <lh_gaviria_r@hotmail.com>
 * @version 1.0.0
 * @since 2019-03-22
 */
public class EmployeeTest {

    private static final Long MIN_DURATION_CALL = 5L;
    private static final Long MAX_DURATION_CALL = 10L;

    /**
     * Test to validate the creation an employee of type operator.
     */
    @Test
    public void createEmployeeTest() {
        Employee employee = new Employee(EmployeesParams.EmployeeType.OPERATOR);

        Assert.assertNotNull(employee);
        Assert.assertEquals(employee.getEmployeeType(), EmployeesParams.EmployeeType.OPERATOR);
        Assert.assertEquals(employee.getEmployeeStatus(), EmployeesParams.EmployeeStatus.AVAILABLE);
    }

    /**
     * Create a employee with type null respond validation null error
     */
    @Test
    public void createEmployeeFailNullTypeTest() {
        try {

            Employee employee = new Employee(null);
        } catch (NullPointerException e) {
            Assert.assertEquals("The validated object is null", e.getMessage());
        }
    }

    /**
     * Create a employee and verify ths status when is available and next attend a call
     * verify status busy
     */
    @Test
    public void getStatusEmployeeAfterAndBeforeAttendACall() throws InterruptedException {

        Employee employee = new Employee(EmployeesParams.EmployeeType.OPERATOR);
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(employee);
        // Verify operator available
        assertEquals(EmployeesParams.EmployeeStatus.AVAILABLE, employee.getEmployeeStatus());

        TimeUnit.SECONDS.sleep(1);
        employee.attendCall(new Call(MIN_DURATION_CALL, MAX_DURATION_CALL));
        employee.attendCall(new Call(MIN_DURATION_CALL, MAX_DURATION_CALL));
        TimeUnit.SECONDS.sleep(1);

        // Verify status operator busy
        assertEquals(EmployeesParams.EmployeeStatus.BUSY, employee.getEmployeeStatus());

        executorService.awaitTermination(MAX_DURATION_CALL * 2, TimeUnit.SECONDS);

        // Verify total of calls attended by operator employee
        assertEquals(2, employee.getAttendedCalls().size());
    }
}
