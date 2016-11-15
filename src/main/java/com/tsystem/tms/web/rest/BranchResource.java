package com.tsystem.tms.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tsystem.tms.domain.Branch;
import com.tsystem.tms.service.BranchService;
import com.tsystem.tms.web.rest.util.HeaderUtil;
import com.tsystem.tms.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Branch.
 */
@RestController
@RequestMapping("/api")
public class BranchResource {

    private final Logger log = LoggerFactory.getLogger(BranchResource.class);
        
    @Inject
    private BranchService branchService;
    
    /**
     * POST  /branches : Create a new branch.
     *
     * @param branch the branch to create
     * @return the ResponseEntity with status 201 (Created) and with body the new branch, or with status 400 (Bad Request) if the branch has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/branches",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Branch> createBranch(@Valid @RequestBody Branch branch) throws URISyntaxException {
        log.debug("REST request to save Branch : {}", branch);
        if (branch.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("branch", "idexists", "A new branch cannot already have an ID")).body(null);
        }
        Branch result = branchService.save(branch);
        return ResponseEntity.created(new URI("/api/branches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("branch", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /branches : Updates an existing branch.
     *
     * @param branch the branch to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated branch,
     * or with status 400 (Bad Request) if the branch is not valid,
     * or with status 500 (Internal Server Error) if the branch couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/branches",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Branch> updateBranch(@Valid @RequestBody Branch branch) throws URISyntaxException {
        log.debug("REST request to update Branch : {}", branch);
        if (branch.getId() == null) {
            return createBranch(branch);
        }
        Branch result = branchService.save(branch);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("branch", branch.getId().toString()))
            .body(result);
    }

    /**
     * GET  /branches : get all the branches.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of branches in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/branches",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Branch>> getAllBranches(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Branches");
        Page<Branch> page = branchService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/branches");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /branches/:id : get the "id" branch.
     *
     * @param id the id of the branch to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the branch, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/branches/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Branch> getBranch(@PathVariable Long id) {
        log.debug("REST request to get Branch : {}", id);
        Branch branch = branchService.findOne(id);
        return Optional.ofNullable(branch)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /branches/:id : delete the "id" branch.
     *
     * @param id the id of the branch to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/branches/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBranch(@PathVariable Long id) {
        log.debug("REST request to delete Branch : {}", id);
        branchService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("branch", id.toString())).build();
    }

}
