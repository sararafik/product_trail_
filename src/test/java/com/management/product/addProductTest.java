package com.management.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.management.product.dtos.AuthenticationRequest;
import com.management.product.dtos.AuthenticationResponse;
import com.management.product.dtos.ProductDto;
import com.management.product.enums.InventoryStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Slf4j
public class addProductTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() throws  Exception{
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("admin@admin.com");
        authenticationRequest.setPassword("testPassword");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(authenticationRequest);
        mockMvc.perform(post("/api/v1/users/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk());
    }


    @Test
    public void testAddProduct() throws Exception {
        String token = obtainAccessToken("admin@admin.com", "testPassword");
        ProductDto productDto = new ProductDto();
        productDto.setCodeProduct("CodeProductTest1");
        productDto.setNameProduct("nameProductTest1");
        productDto.setDescriptionProduct("DescriptionProductTest1");
        productDto.setImageProduct("ImageProductTest1");
        productDto.setCategoryProduct("CategoryProductTest1");
        productDto.setPriceProduct(456);
        productDto.setQuantityProduct(19);
        productDto.setInternalReferenceProduct("InternalReferenceProductTest1");
        productDto.setShellIdProduct(4444);
        productDto.setInventoryStatus(InventoryStatus.INSTOCK);
        productDto.setRatingProduct(222);
        ObjectMapper objectMapper = new ObjectMapper();
        String productJson = objectMapper.writeValueAsString(productDto);
        log.info("productJson : {} ",productJson);
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(productJson))
                .andExpect(status().isOk());
    }



    private String obtainAccessToken(String email,String password) throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail(email);
        authenticationRequest.setPassword(password);
        ObjectMapper objectMapper = new ObjectMapper();
        String authJson = objectMapper.writeValueAsString(authenticationRequest);
        log.info("authJson : {}",authJson);
        MvcResult result = mockMvc.perform(post("/api/authentication/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authJson))
                        .andReturn();
        String response = result.getResponse().getContentAsString();
        log.info("response : {}",response);
        AuthenticationResponse authenticationResponse = objectMapper.readValue(response,AuthenticationResponse.class);
        return authenticationResponse.getToken();

    }
}
