package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderCreateCommandHandler {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderDataMapper orderDataMapper;

    public OrderCreateCommandHandler(OrderDomainService orderDomainService,
                                     OrderRepository orderRepository,
                                     CustomerRepository customerRepository,
                                     RestaurantRepository restaurantRepository,
                                     OrderDataMapper orderDataMapper) {
        this.orderDomainService = orderDomainService;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderDataMapper = orderDataMapper;
    }

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitiateOrder(order, restaurant);
        Order savedOrder = saveOrder(order);
        log.info("New order is saved with id: {}", savedOrder.getId().getValue());
        return orderDataMapper.orderToCreateOrderResponse(savedOrder, "New order is saved with id: " +
                savedOrder.getId().getValue());
    }

    private Order saveOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        if (savedOrder == null) {
            log.error("Could not save the order!");
            throw new OrderDomainException("Could not save the order!");
        }
        log.info("New order is saved with id: {}", savedOrder.getId().getValue());
        return order;
    }


    private void checkCustomer(UUID customerId) {
        Optional<Customer> customerResult = customerRepository.findCustomer(customerId);
        if (customerResult.isEmpty()) {
            log.warn("No customer is found with customerId: {}", customerId);
            throw new OrderDomainException("No customer is found with customerId: " + customerId);
        }
    }

    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
        Restaurant restaurant = orderDataMapper.createOrderCommandToRestaurant(createOrderCommand);
        Optional<Restaurant> restaurantResult = restaurantRepository.findRestaurantInformation(restaurant);
        if (restaurantResult.isEmpty()) {
            log.warn("Could not find restaurant with restaurantId: {}", restaurant.getId().getValue());
            throw new OrderDomainException("Could not find restaurant with restaurantId: " +
                    restaurant.getId().getValue());
        }
        return restaurantResult.get();
    }


}
