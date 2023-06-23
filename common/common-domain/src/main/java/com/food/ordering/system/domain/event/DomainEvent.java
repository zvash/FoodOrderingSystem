package com.food.ordering.system.domain.event;

//T is the Entity type that has dispatched this event
public interface DomainEvent<T> {

    void fire();
}
