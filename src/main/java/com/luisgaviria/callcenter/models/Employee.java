package com.luisgaviria.callcenter.models;

import com.luisgaviria.callcenter.utilities.EmployeesParams;
import com.luisgaviria.callcenter.utilities.Utilities;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

/**
 * Class to model a generic employee.
 *
 * @author Luis Hernando Gaviria Roa <lh_gaviria_r@hotmail.com>
 * @version 1.0.0
 * @since 2019-03-22
 */
public class Employee implements Runnable {

    /**
     * Employee type.
     */
    private EmployeesParams.EmployeeType employeeType;

    /**
     * Employee status.
     */
    private EmployeesParams.EmployeeStatus employeeStatus;

    /**
     * Linked collection deque to contains the incoming calls.
     */
    private ConcurrentLinkedDeque<Call> incomingCalls;

    /**
     * Linked collection deque to contains the attended calls.
     */
    private ConcurrentLinkedDeque<Call> attendedCalls;

    /**
     * Build a employee with a specific employee type and available status.
     *
     * @param employeeType {@link EmployeesParams.EmployeeType}
     */
    public Employee(EmployeesParams.EmployeeType employeeType) {
        Validate.notNull(employeeType);
        this.employeeType = employeeType;
        this.employeeStatus = EmployeesParams.EmployeeStatus.AVAILABLE;
        this.incomingCalls = new ConcurrentLinkedDeque<>();
        this.attendedCalls = new ConcurrentLinkedDeque<>();
    }

    /**
     * Get current employee type
     *
     * @return {@link EmployeesParams.EmployeeType}
     */
    public EmployeesParams.EmployeeType getEmployeeType() {
        return employeeType;
    }

    /**
     * Get current employee status
     *
     * @return {@link EmployeesParams.EmployeeStatus}
     */
    public synchronized EmployeesParams.EmployeeStatus getEmployeeStatus() {
        return employeeStatus;
    }

    public synchronized void setEmployeeStatus(EmployeesParams.EmployeeStatus employeeStatus) {
        Utilities.LOGGER.info("Employee: " + Thread.currentThread().getName() + ", set status to: " + employeeStatus + ".");
        this.employeeStatus = employeeStatus;
    }

    /**
     * Get attended calls of an employee
     *
     * @return List<Call>
     */
    public synchronized List<Call> getAttendedCalls() {
        return new ArrayList<>(attendedCalls);
    }

    /**
     * Set a Call to an employee
     *
     * @param Call, {@link Call}
     */
    public synchronized void attendCall(Call Call) {
        Utilities.LOGGER.info("Employee: " + Thread.currentThread().getName() + ", attend a Call of " + Call.getDuration() + " seconds.");
        this.incomingCalls.add(Call);
    }

    /**
     * Method to execute a runnable(thread) of Employee
     */
    @Override
    public void run() {
        Utilities.LOGGER.info("Employee: " + Thread.currentThread().getName() + ", starts to work.");
        while (true) {
            if (!this.incomingCalls.isEmpty()) {
                Call Call = this.incomingCalls.poll();
                this.setEmployeeStatus(EmployeesParams.EmployeeStatus.BUSY);
                Utilities.LOGGER.info("Employee: " + Thread.currentThread().getName() + ", receive Call of " + Call.getDuration() + " seconds.");
                try {
                    TimeUnit.SECONDS.sleep(Call.getDuration());
                } catch (InterruptedException e) {
                    Utilities.LOGGER.error("Employee: " + Thread.currentThread().getName() + ", finished the Call with problems." + e.getMessage() + ".");
                } finally {
                    this.setEmployeeStatus(EmployeesParams.EmployeeStatus.AVAILABLE);
                }
                this.attendedCalls.add(Call);
                Utilities.LOGGER.info("Employee: " + Thread.currentThread().getName() + ", finished a Call correctly of " + Call.getDuration() + " seconds.");
            }
        }
    }
}
