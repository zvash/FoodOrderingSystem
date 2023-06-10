package com.food.ordering.system.order.service.domain.ports.input.message.listener.restauarantapproval;

import com.food.ordering.system.order.service.domain.dto.message.RestaurantApprovalResponse;

public interface RestarantApprovalResponseMessageListener {

    void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse);

    void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse);

}