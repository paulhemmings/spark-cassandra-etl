package com.razor.solrcassandra.services;

/**
 * Created by paul.hemmings on 2/12/16.
 */
public class ServiceException extends Exception {
    private int statusCode = 400;
    public ServiceException(String message) { super(message); }
    public ServiceException(String message, Throwable throwable) { super(message, throwable);}
    public int getStatusCode(){ return this.statusCode; }
    public ServiceException setStatusCode(int code) {
        this.statusCode = code;
        return this;
    }
}
