package com.tsystem.tms.web.rest;

import com.tsystem.tms.TmsApp;
import com.tsystem.tms.domain.CustomerGroup;
import com.tsystem.tms.repository.CustomerGroupRepository;
import com.tsystem.tms.service.CustomerGroupService;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CustomerGroupResource REST controller.
 *
 * @see CustomerGroupResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TmsApp.class)
@WebAppConfiguration
@IntegrationTest
public class CustomerGroupResourceIntTest {

    private static final String DEFAULT_GROUP_NAME = "AAAAA";
    private static final String UPDATED_GROUP_NAME = "BBBBB";

    @Inject
    private CustomerGroupRepository customerGroupRepository;

    @Inject
    private CustomerGroupService customerGroupService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCustomerGroupMockMvc;

    private CustomerGroup customerGroup;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CustomerGroupResource customerGroupResource = new CustomerGroupResource();
        ReflectionTestUtils.setField(customerGroupResource, "customerGroupService", customerGroupService);
        this.restCustomerGroupMockMvc = MockMvcBuilders.standaloneSetup(customerGroupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        customerGroup = new CustomerGroup();
        customerGroup.setGroupName(DEFAULT_GROUP_NAME);
    }

    @Test
    @Transactional
    public void createCustomerGroup() throws Exception {
        int databaseSizeBeforeCreate = customerGroupRepository.findAll().size();

        // Create the CustomerGroup

        restCustomerGroupMockMvc.perform(post("/api/customer-groups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerGroup)))
                .andExpect(status().isCreated());

        // Validate the CustomerGroup in the database
        List<CustomerGroup> customerGroups = customerGroupRepository.findAll();
        assertThat(customerGroups).hasSize(databaseSizeBeforeCreate + 1);
        CustomerGroup testCustomerGroup = customerGroups.get(customerGroups.size() - 1);
        assertThat(testCustomerGroup.getGroupName()).isEqualTo(DEFAULT_GROUP_NAME);
    }

    @Test
    @Transactional
    public void checkGroupNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerGroupRepository.findAll().size();
        // set the field null
        customerGroup.setGroupName(null);

        // Create the CustomerGroup, which fails.

        restCustomerGroupMockMvc.perform(post("/api/customer-groups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customerGroup)))
                .andExpect(status().isBadRequest());

        List<CustomerGroup> customerGroups = customerGroupRepository.findAll();
        assertThat(customerGroups).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCustomerGroups() throws Exception {
        // Initialize the database
        customerGroupRepository.saveAndFlush(customerGroup);

        // Get all the customerGroups
        restCustomerGroupMockMvc.perform(get("/api/customer-groups?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(customerGroup.getId().intValue())))
                .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME.toString())));
    }

    @Test
    @Transactional
    public void getCustomerGroup() throws Exception {
        // Initialize the database
        customerGroupRepository.saveAndFlush(customerGroup);

        // Get the customerGroup
        restCustomerGroupMockMvc.perform(get("/api/customer-groups/{id}", customerGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(customerGroup.getId().intValue()))
            .andExpect(jsonPath("$.groupName").value(DEFAULT_GROUP_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCustomerGroup() throws Exception {
        // Get the customerGroup
        restCustomerGroupMockMvc.perform(get("/api/customer-groups/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomerGroup() throws Exception {
        // Initialize the database
        customerGroupService.save(customerGroup);

        int databaseSizeBeforeUpdate = customerGroupRepository.findAll().size();

        // Update the customerGroup
        CustomerGroup updatedCustomerGroup = new CustomerGroup();
        updatedCustomerGroup.setId(customerGroup.getId());
        updatedCustomerGroup.setGroupName(UPDATED_GROUP_NAME);

        restCustomerGroupMockMvc.perform(put("/api/customer-groups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCustomerGroup)))
                .andExpect(status().isOk());

        // Validate the CustomerGroup in the database
        List<CustomerGroup> customerGroups = customerGroupRepository.findAll();
        assertThat(customerGroups).hasSize(databaseSizeBeforeUpdate);
        CustomerGroup testCustomerGroup = customerGroups.get(customerGroups.size() - 1);
        assertThat(testCustomerGroup.getGroupName()).isEqualTo(UPDATED_GROUP_NAME);
    }

    @Test
    @Transactional
    public void deleteCustomerGroup() throws Exception {
        // Initialize the database
        customerGroupService.save(customerGroup);

        int databaseSizeBeforeDelete = customerGroupRepository.findAll().size();

        // Get the customerGroup
        restCustomerGroupMockMvc.perform(delete("/api/customer-groups/{id}", customerGroup.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CustomerGroup> customerGroups = customerGroupRepository.findAll();
        assertThat(customerGroups).hasSize(databaseSizeBeforeDelete - 1);
    }
}
