package com.food.ordering.system.order.service.domain.dto.message;

import com.food.ordering.system.domain.valueobject.CustomerId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CustomerModel {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
}
