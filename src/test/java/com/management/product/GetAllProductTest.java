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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Slf4j
public class GetAllProductTest {

    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    public void setUp() throws  Exception{
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("admin@admin.com");
        authenticationRequest.setPassword("testPassword");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(authenticationRequest);
        MvcResult result = mockMvc.perform(post("/api/v1/users/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andReturn();
        int status = result.getResponse().getStatus();
        assertTrue(status == HttpStatus.OK.value() || status == HttpStatus.CONFLICT.value(), "Error catched ");
    }



    @Test
    public void testAddProduct() throws Exception {
        String token = obtainAccessToken("admin@admin.com", "testPassword");
        log.info("testAddProduct token : {}", token);
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

    @Test
    public void testGetAllProduct() throws Exception {
        String token = obtainAccessToken("admin@admin.com", "testPassword");
        log.info("testGetAllProduct token : {}", token);
        MockHttpServletResponse
                mockHttpServletResponse =   mockMvc.perform(get("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        )
                .andReturn().getResponse();
        mockHttpServletResponse.getContentAsString();
        log.info("testGetAllProduct response  {}",mockHttpServletResponse.getContentAsString());

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
