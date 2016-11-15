package com.tsystem.tms.service;

import com.tsystem.tms.domain.ScheduledPayement;
import com.tsystem.tms.repository.ScheduledPayementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing ScheduledPayement.
 */
@Service
@Transactional
public class ScheduledPayementService {

    private final Logger log = LoggerFactory.getLogger(ScheduledPayementService.class);
    
    @Inject
    private ScheduledPayementRepository scheduledPayementRepository;
    
    /**
     * Save a scheduledPayement.
     * 
     * @param scheduledPayement the entity to save
     * @return the persisted entity
     */
    public ScheduledPayement save(ScheduledPayement scheduledPayement) {
        log.debug("Request to save ScheduledPayement : {}", scheduledPayement);
        ScheduledPayement result = scheduledPayementRepository.save(scheduledPayement);
        return result;
    }

    /**
     *  Get all the scheduledPayements.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ScheduledPayement> findAll(Pageable pageable) {
        log.debug("Request to get all ScheduledPayements");
        Page<ScheduledPayement> result = scheduledPayementRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one scheduledPayement by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ScheduledPayement findOne(Long id) {
        log.debug("Request to get ScheduledPayement : {}", id);
        ScheduledPayement scheduledPayement = scheduledPayementRepository.findOne(id);
        return scheduledPayement;
    }

    /**
     *  Delete the  scheduledPayement by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ScheduledPayement : {}", id);
        scheduledPayementRepository.delete(id);
    }
}
