package com.samplemiroservice.demo.orderservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.samplemiroservice.demo.orderservice.dto.InventoryResponse;
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

	@Autowired
	private WebClient.Builder webClientBuilder;

	public void placeOrder(OrderRequest orderRequest) {
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList().stream()
				.map(orderLineItemsDto -> mapToDto(orderLineItemsDto)).toList();
		order.setOrderLineItemsList(orderLineItemsList);

		List<String> suCodeList = order.getOrderLineItemsList()
				.stream()
				.map(orderLineItem -> orderLineItem.getSkuCode())
				.toList();
		// call inventory service if product is there only place order.
		InventoryResponse[] inventoryResponseArr = webClientBuilder.build().get().uri("http://inventory-service/api/inventory", 
				uriBuilder ->uriBuilder.queryParam("skuCode", suCodeList).build())
				.retrieve()
				.bodyToMono(InventoryResponse[].class)
				.block();
		
		Boolean isAllproductInStock =  Arrays.stream(inventoryResponseArr).allMatch(inventoryRes -> inventoryRes.isInStock());

		if (isAllproductInStock) {
			orderRepository.save(order);
		} else {
			throw new IllegalArgumentException("Product is not in stock, please try again later");
		}

	}

	private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
		OrderLineItems orderLineItems = new OrderLineItems();
		orderLineItems.setPrice(orderLineItemsDto.getPrice());
		orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
		orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
		return orderLineItems;
	}

}
