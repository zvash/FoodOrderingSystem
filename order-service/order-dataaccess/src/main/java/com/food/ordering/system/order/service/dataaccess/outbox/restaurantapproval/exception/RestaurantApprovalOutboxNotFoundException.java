package com.food.ordering.system.order.service.dataaccess.outbox.restaurantapproval.exception;

public class RestaurantApprovalOutboxNotFoundException extends RuntimeException  {
    public RestaurantApprovalOutboxNotFoundException(String message) {
        super(message);
    }

    public RestaurantApprovalOutboxNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
