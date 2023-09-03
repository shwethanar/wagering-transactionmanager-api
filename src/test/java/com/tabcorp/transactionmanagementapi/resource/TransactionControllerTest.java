package com.tabcorp.transactionmanagementapi.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tabcorp.transactionmanagementapi.dto.TransactionRequest;
import com.tabcorp.transactionmanagementapi.errorhandling.JsonDeserializationException;
import com.tabcorp.transactionmanagementapi.models.Transaction;
import com.tabcorp.transactionmanagementapi.service.TransactionService;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

//@WebMvcTest(TransactionController.class)
@ContextConfiguration(classes = { TransactionControllerTest.TestConfig.class })

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@Import(SecurityTestConfiguration.class) // Import the custom security configuration

public class TransactionControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private FilterChainProxy springSecurityFilterChain;
    
    @InjectMocks
    private TransactionController transactionController;

    @Mock
    private TransactionService transactionService;
 
    @Autowired
    private ObjectMapper objectMapper;
    private RestTemplate restTemplate;

   
    
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(springSecurityFilterChain)
                .build();

        // Configure SecurityContextHolder to use the custom filter
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        restTemplate = new RestTemplate();
    }

    @Test
    public void testCreateTransactionFromJson_Success() throws Exception {
        // Create a valid JSON request
        TransactionRequest request = new TransactionRequest();
        request.setCustomerId(10002L);
        request.setProductCode("PRODUCT_001");
        // Set other fields as needed

        // Create a mock Transaction
        Transaction mockTransaction = new Transaction();
        mockTransaction.setId(1L);
        // Set other fields as needed

        // Mock the behavior of the service method
        Mockito.when(transactionService.addTransaction(Mockito.any(TransactionRequest.class))).thenReturn(mockTransaction);

        String baseUrl = "http://localhost:8086"; // Replace with your actual base URL
        String uri = baseUrl + "/transactions/json";

        // Create HttpHeaders with authentication credentials
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth("admin", "admin"); // Replace with actual credentials

        // Create an HttpEntity with headers
        HttpEntity<TransactionRequest> entity = new HttpEntity<>(request, headers);

        // Send a POST request with JSON data and authentication headers
        ResponseEntity<Transaction> response = restTemplate.exchange(
            uri,
            HttpMethod.POST,
            entity,
            Transaction.class
        );

        // Check the response status and body
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
      //  assertEquals(mockTransaction.getId(), response.getBody().getId());
    }

    

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void testCreateTransactionFromJson_InvalidJson() throws Exception {
        // Create an invalid JSON request
        String jsonRequest = "Invalid JSON";
        String baseUrl = "http://localhost:8086";
        String uri = baseUrl + "/transactions/json";

     // Encode your username and password as a Basic Authentication header
        String username = "admin";
        String password = "admin";
        String authHeader = "Basic " + new String(Base64.encode((username + ":" + password).getBytes()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", authHeader); // Add the Basic Authentication header

        // Attempt to parse the JSON request
        TransactionRequest request;
       /* try {
            request = objectMapper.readValue(jsonRequest, TransactionRequest.class);
        } catch (JsonProcessingException e) {
            // Handle the JSON parsing exception here
            throw new JsonDeserializationException("Invalid JSON request: " + e.getMessage());
        }*/

      //  HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
        // Send a POST request with the invalid JSON data
        ResponseEntity<Transaction> responseEntity = restTemplate.exchange(
        		uri,
                HttpMethod.POST,
                new HttpEntity<>(jsonRequest, headers),
                Transaction.class
        );
        } catch (HttpClientErrorException.BadRequest e) {
            // Catch the exception and handle it gracefully
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());

        }
    }



    @Test
    public void testHandleNoFirstName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/transactions/sdf"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGreeting() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/transactions/test"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        assertEquals("Hello", responseContent+"Hello");
    }

    @Test
    public void testGetTotalCostByCustomer() throws Exception {
        // Define the expected total cost for a customer
        BigDecimal expectedTotalCost = new BigDecimal("100.00");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/transactions/report/total-cost-by-customer/{customerId}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Verify that the response contains the expected total cost
        // For example, you can parse the response content as JSON and assert its value
    }

    @Test
    public void testGetTotalCostByProduct() throws Exception {
        // Define the expected total cost for a product
        BigDecimal expectedTotalCost = new BigDecimal("50.00");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/transactions/report/total-cost-by-product/{productCode}", "PRODUCT_001"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Verify that the response contains the expected total cost
        // For example, you can parse the response content as JSON and assert its value
    }

    @Test
    public void testGetTransactionsToAustraliaCount() throws Exception {
        // Define the expected transaction count to Australia
        Long expectedTransactionCount = 10L;

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/transactions/report/transactions-to-australia-count"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Verify that the response contains the expected transaction count
        // For example, you can parse the response content as JSON and assert its value
    }

   /* @Test
    public void testGetTotalCostForCustomers() throws Exception {
        // Define the expected total cost map for customers
        Map<Long, BigDecimal> expectedTotalCostMap =  Define your expected map here ;

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/transactions/report/total-cost-per-customer"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Verify that the response contains the expected total cost map
        // For example, you can parse the response content as JSON and assert its properties
    }

    @Test
    public void testGetTotalCostForProducts() throws Exception {
        // Define the expected total cost map for products
        Map<String, BigDecimal> expectedTotalCostMap =  Define your expected map here ;

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/transactions/report/total-cost-per-product"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Verify that the response contains the expected total cost map
        // For example, you can parse the response content as JSON and assert its properties
    }

    @Test
    public void testGetTransactionsInAustraliaForCustomer() throws Exception {
        // Define the expected list of transactions in Australia for a customer
        List<Transaction> expectedTransactions =  Define your expected list here ;

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/transactions/report/transactions-in-australia/{customerId}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Verify that the response contains the expected transactions
        // For example, you can parse the response content as JSON and assert its properties
    }*/
    
    @Test
    public void testYourEndpoint() throws Exception {
        // Write your test logic here, authentication is disabled for testing
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8086"))
        
               .andExpect(status().isOk());
    }


   

    // Configuration for the custom filter
    @Lazy
    @Configuration
    public static class TestConfig {
        @Bean
        public NoAuthenticationFilter noAuthenticationFilter() {
            return new NoAuthenticationFilter();
        }
    }
}
