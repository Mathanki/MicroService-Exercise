package com.samplemiroservice.demo.orderservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samplemiroservice.demo.orderservice.dto.OrderLineItemsDto;
import com.samplemiroservice.demo.orderservice.dto.OrderRequest;
import com.samplemiroservice.demo.orderservice.model.Order;
import com.samplemiroservice.demo.orderservice.model.OrderLineItems;
import com.samplemiroservice.demo.orderservice.repository.OrderRepository;

@Service
@Transactional
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	public void placeOrder(OrderRequest orderRequest) {
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList().stream()
				.map(orderLineItemsDto -> mapToDto(orderLineItemsDto)).toList();
		order.setOrderLineItemsList(orderLineItemsList);
		orderRepository.save(order);

	}

	private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
		OrderLineItems orderLineItems = new OrderLineItems();
		orderLineItems.setPrice(orderLineItemsDto.getPrice());
		orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
		orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
		return orderLineItems;
	}

}
