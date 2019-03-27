package com.luisgaviria.callcenter.domain;

import com.luisgaviria.callcenter.models.Call;
import com.luisgaviria.callcenter.models.Employee;
import com.luisgaviria.callcenter.utilities.EmployeesParams;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;

/**
 * Class to test the model Call
 *
 * @author Luis Hernando Gaviria Roa <lh_gaviria_r@hotmail.com>
 * @version 1.0.0
 * @since 2019-03-22
 */
public class DispatcherTest {

    /**
     * Quantity of calls to run tests.
     */
    private static final long CALLS_QUANTITY = 10L;
    /**
     * Define minimum call duration configure.
     */
    private static final long MIN_CALL_DURATION = 5L;
    /**
     * Define maximum call duration configure.
     */
    private static final long MAX_CALL_DURATION = 10L;

    /**
     * Quantity operator to create in the tests
     */
    private static final int OPERATOR_QUANTITY = 6;
    /**
     * Quantity supervisor to create in the tests
     */
    private static final long SUPERVISOR_QUANTITY = 3;
    /**
     * Quantity director to create in the tests
     */
    private static final long DIRECTOR_QUANTITY = 1;

    /**
     * List of calls to test.
     */
    private List<Call> callList;
    /**
     * List of employees to test.
     */
    private List<Employee> employeeList;

    /**
     * Initial setup all object required in the test.
     */
    @Before
    public void setup() {
        this.callList = Call.buildListOfRandomCalls(CALLS_QUANTITY, MIN_CALL_DURATION, MAX_CALL_DURATION);

        this.employeeList = new ArrayList<>();

        for (int i = 0; i < OPERATOR_QUANTITY; i++) {
            this.employeeList.add(new Employee(EmployeesParams.EmployeeType.OPERATOR));
        }
        for (int i = 0; i < SUPERVISOR_QUANTITY; i++) {
            this.employeeList.add(new Employee(EmployeesParams.EmployeeType.SUPERVISOR));
        }
        for (int i = 0; i < DIRECTOR_QUANTITY; i++) {
            this.employeeList.add(new Employee(EmployeesParams.EmployeeType.DIRECTOR));
        }
    }

    /**
     * Test validation employees list not null in constructor of Dispatcher.
     */
    @Test
    public void createDispatcherWitNullEmployeesFail() {
        try {

            Dispatcher dispatcher = new Dispatcher(null);
        } catch (NullPointerException e) {
            Assert.assertEquals("The validated object is null", e.getMessage());
        }
    }

    /**
     * Test dispatch call method to get a response from the employees queue.
     */
    @Test
    public void dispatchCallsToEmployeeTest() throws InterruptedException {
        Dispatcher dispatcher = new Dispatcher(this.employeeList);

        dispatcher.start();

        TimeUnit.SECONDS.sleep(1);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(dispatcher);
        TimeUnit.SECONDS.sleep(1);

        this.callList.stream().forEach(call -> {
            dispatcher.dispatchCall(call);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                fail();
            }
        });

        executorService.awaitTermination(MAX_CALL_DURATION * 2, TimeUnit.SECONDS);
        Assert.assertEquals(CALLS_QUANTITY, employeeList.stream().mapToInt(
                employee -> employee.getAttendedCalls().size()).sum());
    }

    /**
     * Test dispatch call method to get a response from the employees queue,
     * however the call not attended is add to queue first position to attend
     * when there is availability, because every employees are busy.
     */
    @Test
    public void dispatchCallsToEmployeeEveryBusyTest() throws InterruptedException {
        Dispatcher dispatcher = new Dispatcher(this.employeeList);
        this.callList = Call.buildListOfRandomCalls(20L, MIN_CALL_DURATION, MAX_CALL_DURATION * 2);


        dispatcher.start();

        TimeUnit.SECONDS.sleep(1);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(dispatcher);
        TimeUnit.SECONDS.sleep(1);

        this.callList.stream().forEach(call -> {

            dispatcher.dispatchCall(call);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                fail();
            }

        });
        executorService.awaitTermination(MAX_CALL_DURATION * 2 * 2, TimeUnit.SECONDS);
        Assert.assertEquals(20, this.employeeList.stream().mapToInt(
                employee -> employee.getAttendedCalls().size()).sum());
    }

}
