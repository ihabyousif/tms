package com.tsystem.tms.web.rest;

import com.tsystem.tms.TmsApp;
import com.tsystem.tms.domain.CustomerProduct;
import com.tsystem.tms.repository.CustomerProductRepository;
import com.tsystem.tms.service.CustomerProductService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.math.BigDecimal;;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CustomerProductResource REST controller.
 *
 * @see CustomerProductResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TmsApp.class)
@WebAppConfiguration
@IntegrationTest
public class CustomerProductResourceIntTest {


    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_DISCOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DISCOUNT = new BigDecimal(2);

    @Inject
    private CustomerProductRepository customerProductRepository;

    @Inject
    private CustomerProductService customerProductService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCustomerProductMockMvc;

    private CustomerProduct customerProduct;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CustomerProductResource customerProductResource = new CustomerProductResource();
        ReflectionTestUtils.setField(customerProductResource, "customerProductService", customerProductService);
        this.restCustomerProductMockMvc = MockMvcBuilders.standaloneSetup(customerProductResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        customerProduct = new CustomerProduct();
        customerProduct.setPrice(DEFAULT_PRICE);
        customerProduct.setDiscount(DEFAULT_DISCOUNT);
    }

    @Test
    @Transactional
    public void createCustomerProduct() throws Exception {
        int databaseSizeBeforeCreate = customerProductRepository.findAll().size();

        // Create the CustomerProduct

        restCustomerProductMockMvc.perform(post("/api/customer-products")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerProduct)))
                .andExpect(status().isCreated());

        // Validate the CustomerProduct in the database
        List<CustomerProduct> customerProducts = customerProductRepository.findAll();
        assertThat(customerProducts).hasSize(databaseSizeBeforeCreate + 1);
        CustomerProduct testCustomerProduct = customerProducts.get(customerProducts.size() - 1);
        assertThat(testCustomerProduct.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testCustomerProduct.getDiscount()).isEqualTo(DEFAULT_DISCOUNT);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerProductRepository.findAll().size();
        // set the field null
        customerProduct.setPrice(null);

        // Create the CustomerProduct, which fails.

        restCustomerProductMockMvc.perform(post("/api/customer-products")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerProduct)))
                .andExpect(status().isBadRequest());

        List<CustomerProduct> customerProducts = customerProductRepository.findAll();
        assertThat(customerProducts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCustomerProducts() throws Exception {
        // Initialize the database
        customerProductRepository.saveAndFlush(customerProduct);

        // Get all the customerProducts
        restCustomerProductMockMvc.perform(get("/api/customer-products?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(customerProduct.getId().intValue())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
                .andExpect(jsonPath("$.[*].discount").value(hasItem(DEFAULT_DISCOUNT.intValue())));
    }

    @Test
    @Transactional
    public void getCustomerProduct() throws Exception {
        // Initialize the database
        customerProductRepository.saveAndFlush(customerProduct);

        // Get the customerProduct
        restCustomerProductMockMvc.perform(get("/api/customer-products/{id}", customerProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(customerProduct.getId().intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.discount").value(DEFAULT_DISCOUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCustomerProduct() throws Exception {
        // Get the customerProduct
        restCustomerProductMockMvc.perform(get("/api/customer-products/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomerProduct() throws Exception {
        // Initialize the database
        customerProductService.save(customerProduct);

        int databaseSizeBeforeUpdate = customerProductRepository.findAll().size();

        // Update the customerProduct
        CustomerProduct updatedCustomerProduct = new CustomerProduct();
        updatedCustomerProduct.setId(customerProduct.getId());
        updatedCustomerProduct.setPrice(UPDATED_PRICE);
        updatedCustomerProduct.setDiscount(UPDATED_DISCOUNT);

        restCustomerProductMockMvc.perform(put("/api/customer-products")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCustomerProduct)))
                .andExpect(status().isOk());

        // Validate the CustomerProduct in the database
        List<CustomerProduct> customerProducts = customerProductRepository.findAll();
        assertThat(customerProducts).hasSize(databaseSizeBeforeUpdate);
        CustomerProduct testCustomerProduct = customerProducts.get(customerProducts.size() - 1);
        assertThat(testCustomerProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCustomerProduct.getDiscount()).isEqualTo(UPDATED_DISCOUNT);
    }

    @Test
    @Transactional
    public void deleteCustomerProduct() throws Exception {
        // Initialize the database
        customerProductService.save(customerProduct);

        int databaseSizeBeforeDelete = customerProductRepository.findAll().size();

        // Get the customerProduct
        restCustomerProductMockMvc.perform(delete("/api/customer-products/{id}", customerProduct.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CustomerProduct> customerProducts = customerProductRepository.findAll();
        assertThat(customerProducts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
