package com.devsuperior.dscatalog.services.exceptions;

public class PropertyNotFoundException extends RuntimeException{
    public PropertyNotFoundException(String msg) {
        super(msg);
    }
}
