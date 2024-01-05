package com.samplemiroservice.demo.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.samplemiroservice.demo.productservice.model.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

}
