package com.tsystem.tms.service;

import com.tsystem.tms.domain.Transaction;
import com.tsystem.tms.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Transaction.
 */
@Service
@Transactional
public class TransactionService {

    private final Logger log = LoggerFactory.getLogger(TransactionService.class);
    
    @Inject
    private TransactionRepository transactionRepository;
    
    /**
     * Save a transaction.
     * 
     * @param transaction the entity to save
     * @return the persisted entity
     */
    public Transaction save(Transaction transaction) {
        log.debug("Request to save Transaction : {}", transaction);
        Transaction result = transactionRepository.save(transaction);
        return result;
    }

    /**
     *  Get all the transactions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Transaction> findAll(Pageable pageable) {
        log.debug("Request to get all Transactions");
        Page<Transaction> result = transactionRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one transaction by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Transaction findOne(Long id) {
        log.debug("Request to get Transaction : {}", id);
        Transaction transaction = transactionRepository.findOne(id);
        return transaction;
    }

    /**
     *  Delete the  transaction by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Transaction : {}", id);
        transactionRepository.delete(id);
    }
}
