/*
 * Copyright (c) 2024. Paul Nwabudike
 * Since: July 2024
 * Author: Paul Nwabudike
 * Name: ProductService
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at   http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 */

package com.ecomm.microservices.product.service;

import com.ecomm.microservices.product.dto.ProductRequest;
import com.ecomm.microservices.product.dto.ProductResponse;
import com.ecomm.microservices.product.model.Product;
import com.ecomm.microservices.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    // @RequiredArgsConstructor was used to generate the constructor below
    /* public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    } */
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .build();

        productRepository.save(product);
        log.info("Product {} is saved", product.getId());

        return  new ProductResponse(product.getId(), product.getName(),
                product.getDescription(), product.getPrice());

    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products =  productRepository.findAll();
        return products.stream().map(this::mapToProductResponse).toList();

    }

    private ProductResponse mapToProductResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(),
                product.getDescription(), product.getPrice());
    }

}
