package com.food.ordering.system.customer.service;

import com.food.ordering.system.customer.service.entity.Customer;
import com.food.ordering.system.customer.service.event.CustomerCreatedEvent;

public interface CustomerDomainService {

    CustomerCreatedEvent validateAndInitiateCustomer(Customer customer);

}
