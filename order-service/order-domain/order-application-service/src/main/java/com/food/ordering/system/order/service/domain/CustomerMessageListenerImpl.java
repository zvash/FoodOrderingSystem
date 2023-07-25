package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.message.CustomerModel;
import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.customer.CustomerMessageListener;
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomerMessageListenerImpl implements CustomerMessageListener {

    private OrderDataMapper orderDataMapper;
    private CustomerRepository customerRepository;

    public CustomerMessageListenerImpl(OrderDataMapper orderDataMapper, CustomerRepository customerRepository) {
        this.orderDataMapper = orderDataMapper;
        this.customerRepository = customerRepository;
    }

    @Override
    public void customerCreated(CustomerModel customerModel) {
        Customer customer = customerRepository.save(orderDataMapper.customerModelToCustomer(customerModel));
        if (customer == null) {
            log.error("Customer could not be created in order database with id: {}", customerModel.getId());
            throw new OrderDomainException("Customer could not be created in order database with id: " +
                    customerModel.getId());
        }
        log.info("Customer is created in order database with id: {}", customerModel.getId());
    }
}
