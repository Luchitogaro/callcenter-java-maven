package com.luisgaviria.callcenter.exceptions;

/**
 * Define an exception for a non available employee
 *
 * @author Luis Hernando Gaviria Roa <lh_gaviria_r@hotmail.com>
 * @version 1.0.0
 * @since 2019-03-22
 */
public class NotAvailableEmployeeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NotAvailableEmployeeException(String message) {
        super(message);
    }
}
