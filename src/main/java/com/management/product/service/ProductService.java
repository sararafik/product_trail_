package com.management.product.service;

import com.management.product.dtos.ProductDto;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto);
    Optional<ProductDto> getProductById(Long idProduct);
    ProductDto updateProductById(Long idProduct,ProductDto productDto);
    void  removeProductById(Long idProduct);
    boolean isAdmin();
    Page<ProductDto> getProducts(int page, int size);
}
