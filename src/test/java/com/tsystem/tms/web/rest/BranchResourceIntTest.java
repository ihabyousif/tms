package com.tsystem.tms.web.rest;

import com.tsystem.tms.TmsApp;
import com.tsystem.tms.domain.Branch;
import com.tsystem.tms.repository.BranchRepository;
import com.tsystem.tms.service.BranchService;

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
 * Test class for the BranchResource REST controller.
 *
 * @see BranchResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TmsApp.class)
@WebAppConfiguration
@IntegrationTest
public class BranchResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_PHONE_1 = "AAAAA";
    private static final String UPDATED_PHONE_1 = "BBBBB";
    private static final String DEFAULT_PHONE_2 = "AAAAA";
    private static final String UPDATED_PHONE_2 = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_ADDRESS = "AAAAA";
    private static final String UPDATED_ADDRESS = "BBBBB";

    @Inject
    private BranchRepository branchRepository;

    @Inject
    private BranchService branchService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBranchMockMvc;

    private Branch branch;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BranchResource branchResource = new BranchResource();
        ReflectionTestUtils.setField(branchResource, "branchService", branchService);
        this.restBranchMockMvc = MockMvcBuilders.standaloneSetup(branchResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        branch = new Branch();
        branch.setName(DEFAULT_NAME);
        branch.setPhone1(DEFAULT_PHONE_1);
        branch.setPhone2(DEFAULT_PHONE_2);
        branch.setDescription(DEFAULT_DESCRIPTION);
        branch.setAddress(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    public void createBranch() throws Exception {
        int databaseSizeBeforeCreate = branchRepository.findAll().size();

        // Create the Branch

        restBranchMockMvc.perform(post("/api/branches")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(branch)))
                .andExpect(status().isCreated());

        // Validate the Branch in the database
        List<Branch> branches = branchRepository.findAll();
        assertThat(branches).hasSize(databaseSizeBeforeCreate + 1);
        Branch testBranch = branches.get(branches.size() - 1);
        assertThat(testBranch.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBranch.getPhone1()).isEqualTo(DEFAULT_PHONE_1);
        assertThat(testBranch.getPhone2()).isEqualTo(DEFAULT_PHONE_2);
        assertThat(testBranch.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBranch.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = branchRepository.findAll().size();
        // set the field null
        branch.setName(null);

        // Create the Branch, which fails.

        restBranchMockMvc.perform(post("/api/branches")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(branch)))
                .andExpect(status().isBadRequest());

        List<Branch> branches = branchRepository.findAll();
        assertThat(branches).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhone1IsRequired() throws Exception {
        int databaseSizeBeforeTest = branchRepository.findAll().size();
        // set the field null
        branch.setPhone1(null);

        // Create the Branch, which fails.

        restBranchMockMvc.perform(post("/api/branches")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(branch)))
                .andExpect(status().isBadRequest());

        List<Branch> branches = branchRepository.findAll();
        assertThat(branches).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = branchRepository.findAll().size();
        // set the field null
        branch.setAddress(null);

        // Create the Branch, which fails.

        restBranchMockMvc.perform(post("/api/branches")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(branch)))
                .andExpect(status().isBadRequest());

        List<Branch> branches = branchRepository.findAll();
        assertThat(branches).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBranches() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branches
        restBranchMockMvc.perform(get("/api/branches?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(branch.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].phone1").value(hasItem(DEFAULT_PHONE_1.toString())))
                .andExpect(jsonPath("$.[*].phone2").value(hasItem(DEFAULT_PHONE_2.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())));
    }

    @Test
    @Transactional
    public void getBranch() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get the branch
        restBranchMockMvc.perform(get("/api/branches/{id}", branch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(branch.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.phone1").value(DEFAULT_PHONE_1.toString()))
            .andExpect(jsonPath("$.phone2").value(DEFAULT_PHONE_2.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBranch() throws Exception {
        // Get the branch
        restBranchMockMvc.perform(get("/api/branches/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBranch() throws Exception {
        // Initialize the database
        branchService.save(branch);

        int databaseSizeBeforeUpdate = branchRepository.findAll().size();

        // Update the branch
        Branch updatedBranch = new Branch();
        updatedBranch.setId(branch.getId());
        updatedBranch.setName(UPDATED_NAME);
        updatedBranch.setPhone1(UPDATED_PHONE_1);
        updatedBranch.setPhone2(UPDATED_PHONE_2);
        updatedBranch.setDescription(UPDATED_DESCRIPTION);
        updatedBranch.setAddress(UPDATED_ADDRESS);

        restBranchMockMvc.perform(put("/api/branches")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBranch)))
                .andExpect(status().isOk());

        // Validate the Branch in the database
        List<Branch> branches = branchRepository.findAll();
        assertThat(branches).hasSize(databaseSizeBeforeUpdate);
        Branch testBranch = branches.get(branches.size() - 1);
        assertThat(testBranch.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBranch.getPhone1()).isEqualTo(UPDATED_PHONE_1);
        assertThat(testBranch.getPhone2()).isEqualTo(UPDATED_PHONE_2);
        assertThat(testBranch.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBranch.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void deleteBranch() throws Exception {
        // Initialize the database
        branchService.save(branch);

        int databaseSizeBeforeDelete = branchRepository.findAll().size();

        // Get the branch
        restBranchMockMvc.perform(delete("/api/branches/{id}", branch.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Branch> branches = branchRepository.findAll();
        assertThat(branches).hasSize(databaseSizeBeforeDelete - 1);
    }
}
