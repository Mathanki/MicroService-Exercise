package com.samplemiroservice.demo.inventoryservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.samplemiroservice.demo.inventoryservice.model.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {

	Optional<Inventory> findBySkuCode(String skuCode);
	
	List<Inventory>findBySkuCodeIn(List<String> skuCodeList);

}