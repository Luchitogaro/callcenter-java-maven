package com.luisgaviria.callcenter.interfaces;

import com.luisgaviria.callcenter.models.Employee;

import java.util.Collection;

/**
 * Interface to define recurrent Employee methods
 *
 * @author Luis Hernando Gaviria Roa <lh_gaviria_r@hotmail.com>
 * @version 1.0.0
 * @since 2019-03-22
 */
public interface CallReceptor {

    /**
     * Find an Employee into available
     *
     * @param employees List of employee
     * @return An Employee
     */
    Employee findEmployee(Collection<Employee> employees);
}
