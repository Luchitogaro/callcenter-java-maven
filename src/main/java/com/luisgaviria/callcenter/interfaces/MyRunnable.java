package com.luisgaviria.callcenter.interfaces;

import com.luisgaviria.callcenter.exceptions.NotAvailableEmployeeException;

public interface MyRunnable extends Runnable {
    void run() throws NotAvailableEmployeeException;
}
