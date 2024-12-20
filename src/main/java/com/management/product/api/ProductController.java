package com.management.product.api;

import com.management.product.dtos.ProductDto;
import com.management.product.enums.InventoryStatus;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


public interface ProductController {
    String PATH_VARIABLE_ID_PRODUCT = "idProduct";
    String URI_ID_PRODUCT = "/{idProduct}";
    String URI_CART_USER = "/cart-user";
    String URI_WISH_LIST = "/wish-list";

    @PostMapping()
    ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto);
    @GetMapping(value = URI_ID_PRODUCT)
    ResponseEntity<ProductDto> getProductById(@PathVariable(name = PATH_VARIABLE_ID_PRODUCT) Long idProduct);
    @PatchMapping(value = URI_ID_PRODUCT)
    ResponseEntity<ProductDto> updateProductById(@PathVariable(name = PATH_VARIABLE_ID_PRODUCT) Long idProduct , @RequestBody ProductDto productDto);
    @DeleteMapping(value = URI_ID_PRODUCT)
    ResponseEntity<Void> deleteProductById(@PathVariable(name = PATH_VARIABLE_ID_PRODUCT) Long idProduct );
    @GetMapping(value = URI_CART_USER)
    ResponseEntity<List<ProductDto>> getProductsOfCartUser();
    @GetMapping(value = URI_WISH_LIST)
    ResponseEntity<List<ProductDto>> getProductsOfWishlistUser();
    @GetMapping()
    Page<ProductDto> getProducts(
            @RequestParam(required = false) String nameProduct,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(name = "inventory", required = false) InventoryStatus inventoryStatus,
            @RequestParam(name = "sortDesc", defaultValue = "true") boolean sortDesc) ;

}
