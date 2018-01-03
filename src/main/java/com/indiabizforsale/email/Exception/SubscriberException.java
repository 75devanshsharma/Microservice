package com.indiabizforsale.email.Exception;

public class SubscriberException extends RuntimeException {
    public SubscriberException(Exception e) {
        super(e);
    }
}