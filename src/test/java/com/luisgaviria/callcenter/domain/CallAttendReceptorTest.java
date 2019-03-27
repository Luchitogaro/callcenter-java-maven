package com.luisgaviria.callcenter.domain;

import com.luisgaviria.callcenter.models.Employee;
import com.luisgaviria.callcenter.utilities.EmployeesParams;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Class to test the domain class CallAttendReceptorTest
 *
 * @author Luis Hernando Gaviria Roa <lh_gaviria_r@hotmail.com>
 * @version 1.0.0
 * @since 2019-03-22
 */
public class CallAttendReceptorTest {

    /**
     * Object to test the CallAttendReceptor.
     */
    private CallAttendReceptor callAttendReceptor;
    /**
     * Object to represent an operator employee.
     */
    private Employee employeeOperator;
    /**
     * Object to represent a supervisor employee.
     */
    private Employee employeeSupervisor;
    /**
     * Object to represent a director employee.
     */
    private Employee employeeDirector;

    /**
     * Object to represent an employee found in tests.
     */
    private Employee employeeFound;

    /**
     * List of employees to test
     */
    private List<Employee> employeeList;

    /**
     * Initial setup all object required in the test
     */
    @Before
    public void setup() {
        this.callAttendReceptor = new CallAttendReceptor();
        this.employeeOperator = new Employee(EmployeesParams.EmployeeType.OPERATOR);
        this.employeeSupervisor = new Employee(EmployeesParams.EmployeeType.SUPERVISOR);
        this.employeeDirector = new Employee(EmployeesParams.EmployeeType.DIRECTOR);
    }

    /**
     * Test to find a available operator to assign.
     */
    @Test
    public void findEmployeeOperatorAvailableTest() {
        this.employeeList = Arrays.asList(employeeOperator, employeeSupervisor, employeeDirector);

        this.employeeFound = this.callAttendReceptor.findEmployee(this.employeeList);

        Assert.assertNotNull(this.employeeFound);
        Assert.assertEquals(EmployeesParams.EmployeeType.OPERATOR, this.employeeFound.getEmployeeType());
    }

    /**
     * Test to find a available supervisor to assign.
     */
    @Test
    public void findEmployeeSupervisorAvailableTest() {
        this.employeeOperator.setEmployeeStatus(EmployeesParams.EmployeeStatus.BUSY);

        this.employeeList = Arrays.asList(employeeOperator, employeeSupervisor, employeeDirector);

        this.employeeFound = this.callAttendReceptor.findEmployee(this.employeeList);

        Assert.assertNotNull(this.employeeFound);
        Assert.assertEquals(EmployeesParams.EmployeeType.SUPERVISOR, this.employeeFound.getEmployeeType());
    }

    /**
     * Test to find a available director to assign.
     */
    @Test
    public void findEmployeeDirectorAvailableTest() {

        this.employeeOperator.setEmployeeStatus(EmployeesParams.EmployeeStatus.BUSY);
        this.employeeSupervisor.setEmployeeStatus(EmployeesParams.EmployeeStatus.BUSY);

        this.employeeList = Arrays.asList(employeeOperator, employeeSupervisor, employeeDirector);

        this.employeeFound = this.callAttendReceptor.findEmployee(this.employeeList);

        Assert.assertNotNull(this.employeeFound);
        Assert.assertEquals(EmployeesParams.EmployeeType.DIRECTOR, this.employeeFound.getEmployeeType());
    }

    /**
     * Test to not found a available employee.
     */
    @Test
    public void notFoundAvailableEmployeeTest() {

        this.employeeOperator.setEmployeeStatus(EmployeesParams.EmployeeStatus.BUSY);
        this.employeeSupervisor.setEmployeeStatus(EmployeesParams.EmployeeStatus.BUSY);
        this.employeeDirector.setEmployeeStatus(EmployeesParams.EmployeeStatus.BUSY);

        this.employeeList = Arrays.asList(employeeOperator, employeeSupervisor, employeeDirector);

        this.employeeFound = this.callAttendReceptor.findEmployee(this.employeeList);

        Assert.assertNull(this.employeeFound);
    }


}
