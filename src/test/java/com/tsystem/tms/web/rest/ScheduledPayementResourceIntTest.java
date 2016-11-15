package com.tsystem.tms.web.rest;

import com.tsystem.tms.TmsApp;
import com.tsystem.tms.domain.ScheduledPayement;
import com.tsystem.tms.repository.ScheduledPayementRepository;
import com.tsystem.tms.service.ScheduledPayementService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.math.BigDecimal;;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ScheduledPayementResource REST controller.
 *
 * @see ScheduledPayementResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TmsApp.class)
@WebAppConfiguration
@IntegrationTest
public class ScheduledPayementResourceIntTest {


    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    @Inject
    private ScheduledPayementRepository scheduledPayementRepository;

    @Inject
    private ScheduledPayementService scheduledPayementService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restScheduledPayementMockMvc;

    private ScheduledPayement scheduledPayement;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ScheduledPayementResource scheduledPayementResource = new ScheduledPayementResource();
        ReflectionTestUtils.setField(scheduledPayementResource, "scheduledPayementService", scheduledPayementService);
        this.restScheduledPayementMockMvc = MockMvcBuilders.standaloneSetup(scheduledPayementResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        scheduledPayement = new ScheduledPayement();
        scheduledPayement.setDate(DEFAULT_DATE);
        scheduledPayement.setAmount(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createScheduledPayement() throws Exception {
        int databaseSizeBeforeCreate = scheduledPayementRepository.findAll().size();

        // Create the ScheduledPayement

        restScheduledPayementMockMvc.perform(post("/api/scheduled-payements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scheduledPayement)))
                .andExpect(status().isCreated());

        // Validate the ScheduledPayement in the database
        List<ScheduledPayement> scheduledPayements = scheduledPayementRepository.findAll();
        assertThat(scheduledPayements).hasSize(databaseSizeBeforeCreate + 1);
        ScheduledPayement testScheduledPayement = scheduledPayements.get(scheduledPayements.size() - 1);
        assertThat(testScheduledPayement.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testScheduledPayement.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = scheduledPayementRepository.findAll().size();
        // set the field null
        scheduledPayement.setDate(null);

        // Create the ScheduledPayement, which fails.

        restScheduledPayementMockMvc.perform(post("/api/scheduled-payements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scheduledPayement)))
                .andExpect(status().isBadRequest());

        List<ScheduledPayement> scheduledPayements = scheduledPayementRepository.findAll();
        assertThat(scheduledPayements).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = scheduledPayementRepository.findAll().size();
        // set the field null
        scheduledPayement.setAmount(null);

        // Create the ScheduledPayement, which fails.

        restScheduledPayementMockMvc.perform(post("/api/scheduled-payements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scheduledPayement)))
                .andExpect(status().isBadRequest());

        List<ScheduledPayement> scheduledPayements = scheduledPayementRepository.findAll();
        assertThat(scheduledPayements).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllScheduledPayements() throws Exception {
        // Initialize the database
        scheduledPayementRepository.saveAndFlush(scheduledPayement);

        // Get all the scheduledPayements
        restScheduledPayementMockMvc.perform(get("/api/scheduled-payements?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(scheduledPayement.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())));
    }

    @Test
    @Transactional
    public void getScheduledPayement() throws Exception {
        // Initialize the database
        scheduledPayementRepository.saveAndFlush(scheduledPayement);

        // Get the scheduledPayement
        restScheduledPayementMockMvc.perform(get("/api/scheduled-payements/{id}", scheduledPayement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(scheduledPayement.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingScheduledPayement() throws Exception {
        // Get the scheduledPayement
        restScheduledPayementMockMvc.perform(get("/api/scheduled-payements/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateScheduledPayement() throws Exception {
        // Initialize the database
        scheduledPayementService.save(scheduledPayement);

        int databaseSizeBeforeUpdate = scheduledPayementRepository.findAll().size();

        // Update the scheduledPayement
        ScheduledPayement updatedScheduledPayement = new ScheduledPayement();
        updatedScheduledPayement.setId(scheduledPayement.getId());
        updatedScheduledPayement.setDate(UPDATED_DATE);
        updatedScheduledPayement.setAmount(UPDATED_AMOUNT);

        restScheduledPayementMockMvc.perform(put("/api/scheduled-payements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedScheduledPayement)))
                .andExpect(status().isOk());

        // Validate the ScheduledPayement in the database
        List<ScheduledPayement> scheduledPayements = scheduledPayementRepository.findAll();
        assertThat(scheduledPayements).hasSize(databaseSizeBeforeUpdate);
        ScheduledPayement testScheduledPayement = scheduledPayements.get(scheduledPayements.size() - 1);
        assertThat(testScheduledPayement.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testScheduledPayement.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void deleteScheduledPayement() throws Exception {
        // Initialize the database
        scheduledPayementService.save(scheduledPayement);

        int databaseSizeBeforeDelete = scheduledPayementRepository.findAll().size();

        // Get the scheduledPayement
        restScheduledPayementMockMvc.perform(delete("/api/scheduled-payements/{id}", scheduledPayement.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ScheduledPayement> scheduledPayements = scheduledPayementRepository.findAll();
        assertThat(scheduledPayements).hasSize(databaseSizeBeforeDelete - 1);
    }
}
