package com.example.store.controller;

import com.example.store.entity.Customer;
import com.example.store.mapper.CustomerMapper;
import com.example.store.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@ComponentScan(basePackageClasses = CustomerMapper.class)
class CustomerControllerExtendedTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerRepository customerRepository;

    private Customer customer1;
    private Customer customer2;

    @BeforeEach
    void setUp() {
        customer1 = new Customer();
        customer1.setName("John Doe");
        customer1.setId(1L);

        customer2 = new Customer();
        customer2.setName("Jane Smith");
        customer2.setId(2L);
    }

    @Test
    void testGetAllCustomersWithoutQuery() throws Exception {
        when(customerRepository.findAll()).thenReturn(List.of(customer1, customer2));

        mockMvc.perform(get("/customer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Smith"));
    }

    @Test
    void testGetCustomersByNameQuery() throws Exception {
        when(customerRepository.findByNameContainsIgnoreCase("John")).thenReturn(List.of(customer1));

        mockMvc.perform(get("/customer?query=John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    void testGetCustomersByNameQueryNoMatch() throws Exception {
        when(customerRepository.findByNameContainsIgnoreCase("Unknown")).thenReturn(List.of());

        mockMvc.perform(get("/customer?query=Unknown"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", List.of()));
    }

    @Test
    void testCreateCustomer() throws Exception {
        when(customerRepository.save(customer1)).thenReturn(customer1);

        mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }
}
