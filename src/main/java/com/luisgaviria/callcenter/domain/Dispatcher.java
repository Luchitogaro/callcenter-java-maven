package com.luisgaviria.callcenter.domain;

import com.luisgaviria.callcenter.interfaces.CallReceptor;
import com.luisgaviria.callcenter.models.Call;
import com.luisgaviria.callcenter.models.Employee;
import com.luisgaviria.callcenter.utilities.Utilities;
import org.apache.commons.lang3.Validate;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class to execute principal activity on call center, configure threads and execute employee listener.
 *
 * @author Luis Hernando Gaviria Roa <lh_gaviria_r@hotmail.com>
 * @version 1.0.0
 * @since 2019-03-22
 */
public class Dispatcher implements Runnable {

    /**
     * Message to return info about the moment when every employees are busy.
     */
    public static final String NOT_AVAILABLE_EMPLOYEES = "Soryy, in this moment every employees are attending calls, please wait.";
    /**
     * Configure max threads to execute on this session (Available Employees).
     */
    public static final Integer MAX_THREADS = 10;

    /**
     * Attribute to define when a thread start or stop the execution.
     */
    private Boolean active;

    /**
     * Service to execute a thread.
     */
    private ExecutorService executorService;

    /**
     * Linked collection deque to contains the employees to manage.
     */
    private ConcurrentLinkedDeque<Employee> employees;

    /**
     * Linked collection deque to contains the incoming calls.
     */
    private ConcurrentLinkedDeque<Call> incomingCalls;

    /**
     * Instance to access a findEmployee method.
     */
    private CallReceptor callReceptor;

    /**
     * Constructor of dispatcher with the list of employees to set new Dispatcher with CallAttendReceptor.
     *
     * @param employees {@link List<Employee>}
     */
    public Dispatcher(List<Employee> employees) {
        this(employees, new CallAttendReceptor());
    }

    /**
     * Constructor principal of class. Validate when the list of employees or call receptor are null.
     *
     * @param employees    {@link List<Employee>}
     * @param callReceptor {@link CallAttendReceptor} {@link CallReceptor}
     */
    public Dispatcher(List<Employee> employees, CallReceptor callReceptor) {
        Validate.notNull(employees);
        Validate.notNull(callReceptor);
        this.employees = new ConcurrentLinkedDeque(employees);
        this.callReceptor = callReceptor;
        this.incomingCalls = new ConcurrentLinkedDeque<>();
        this.executorService = Executors.newFixedThreadPool(MAX_THREADS);
    }

    /**
     * Method to dispatch a incoming Call delegating to thread executor.
     *
     * @param Call {@link Call}
     */
    public synchronized void dispatchCall(Call Call) {
        Utilities.LOGGER.info("Dispatch new Call of " + Call.getDuration() + " seconds.");
        this.incomingCalls.add(Call);
    }

    /**
     * For each employee on list, start the thread execution on dispatcher.
     */
    public synchronized void start() {
        this.active = true;
        for (Employee employee : this.employees) {
            this.executorService.execute(employee);
        }
    }

    /**
     * Stop run threads on dispatcher.
     */
    public synchronized void stop() {
        this.active = false;
        this.executorService.shutdown();
    }

    /**
     * Get status of current thread.
     *
     * @return {@link Boolean} true or false.
     */
    public synchronized Boolean getActive() {
        return active;
    }

    /**
     * Method delegated to execute runnable instance or Dispatcher.
     * Validate if the incoming call is empty, so, the process continue to new incoming call.
     * When there are an incoming call is assigned to an employee available across the call receptor.
     * The dispatcher works like a queue FIFO.
     */
    @Override
    public void run() {
        while (getActive()) {
            if (this.incomingCalls.isEmpty()) {
                continue;
            } else {
                Employee employee = this.callReceptor.findEmployee(this.employees);
                if (employee == null) {
                    // When there aren't employees available.
                    Utilities.LOGGER.error(NOT_AVAILABLE_EMPLOYEES);
                    continue;
                } else {
                    Call Call = this.incomingCalls.poll(); // Get FIFO calls
                    try {
                        employee.attendCall(Call);
                    } catch (Exception e) {
                        Utilities.LOGGER.error(e.getMessage());
                        this.incomingCalls.addFirst(Call); // To incoming calls aren't attended push into queue at first to attend newly.
                    }
                }
            }
        }
    }
}
