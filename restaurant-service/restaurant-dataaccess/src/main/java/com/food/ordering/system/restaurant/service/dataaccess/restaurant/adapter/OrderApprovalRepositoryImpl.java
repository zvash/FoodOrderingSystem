package com.food.ordering.system.restaurant.service.dataaccess.restaurant.adapter;

import com.food.ordering.system.restaurant.service.dataaccess.restaurant.mapper.RestaurantDataAccessMapper;
import com.food.ordering.system.restaurant.service.dataaccess.restaurant.repository.OrderApprovalJpaRepository;
import com.food.ordering.system.restaurant.service.domain.entity.OrderApproval;
import com.food.ordering.system.restaurant.service.domain.ports.output.repository.OrderApprovalRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderApprovalRepositoryImpl implements OrderApprovalRepository {

    private final RestaurantDataAccessMapper restaurantDataAccessMapper;
    private final OrderApprovalJpaRepository orderApprovalJpaRepository;

    public OrderApprovalRepositoryImpl(RestaurantDataAccessMapper restaurantDataAccessMapper,
                                       OrderApprovalJpaRepository orderApprovalJpaRepository) {
        this.restaurantDataAccessMapper = restaurantDataAccessMapper;
        this.orderApprovalJpaRepository = orderApprovalJpaRepository;
    }

    @Override
    public OrderApproval save(OrderApproval orderApproval) {
        return restaurantDataAccessMapper.orderApprovalEntityToOrderApproval(
                orderApprovalJpaRepository.save(
                        restaurantDataAccessMapper.orderApprovalToOrderApprovalEntity(orderApproval)
                )
        );
    }
}
