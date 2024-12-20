package com.management.product.service;

import com.management.product.dtos.ProductDto;
import com.management.product.enums.InventoryStatus;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto);
    Optional<ProductDto> getProductById(Long idProduct);
    ProductDto updateProductById(Long idProduct,ProductDto productDto);
    void  removeProductById(Long idProduct);
    boolean isAdmin();
    Page<ProductDto> getProducts(String nameProduct, int page, int size, InventoryStatus inventoryStatus, boolean sortDesc);
}
