package com.tsystem.tms.service;

import com.tsystem.tms.domain.CustomerGroup;
import com.tsystem.tms.repository.CustomerGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing CustomerGroup.
 */
@Service
@Transactional
public class CustomerGroupService {

    private final Logger log = LoggerFactory.getLogger(CustomerGroupService.class);
    
    @Inject
    private CustomerGroupRepository customerGroupRepository;
    
    /**
     * Save a customerGroup.
     * 
     * @param customerGroup the entity to save
     * @return the persisted entity
     */
    public CustomerGroup save(CustomerGroup customerGroup) {
        log.debug("Request to save CustomerGroup : {}", customerGroup);
        CustomerGroup result = customerGroupRepository.save(customerGroup);
        return result;
    }

    /**
     *  Get all the customerGroups.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<CustomerGroup> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerGroups");
        Page<CustomerGroup> result = customerGroupRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one customerGroup by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public CustomerGroup findOne(Long id) {
        log.debug("Request to get CustomerGroup : {}", id);
        CustomerGroup customerGroup = customerGroupRepository.findOne(id);
        return customerGroup;
    }

    /**
     *  Delete the  customerGroup by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CustomerGroup : {}", id);
        customerGroupRepository.delete(id);
    }
}
