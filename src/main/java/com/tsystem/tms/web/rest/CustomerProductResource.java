package com.tsystem.tms.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tsystem.tms.domain.CustomerProduct;
import com.tsystem.tms.service.CustomerProductService;
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
 * REST controller for managing CustomerProduct.
 */
@RestController
@RequestMapping("/api")
public class CustomerProductResource {

    private final Logger log = LoggerFactory.getLogger(CustomerProductResource.class);
        
    @Inject
    private CustomerProductService customerProductService;
    
    /**
     * POST  /customer-products : Create a new customerProduct.
     *
     * @param customerProduct the customerProduct to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customerProduct, or with status 400 (Bad Request) if the customerProduct has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/customer-products",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerProduct> createCustomerProduct(@Valid @RequestBody CustomerProduct customerProduct) throws URISyntaxException {
        log.debug("REST request to save CustomerProduct : {}", customerProduct);
        if (customerProduct.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("customerProduct", "idexists", "A new customerProduct cannot already have an ID")).body(null);
        }
        CustomerProduct result = customerProductService.save(customerProduct);
        return ResponseEntity.created(new URI("/api/customer-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("customerProduct", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /customer-products : Updates an existing customerProduct.
     *
     * @param customerProduct the customerProduct to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated customerProduct,
     * or with status 400 (Bad Request) if the customerProduct is not valid,
     * or with status 500 (Internal Server Error) if the customerProduct couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/customer-products",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerProduct> updateCustomerProduct(@Valid @RequestBody CustomerProduct customerProduct) throws URISyntaxException {
        log.debug("REST request to update CustomerProduct : {}", customerProduct);
        if (customerProduct.getId() == null) {
            return createCustomerProduct(customerProduct);
        }
        CustomerProduct result = customerProductService.save(customerProduct);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("customerProduct", customerProduct.getId().toString()))
            .body(result);
    }

    /**
     * GET  /customer-products : get all the customerProducts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of customerProducts in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/customer-products",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CustomerProduct>> getAllCustomerProducts(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CustomerProducts");
        Page<CustomerProduct> page = customerProductService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/customer-products");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /customer-products/:id : get the "id" customerProduct.
     *
     * @param id the id of the customerProduct to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customerProduct, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/customer-products/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CustomerProduct> getCustomerProduct(@PathVariable Long id) {
        log.debug("REST request to get CustomerProduct : {}", id);
        CustomerProduct customerProduct = customerProductService.findOne(id);
        return Optional.ofNullable(customerProduct)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /customer-products/:id : delete the "id" customerProduct.
     *
     * @param id the id of the customerProduct to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/customer-products/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCustomerProduct(@PathVariable Long id) {
        log.debug("REST request to delete CustomerProduct : {}", id);
        customerProductService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("customerProduct", id.toString())).build();
    }

}
