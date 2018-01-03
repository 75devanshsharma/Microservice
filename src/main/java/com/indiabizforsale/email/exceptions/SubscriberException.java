package com.indiabizforsale.email.exceptions;

public class SubscriberException extends RuntimeException {
    public SubscriberException(Exception e) {
        super(e);
    }
}