package com.tsystem.tms.service;

import com.tsystem.tms.domain.CustomerProduct;
import com.tsystem.tms.repository.CustomerProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing CustomerProduct.
 */
@Service
@Transactional
public class CustomerProductService {

    private final Logger log = LoggerFactory.getLogger(CustomerProductService.class);
    
    @Inject
    private CustomerProductRepository customerProductRepository;
    
    /**
     * Save a customerProduct.
     * 
     * @param customerProduct the entity to save
     * @return the persisted entity
     */
    public CustomerProduct save(CustomerProduct customerProduct) {
        log.debug("Request to save CustomerProduct : {}", customerProduct);
        CustomerProduct result = customerProductRepository.save(customerProduct);
        return result;
    }

    /**
     *  Get all the customerProducts.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<CustomerProduct> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerProducts");
        Page<CustomerProduct> result = customerProductRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one customerProduct by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public CustomerProduct findOne(Long id) {
        log.debug("Request to get CustomerProduct : {}", id);
        CustomerProduct customerProduct = customerProductRepository.findOne(id);
        return customerProduct;
    }

    /**
     *  Delete the  customerProduct by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CustomerProduct : {}", id);
        customerProductRepository.delete(id);
    }
}
