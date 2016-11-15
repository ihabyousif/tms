package com.tsystem.tms.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tsystem.tms.domain.CustomerGroup;
import com.tsystem.tms.service.CustomerGroupService;
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
 * REST controller for managing CustomerGroup.
 */
@RestController
@RequestMapping("/api")
public class CustomerGroupResource {

    private final Logger log = LoggerFactory.getLogger(CustomerGroupResource.class);
        
    @Inject
    private CustomerGroupService customerGroupService;
    
    /**
     * POST  /customer-groups : Create a new customerGroup.
     *
     * @param customerGroup the customerGroup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customerGroup, or with status 400 (Bad Request) if the customerGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/customer-groups",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerGroup> createCustomerGroup(@Valid @RequestBody CustomerGroup customerGroup) throws URISyntaxException {
        log.debug("REST request to save CustomerGroup : {}", customerGroup);
        if (customerGroup.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("customerGroup", "idexists", "A new customerGroup cannot already have an ID")).body(null);
        }
        CustomerGroup result = customerGroupService.save(customerGroup);
        return ResponseEntity.created(new URI("/api/customer-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("customerGroup", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /customer-groups : Updates an existing customerGroup.
     *
     * @param customerGroup the customerGroup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated customerGroup,
     * or with status 400 (Bad Request) if the customerGroup is not valid,
     * or with status 500 (Internal Server Error) if the customerGroup couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/customer-groups",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerGroup> updateCustomerGroup(@Valid @RequestBody CustomerGroup customerGroup) throws URISyntaxException {
        log.debug("REST request to update CustomerGroup : {}", customerGroup);
        if (customerGroup.getId() == null) {
            return createCustomerGroup(customerGroup);
        }
        CustomerGroup result = customerGroupService.save(customerGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("customerGroup", customerGroup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /customer-groups : get all the customerGroups.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of customerGroups in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/customer-groups",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CustomerGroup>> getAllCustomerGroups(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CustomerGroups");
        Page<CustomerGroup> page = customerGroupService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/customer-groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /customer-groups/:id : get the "id" customerGroup.
     *
     * @param id the id of the customerGroup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customerGroup, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/customer-groups/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerGroup> getCustomerGroup(@PathVariable Long id) {
        log.debug("REST request to get CustomerGroup : {}", id);
        CustomerGroup customerGroup = customerGroupService.findOne(id);
        return Optional.ofNullable(customerGroup)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /customer-groups/:id : delete the "id" customerGroup.
     *
     * @param id the id of the customerGroup to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/customer-groups/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCustomerGroup(@PathVariable Long id) {
        log.debug("REST request to delete CustomerGroup : {}", id);
        customerGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("customerGroup", id.toString())).build();
    }

}
