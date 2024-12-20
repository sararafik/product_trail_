package com.management.product.api;

import com.management.product.dtos.ProductDto;
import com.management.product.service.CartService;
import com.management.product.service.ProductService;
import com.management.product.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;

import java.util.List;

import static org.zalando.problem.Status.*;


@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductControllerImpl implements ProductController {
    public static final String PRODUCT_NOT_FOUND = "PRODUCT NOT FOUND";
    public static final String UNAUTHORIZED_ACCESS = "Unauthorized Access";
    public static final String THE_USER_IS_NOT_AUTHORIZED = "The user is not authorized.";
    public static final String THE_PRODUCT_NOT_FOUNDED = "The product with id %s not founded.";
    public static final String ERROR_PRODUCTS = "error in getting products";
    public static final String ERROR__FETCHING_PRODUCTS = "Error occurred while fetching products ";
    private final ProductService productService;
    private final CartService cartService;
    private final WishlistService wishlistService;
    @Override
    public ResponseEntity<ProductDto> addProduct(ProductDto productDto) {
            if(productService.isAdmin()){
                ProductDto product = productService.createProduct(productDto);
                return ResponseEntity
                        .ok(product);
            }else{
                throw Problem.builder()
                        .withTitle(UNAUTHORIZED_ACCESS)
                        .withStatus(UNAUTHORIZED)
                        .withDetail(THE_USER_IS_NOT_AUTHORIZED)
                        .build();
            }
    }


    @Override
    public ResponseEntity<ProductDto> getProductById(Long idProduct) {
        return productService.getProductById(idProduct)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> Problem.builder()
                        .withTitle(PRODUCT_NOT_FOUND)
                        .withStatus(NOT_FOUND)
                        .withDetail(String.format(THE_PRODUCT_NOT_FOUNDED, idProduct))
                        .build());
    }


    @Override
    public ResponseEntity<ProductDto> updateProductById(Long idProduct, ProductDto productDto) {
            if(productService.isAdmin()){
                return ResponseEntity.ok(productService.updateProductById(idProduct, productDto));
            }else{
                throw Problem.builder()
                        .withTitle(UNAUTHORIZED_ACCESS)
                        .withStatus(UNAUTHORIZED)
                        .withDetail(THE_USER_IS_NOT_AUTHORIZED)
                        .build();
            }
    }

    @Override
    public ResponseEntity<Void> deleteProductById(Long  idProduct) {
            if(productService.isAdmin()){
                productService.removeProductById(idProduct);
                return ResponseEntity.noContent().build();
            }else {
                throw Problem.builder()
                        .withTitle(UNAUTHORIZED_ACCESS)
                        .withStatus(UNAUTHORIZED)
                        .withDetail(THE_USER_IS_NOT_AUTHORIZED)
                        .build();
            }
    }

    @Override
    public ResponseEntity<List<ProductDto>> getProductsOfCartUser() {
        return ResponseEntity.ok(cartService.getProductsOfCartUser());
    }

    @Override
    public ResponseEntity<List<ProductDto>> getProductsOfWishlistUser() {
        return ResponseEntity.ok(wishlistService.getProductsOfWishlistUser());
    }

    @Override
    public Page<ProductDto> getProducts(int page, int size) {
        return productService.getProducts(page, size);
    }
}
