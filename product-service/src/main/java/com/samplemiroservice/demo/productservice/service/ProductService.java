package com.samplemiroservice.demo.productservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samplemiroservice.demo.productservice.dto.ProductRequest;
import com.samplemiroservice.demo.productservice.dto.ProductResponse;
import com.samplemiroservice.demo.productservice.model.Product;
import com.samplemiroservice.demo.productservice.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public void createProduct(ProductRequest productRequest) {
		Product product = Product.builder().name(productRequest.getName()).description(productRequest.getDescription())
				.price(productRequest.getPrice()).build();
		productRepository.save(product);
		log.info("Product {} is Saved", product.getId());
	}

	public List<ProductResponse> getAllProducts() {

		List<Product> products = productRepository.findAll();
		List<ProductResponse> productResponseList = products.stream().map(product -> mapToProductResponse(product))
				.toList();
		return productResponseList;
	}

	private ProductResponse mapToProductResponse(Product product) {

		return ProductResponse.builder().id(product.getId()).name(product.getName())
				.description(product.getDescription()).price(product.getPrice()).build();
	}

}
