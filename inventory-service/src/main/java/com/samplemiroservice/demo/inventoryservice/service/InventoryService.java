package com.samplemiroservice.demo.inventoryservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samplemiroservice.demo.inventoryservice.dto.InventoryResponse;
import com.samplemiroservice.demo.inventoryservice.model.Inventory;
import com.samplemiroservice.demo.inventoryservice.repository.InventoryRepository;

@Service
public class InventoryService {

	@Autowired
	private InventoryRepository inventoryRepository;

	@Transactional(readOnly = true)
	public List<InventoryResponse> isInStock(List<String> skuCodeList) {
		List<Inventory> inventoryList = inventoryRepository.findBySkuCodeIn(skuCodeList);

		List<InventoryResponse> inventoryResponseList = inventoryList.stream()
				.map(inventory -> InventoryResponse
				.builder()
				.skuCode(inventory.getSkuCode())
				.isInStock(inventory.getQuantity() > 0)
				.build())
				.toList();

		return inventoryResponseList;
	}

}
