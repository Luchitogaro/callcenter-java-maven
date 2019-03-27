package com.luisgaviria.callcenter.domain;

import com.luisgaviria.callcenter.interfaces.CallReceptor;
import com.luisgaviria.callcenter.models.Employee;
import com.luisgaviria.callcenter.utilities.EmployeesParams;
import com.luisgaviria.callcenter.utilities.Utilities;
import org.apache.commons.lang3.Validate;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class to execute find available process when a call is received.
 *
 * @author Luis Hernando Gaviria Roa <lh_gaviria_r@hotmail.com>
 * @version 1.0.0
 * @since 2019-03-22
 */
public class CallAttendReceptor implements CallReceptor {

    /**
     * Implement this method to find a Employee available in order, Operator, Supervisor, Director.
     *
     * @param employeeList
     * @return {@link Employee} or Null in case of all employee busy
     */
    @Override
    public Employee findEmployee(Collection<Employee> employeeList) {
        Validate.notNull(employeeList);
        List<Employee> availableEmployeesList = employeeList.stream().filter(e -> e.getEmployeeStatus() == EmployeesParams.EmployeeStatus.AVAILABLE).collect(Collectors.toList());
        Utilities.LOGGER.info("Available employees: " + availableEmployeesList.size() + ".");
        Optional<Employee> employee = availableEmployeesList.stream().filter(e -> e.getEmployeeType() == EmployeesParams.EmployeeType.OPERATOR).findAny();
        if (!employee.isPresent()) {
            Utilities.LOGGER.info("No available operators found.");
            employee = availableEmployeesList.stream().filter(e -> e.getEmployeeType() == EmployeesParams.EmployeeType.SUPERVISOR).findAny();
            if (!employee.isPresent()) {
                Utilities.LOGGER.info("No available supervisors found.");
                employee = availableEmployeesList.stream().filter(e -> e.getEmployeeType() == EmployeesParams.EmployeeType.DIRECTOR).findAny();
                if (!employee.isPresent()) {
                    Utilities.LOGGER.info("No available directors found.");
                    return null;
                }
            }
        }
        Utilities.LOGGER.info("Employee of type " + employee.get().getEmployeeType() + " found.");
        return employee.get();
    }

}
