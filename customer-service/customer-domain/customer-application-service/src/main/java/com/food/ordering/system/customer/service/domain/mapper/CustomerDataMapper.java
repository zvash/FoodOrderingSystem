package com.food.ordering.system.customer.service.domain.mapper;

import com.food.ordering.system.customer.service.domain.dto.create.CreateCustomerCommand;
import com.food.ordering.system.customer.service.domain.dto.create.CreateCustomerResponse;
import com.food.ordering.system.customer.service.domain.entity.Customer;
import com.food.ordering.system.domain.valueobject.CustomerId;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CustomerDataMapper {

    public Customer createCustomerCommandToCustomer(CreateCustomerCommand createCustomerCommand) {
        return new Customer(
                new CustomerId(UUID.fromString(createCustomerCommand.getCustomerId())),
                createCustomerCommand.getUsername(),
                createCustomerCommand.getFirstName(),
                createCustomerCommand.getLastName()
        );
    }

    public CreateCustomerResponse customerToCreateCustomerResponse(Customer customer, String message) {
        return CreateCustomerResponse.builder()
                .customerId(customer.getId().getValue())
                .message(message)
                .build();
    }

}
