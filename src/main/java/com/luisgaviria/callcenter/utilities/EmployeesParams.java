package com.luisgaviria.callcenter.utilities;

/**
 * Class to model a employee parameters
 *
 * @author Luis Hernando Gaviria Roa <lh_gaviria_r@hotmail.com>
 * @version 1.0.0
 * @since 2019-03-22
 */
public class EmployeesParams {

    /**
     * Params to define the kind of employee
     */
    public enum EmployeeType {
        OPERATOR,
        SUPERVISOR,
        DIRECTOR
    }

    /**
     * Params to define possibles employee status
     */
    public enum EmployeeStatus {
        AVAILABLE,
        BUSY
    }
}
