package com.food.ordering.system.restaurant.service.domain.exception;

import com.food.ordering.system.domain.exception.DomainException;

public class RstaurantNotFoundException extends DomainException {
    public RstaurantNotFoundException(String message) {
        super(message);
    }

    public RstaurantNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
