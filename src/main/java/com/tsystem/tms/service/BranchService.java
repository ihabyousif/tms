package com.tsystem.tms.service;

import com.tsystem.tms.domain.Branch;
import com.tsystem.tms.repository.BranchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Branch.
 */
@Service
@Transactional
public class BranchService {

    private final Logger log = LoggerFactory.getLogger(BranchService.class);
    
    @Inject
    private BranchRepository branchRepository;
    
    /**
     * Save a branch.
     * 
     * @param branch the entity to save
     * @return the persisted entity
     */
    public Branch save(Branch branch) {
        log.debug("Request to save Branch : {}", branch);
        Branch result = branchRepository.save(branch);
        return result;
    }

    /**
     *  Get all the branches.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Branch> findAll(Pageable pageable) {
        log.debug("Request to get all Branches");
        Page<Branch> result = branchRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one branch by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Branch findOne(Long id) {
        log.debug("Request to get Branch : {}", id);
        Branch branch = branchRepository.findOne(id);
        return branch;
    }

    /**
     *  Delete the  branch by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Branch : {}", id);
        branchRepository.delete(id);
    }
}
